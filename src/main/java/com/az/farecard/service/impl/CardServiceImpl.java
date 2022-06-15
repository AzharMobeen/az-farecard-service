package com.az.farecard.service.impl;

import com.az.farecard.dto.CardSwipeType;
import com.az.farecard.dto.TransactionRequest;
import com.az.farecard.dto.TransactionType;
import com.az.farecard.entity.Card;
import com.az.farecard.entity.CardUsage;
import com.az.farecard.exception.CustomRuntimeException;
import com.az.farecard.repository.CardRepository;
import com.az.farecard.repository.CardUsageRepository;
import com.az.farecard.service.CardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final CardUsageRepository cardUsageRepository;
    @Value("${maxPossibleFare:3.20}")
    private double maxPossibleFare;

    // 1) Validated card balance
    // 2) Identify card transaction for SwipeIn/SwipeOut
    // 3) Identify its metro or bus journey and proceed with business logic
    // 4) Complete business logic for SwipeIn/SwipeOut.
    //      => If it's SwipeIn then simply deduct max fare fee and update card balance & create transaction
    //      => If it's SwipeOut then calculate journey fare and then update card balance & create transaction

    @Transactional
    @Override
    public BigDecimal cardTransaction(TransactionRequest request) {

        Card card = fetchCard(request.getCardId());
        if(request.getSwipeType().equals(CardSwipeType.IN)) {
            handleCardSwipeIn(request, card);
        } else {
            handleCardSwipeOut(request, card);
        }
        card = cardRepository.save(card);
        return card.getBalance();
    }

    private void handleCardSwipeOut(TransactionRequest request, Card card) {
        CardUsage usage = fetchCardSwipeInUsage(card);
        calculateJourneyFare(card, usage, request);
    }

    private void handleCardSwipeIn(TransactionRequest request, Card card) {
        validateCardBalance(card);
        CardUsage cardUsage = new CardUsage();
        if(request.getZoneId() == 0){
            // For bus journey max fare should be 1.80
            cardUsage.setFare(BigDecimal.valueOf(1.80));
            card.setBalance(card.getBalance().subtract(BigDecimal.valueOf(1.80)));
        } else {
            cardUsage.setFare(BigDecimal.valueOf(maxPossibleFare));
            card.setBalance(card.getBalance().subtract(BigDecimal.valueOf(maxPossibleFare)));
        }
        cardUsage.setType(TransactionType.ADVANCE);
        cardUsage.setSwipe(CardSwipeType.IN);
        cardUsage.setStation(request.getZoneId());
        card.addCardUsage(cardUsage);
    }

    // TODO: We should use Drools to calculate journey fare.
    private void calculateJourneyFare(Card card, CardUsage usage, TransactionRequest request) {
        if(request.getZoneId() == 0) {
            calculateFareForBusJourney(card, request);
            return;
        }
        if(request.getZoneId().equals(1L) || usage.getStation().equals(1L)) {
            // Anywhere in zone 1
            // Any two zone including zone 1
            // Any three zones
            calculateFareForZone1(card, usage, request);
        } else {
            // Anyone Zone outside zone 1
            // Any two zone excluding zone 1
            calculateFareForOthers(card, usage, request);
        }
    }

    private void calculateFareForBusJourney(Card card, TransactionRequest request) {
        // Anyone Bus journey
        updateCardAndCardUsage(card, 1.80, request);
    }

    private void calculateFareForOthers(Card card, CardUsage usage, TransactionRequest request) {
        if(Objects.equals(request.getZoneId(), usage.getStation())){
            // it can zone 2=2 OR 3=3
            // Anyone Zone outside zone 1
            updateCardAndCardUsage(card, 2.00, request);
        } else {
            // it can be 3 or 2 but outside 1
            // Any two zone excluding zone 1
            updateCardAndCardUsage(card, 2.25, request);
        }
    }

    private void calculateFareForZone1(Card card, CardUsage usage, TransactionRequest request) {
        if(usage.getStation().equals(request.getZoneId())) {
            // Anywhere in zone 1
            updateCardAndCardUsage(card, 2.50, request);
        } else if(request.getZoneId().equals(2L) || usage.getStation().equals(2L)){
            // Any two zone including zone 1
            updateCardAndCardUsage(card, 3.00, request);
        } else {
            // Any three zone including zone 1
            updateCardAndCardUsage(card, 3.20, request);
        }
    }

    private void updateCardAndCardUsage(Card card, double val, TransactionRequest request) {
        if(request.getZoneId() == 0) {
            // For bus journey max fare should be 1.80
            card.setBalance(card.getBalance().add(BigDecimal.valueOf(1.80)).subtract(BigDecimal.valueOf(val)));
        } else {
            card.setBalance(card.getBalance().add(BigDecimal.valueOf(maxPossibleFare))
                    .subtract(BigDecimal.valueOf(val)));
        }
        CardUsage newUsage = new CardUsage();
        newUsage.setType(TransactionType.ACTUAL);
        newUsage.setFare(BigDecimal.valueOf(val));
        newUsage.setStation(request.getZoneId());
        card.addCardUsage(newUsage);
    }

    private CardUsage fetchCardSwipeInUsage(Card card) {
        CardUsage cardUsage = cardUsageRepository.findTopByCardOrderByUsageIdDesc(card);
        if(cardUsage == null || cardUsage.getSwipe().equals(CardSwipeType.OUT))
            throw new CustomRuntimeException("Invalid Request", "Car doesn't swipe in", HttpStatus.NOT_ACCEPTABLE);
        return cardUsage;
    }

    private void validateCardBalance(Card card) {
        if(BigDecimal.valueOf(maxPossibleFare).compareTo(card.getBalance()) > 0) {
            throw new CustomRuntimeException("Out of balance", "Customer card is out of balance",
                    HttpStatus.NOT_ACCEPTABLE);
        }
    }

    private Card fetchCard(long cardId) {
        Optional<Card> requestedCard = cardRepository.findById(cardId);
        return requestedCard.orElseThrow(() -> new CustomRuntimeException("Card not exist",
                "Provided cardId not exist", HttpStatus.NOT_FOUND));
    }
}