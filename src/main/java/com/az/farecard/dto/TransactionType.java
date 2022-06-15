package com.az.farecard.dto;

import lombok.Getter;

public enum TransactionType {

    ADVANCE("Advance"),
    ACTUAL("Actual");
    @Getter
    private String type;
    TransactionType(String type) {
        this.type = type;
    }
}