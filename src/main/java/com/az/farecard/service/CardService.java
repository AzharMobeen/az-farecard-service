package com.az.farecard.service;


import com.az.farecard.dto.TransactionRequest;
import java.math.BigDecimal;

public interface CardService {
    BigDecimal cardTransaction(TransactionRequest request);

}
