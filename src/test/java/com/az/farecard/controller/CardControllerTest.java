package com.az.farecard.controller;

import com.az.farecard.dto.CardSwipeType;
import com.az.farecard.dto.TransactionRequest;
import com.az.farecard.service.impl.CardServiceImpl;
import com.az.farecard.util.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class CardControllerTest {

    @InjectMocks
    private CardController cardController;

    @Mock
    private CardServiceImpl cardService;

    @BeforeEach
    void beforeEach() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addItemInToDoList() {
        TransactionRequest request = TestUtils.buildTransactionRequest(CardSwipeType.IN, 0L);
        assertDoesNotThrow(() -> cardController.cardUsage(request));
    }
}