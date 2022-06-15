package com.az.farecard.dto;

import lombok.Getter;

public enum CardSwipeType {

    IN("IN"),
    OUT("OUT");
    @Getter
    private String type;
    CardSwipeType(String type) {
        this.type = type;
    }
}