package com.az.farecard.repository;


import com.az.farecard.entity.Card;
import com.az.farecard.entity.CardUsage;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CardUsageRepository extends JpaRepository<CardUsage, Long> {

    // It will bring always last swipeIn card usage
    CardUsage findTopByCardOrderByUsageIdDesc(Card card);
}
