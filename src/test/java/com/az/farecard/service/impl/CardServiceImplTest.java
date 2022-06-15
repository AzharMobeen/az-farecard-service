package com.az.farecard.service.impl;

import com.az.farecard.dto.CardSwipeType;
import com.az.farecard.dto.TransactionRequest;
import com.az.farecard.entity.Card;
import com.az.farecard.entity.CardUsage;
import com.az.farecard.exception.CustomRuntimeException;
import com.az.farecard.repository.CardRepository;
import com.az.farecard.repository.CardUsageRepository;
import com.az.farecard.util.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


class CardServiceImplTest {

    @InjectMocks
    private CardServiceImpl cardService;
    @Mock
    private CardRepository cardRepository;
    @Mock
    private CardUsageRepository cardUsageRepository;

    @BeforeEach
    void beforeEach() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(cardService, "maxPossibleFare", 3.20);
    }

    @DisplayName("Card SwipeIn Test cases")
    @Nested
    class CardTransactionSwipeInTest {

        @DisplayName("Failed scenario 1: Card not exist")
        @Test
        void cardTransactionFailedScenarioFirst() {
            TransactionRequest request = TestUtils.buildTransactionRequest(CardSwipeType.IN, 1L);
            Mockito.when(cardRepository.findById(request.getCardId())).thenReturn(Optional.empty());
            assertThrows(CustomRuntimeException.class, () -> cardService.cardTransaction(request),
                    "Card not exist");

        }

        @DisplayName("Failed scenario 2: Out of balance")
        @Test
        void cardTransactionFailedScenarioSecond() {
            TransactionRequest request = TestUtils.buildTransactionRequest(CardSwipeType.IN, 1L);
            Card card = TestUtils.buildCard(request.getCardId(), 1.00);
            Mockito.when(cardRepository.findById(request.getCardId())).thenReturn(Optional.of(card));
            assertThrows(CustomRuntimeException.class, () -> cardService.cardTransaction(request),
                    "Out of balance");

        }

        @DisplayName("Success scenario SwipeIn")
        @Test
        void cardTransactionSuccessScenario() {
            TransactionRequest request = TestUtils.buildTransactionRequest(CardSwipeType.IN, 1L);
            Card card = TestUtils.buildCard(request.getCardId(), 30.00);
            Mockito.when(cardRepository.findById(request.getCardId())).thenReturn(Optional.of(card));
            Mockito.when(cardRepository.save(card)).thenReturn(card);
            assertEquals( cardService.cardTransaction(request), BigDecimal.valueOf(26.8));
        }

        @DisplayName("Success scenario SwipeIn for any bus journey")
        @Test
        void cardTransactionSuccessScenarioSecond() {
            TransactionRequest request = TestUtils.buildTransactionRequest(CardSwipeType.IN, 0L);
            Card card = TestUtils.buildCard(request.getCardId(), 30.00);
            Mockito.when(cardRepository.findById(request.getCardId())).thenReturn(Optional.of(card));
            Mockito.when(cardRepository.save(card)).thenReturn(card);
            assertEquals( cardService.cardTransaction(request), BigDecimal.valueOf(28.2));
        }
    }

    @DisplayName("Card SwipeOut Test cases")
    @Nested
    class CardTransactionSwipeOutTest {


        @DisplayName("Failed scenario 1: Invalid Request")
        @Test
        void cardTransactionFailedScenarioFirst() {
            TransactionRequest request = TestUtils.buildTransactionRequest(CardSwipeType.OUT, 1L);
            Card card = TestUtils.buildCard(request.getCardId(), 30.00);
            Mockito.when(cardRepository.findById(request.getCardId())).thenReturn(Optional.of(card));
            //CardUsage cardUsage = TestUtils.buildCardUsage(26.8);
            Mockito.when(cardUsageRepository.findTopByCardOrderByUsageIdDesc(card)).thenReturn(null);

            assertThrows(CustomRuntimeException.class, () -> cardService.cardTransaction(request),
                    "Invalid Request");

        }

        @DisplayName("Failed scenario 2: Invalid Request")
        @Test
        void cardTransactionFailedScenarioSecond() {
            TransactionRequest request = TestUtils.buildTransactionRequest(CardSwipeType.OUT, 1L);
            Card card = TestUtils.buildCard(request.getCardId(), 30.00);
            Mockito.when(cardRepository.findById(request.getCardId())).thenReturn(Optional.of(card));
            CardUsage cardUsage = TestUtils.buildCardUsage(26.8, card, CardSwipeType.OUT, 1L);
            Mockito.when(cardUsageRepository.findTopByCardOrderByUsageIdDesc(card)).thenReturn(cardUsage);

            assertThrows(CustomRuntimeException.class, () -> cardService.cardTransaction(request),
                    "Invalid Request");

        }

        @DisplayName("Success scenario SwipeOut: Anywhere in Zone 1 [2.50]")
        @Test
        void cardTransactionSuccessScenarioFirst() {
            TransactionRequest request = TestUtils.buildTransactionRequest(CardSwipeType.OUT, 1L);
            Card card = TestUtils.buildCard(request.getCardId(), 26.8);
            Mockito.when(cardRepository.findById(request.getCardId())).thenReturn(Optional.of(card));
            CardUsage cardUsage = TestUtils.buildCardUsage(2.50, card, CardSwipeType.IN, 1L);
            Mockito.when(cardUsageRepository.findTopByCardOrderByUsageIdDesc(card)).thenReturn(cardUsage);
            Mockito.when(cardRepository.save(card)).thenReturn(card);
            assertEquals( cardService.cardTransaction(request), BigDecimal.valueOf(27.5));
        }

        @DisplayName("Success scenario SwipeOut (1-2): Any two zones including zone 1 [3.00]")
        @Test
        void cardTransactionSuccessScenarioSecond() {
            TransactionRequest request = TestUtils.buildTransactionRequest(CardSwipeType.OUT, 2L);
            Card card = TestUtils.buildCard(request.getCardId(), 26.8);
            Mockito.when(cardRepository.findById(request.getCardId())).thenReturn(Optional.of(card));
            CardUsage cardUsage = TestUtils.buildCardUsage(3.00, card, CardSwipeType.IN, 1L);
            Mockito.when(cardUsageRepository.findTopByCardOrderByUsageIdDesc(card)).thenReturn(cardUsage);
            Mockito.when(cardRepository.save(card)).thenReturn(card);
            assertEquals( cardService.cardTransaction(request), BigDecimal.valueOf(27.00));
        }

        @DisplayName("Success scenario SwipeOut (2-1): Any two zones including zone 1 [3.00]")
        @Test
        void cardTransactionSuccessScenarioSix() {
            TransactionRequest request = TestUtils.buildTransactionRequest(CardSwipeType.OUT, 1L);
            Card card = TestUtils.buildCard(request.getCardId(), 26.8);
            Mockito.when(cardRepository.findById(request.getCardId())).thenReturn(Optional.of(card));
            CardUsage cardUsage = TestUtils.buildCardUsage(3.00, card, CardSwipeType.IN, 2L);
            Mockito.when(cardUsageRepository.findTopByCardOrderByUsageIdDesc(card)).thenReturn(cardUsage);
            Mockito.when(cardRepository.save(card)).thenReturn(card);
            assertEquals( cardService.cardTransaction(request), BigDecimal.valueOf(27.00));
        }

        @DisplayName("Success scenario SwipeOut (1-3): Any three zones including zone 1 [3.20]")
        @Test
        void cardTransactionSuccessScenarioFifth() {
            TransactionRequest request = TestUtils.buildTransactionRequest(CardSwipeType.OUT, 3L);
            Card card = TestUtils.buildCard(request.getCardId(), 26.8);
            Mockito.when(cardRepository.findById(request.getCardId())).thenReturn(Optional.of(card));
            CardUsage cardUsage = TestUtils.buildCardUsage(3.20, card, CardSwipeType.IN, 1L);
            Mockito.when(cardUsageRepository.findTopByCardOrderByUsageIdDesc(card)).thenReturn(cardUsage);
            Mockito.when(cardRepository.save(card)).thenReturn(card);
            assertEquals( cardService.cardTransaction(request), BigDecimal.valueOf(26.80));
        }

        @DisplayName("Success scenario SwipeOut (3-1): Any three zones including zone 1 [3.20]")
        @Test
        void cardTransactionSuccessScenarioSeven() {
            TransactionRequest request = TestUtils.buildTransactionRequest(CardSwipeType.OUT, 1L);
            Card card = TestUtils.buildCard(request.getCardId(), 26.8);
            Mockito.when(cardRepository.findById(request.getCardId())).thenReturn(Optional.of(card));
            CardUsage cardUsage = TestUtils.buildCardUsage(3.20, card, CardSwipeType.IN, 3L);
            Mockito.when(cardUsageRepository.findTopByCardOrderByUsageIdDesc(card)).thenReturn(cardUsage);
            Mockito.when(cardRepository.save(card)).thenReturn(card);
            assertEquals( cardService.cardTransaction(request), BigDecimal.valueOf(26.80));
        }

        @DisplayName("Success scenario SwipeOut: Any Bus Journey [1.80]")
        @Test
        void cardTransactionSuccessScenarioThird() {
            TransactionRequest request = TestUtils.buildTransactionRequest(CardSwipeType.OUT, 0L);
            Card card = TestUtils.buildCard(request.getCardId(), 28.2);
            Mockito.when(cardRepository.findById(request.getCardId())).thenReturn(Optional.of(card));
            CardUsage cardUsage = TestUtils.buildCardUsage(1.80, card, CardSwipeType.IN, 0L);
            Mockito.when(cardUsageRepository.findTopByCardOrderByUsageIdDesc(card)).thenReturn(cardUsage);
            Mockito.when(cardRepository.save(card)).thenReturn(card);
            assertEquals( cardService.cardTransaction(request), BigDecimal.valueOf(28.2));
        }

        @DisplayName("Success scenario SwipeOut(2-3): Any two zones excluding zone 1 [2.25]")
        @Test
        void cardTransactionSuccessScenarioFourth() {
            TransactionRequest request = TestUtils.buildTransactionRequest(CardSwipeType.OUT, 2L);
            Card card = TestUtils.buildCard(request.getCardId(), 26.8);
            Mockito.when(cardRepository.findById(request.getCardId())).thenReturn(Optional.of(card));
            CardUsage cardUsage = TestUtils.buildCardUsage(2.25, card, CardSwipeType.IN, 3L);
            Mockito.when(cardUsageRepository.findTopByCardOrderByUsageIdDesc(card)).thenReturn(cardUsage);
            Mockito.when(cardRepository.save(card)).thenReturn(card);
            assertEquals( cardService.cardTransaction(request), BigDecimal.valueOf(27.75));
        }

        @DisplayName("Success scenario SwipeOut(3-2): Any two zones excluding zone 1 [2.25]")
        @Test
        void cardTransactionSuccessScenarioEight() {
            TransactionRequest request = TestUtils.buildTransactionRequest(CardSwipeType.OUT, 3L);
            Card card = TestUtils.buildCard(request.getCardId(), 26.8);
            Mockito.when(cardRepository.findById(request.getCardId())).thenReturn(Optional.of(card));
            CardUsage cardUsage = TestUtils.buildCardUsage(2.25, card, CardSwipeType.IN, 2L);
            Mockito.when(cardUsageRepository.findTopByCardOrderByUsageIdDesc(card)).thenReturn(cardUsage);
            Mockito.when(cardRepository.save(card)).thenReturn(card);
            assertEquals( cardService.cardTransaction(request), BigDecimal.valueOf(27.75));
        }

        @DisplayName("Success scenario SwipeOut(3-3 OR 2-2): Any one zone outside zone 1 [2.00]")
        @Test
        void cardTransactionSuccessScenarioNine() {
            TransactionRequest request = TestUtils.buildTransactionRequest(CardSwipeType.OUT, 3L);
            Card card = TestUtils.buildCard(request.getCardId(), 26.8);
            Mockito.when(cardRepository.findById(request.getCardId())).thenReturn(Optional.of(card));
            CardUsage cardUsage = TestUtils.buildCardUsage(2.00, card, CardSwipeType.IN, 3L);
            Mockito.when(cardUsageRepository.findTopByCardOrderByUsageIdDesc(card)).thenReturn(cardUsage);
            Mockito.when(cardRepository.save(card)).thenReturn(card);
            assertEquals( cardService.cardTransaction(request), BigDecimal.valueOf(28.00));
        }
    }
}