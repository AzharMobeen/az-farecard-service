package com.az.farecard.repository;

import com.az.farecard.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CardRepository extends JpaRepository<Card, Long> {

}
