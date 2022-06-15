package com.az.farecard.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class TransactionRequest implements Serializable {

    @NotNull(message = "please provide cardId")
    @Min(message = "Please provide valid cardId from 1, 2, 3, 4", value = 1)
    @Max(message = "Please provide valid cardId from 1, 2, 3, 4", value = 4)
    private Long cardId;

    // All bus journey not belong to Zone
    @NotNull(message = "please provide zoneId")
    @Min(message = "Please provide valid zoneId from 0, 1, 2, 3", value = 0)
    @Max(message = "Please provide valid zoneId from 0, 1, 2, 3", value = 3)
    private Long zoneId;

    @NotNull(message = "please provide swipeType (IN/OUT)")
    private CardSwipeType swipeType;
}
