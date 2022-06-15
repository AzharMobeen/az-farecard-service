package com.az.farecard.util;

import com.az.farecard.dto.CardSwipeType;
import com.az.farecard.dto.TransactionRequest;
import com.az.farecard.dto.TransactionType;
import com.az.farecard.entity.Card;
import com.az.farecard.entity.CardUsage;

import java.math.BigDecimal;

public class TestUtils {
    public static TransactionRequest buildTransactionRequest(CardSwipeType swipeType, Long zoneId) {
        TransactionRequest request = new TransactionRequest();
        request.setCardId(1L);
        request.setZoneId(zoneId);
        request.setSwipeType(swipeType);
        return request;
    }

    public static Card buildCard(Long cardId, double balance) {
        Card card = new Card();
        card.setCardId(cardId);
        card.setBalance(BigDecimal.valueOf(balance));

        return card;
    }

    public static CardUsage buildCardUsage(double fare, Card card, CardSwipeType swipeType, long zoneId) {
        CardUsage cardUsage = new CardUsage();
        cardUsage.setCard(card);
        cardUsage.setFare(BigDecimal.valueOf(fare));
        cardUsage.setUsageId(1L);
        cardUsage.setSwipe(swipeType);
        cardUsage.setType(TransactionType.ADVANCE);
        cardUsage.setStation(zoneId);
        return cardUsage;
    }
}
