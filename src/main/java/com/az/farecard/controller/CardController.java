package com.az.farecard.controller;

import com.az.farecard.dto.TransactionRequest;
import com.az.farecard.exception.ErrorMessage;
import com.az.farecard.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.math.BigDecimal;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/cards")
public class CardController {

    private final CardService cardService;

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success", content = {@Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BigDecimal.class)
        )}),
        @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = {@Content(
                schema = @Schema(implementation = ErrorMessage.class)
        )}),
        @ApiResponse(responseCode = "404", description = "NOT FOUND", content = {@Content(
                schema = @Schema(implementation = ErrorMessage.class)
        )}),
        @ApiResponse(responseCode = "406", description = "NOT ACCEPTABLE", content = {@Content(
                schema = @Schema(implementation = ErrorMessage.class)
        )})
    })
    @Operation(summary = "Card SwipeIn/SwipeOut")
    @PostMapping
    public BigDecimal cardUsage(@Valid @RequestBody TransactionRequest request) {
        return cardService.cardTransaction(request);
    }
}