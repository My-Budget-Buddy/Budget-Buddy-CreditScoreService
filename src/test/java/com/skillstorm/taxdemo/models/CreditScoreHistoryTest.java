package com.skillstorm.taxdemo.models;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class CreditScoreHistoryTest {

    // Test the constructor with 4 arguments not covered by MeanBean
    @Test
    void testCreditScoreHistory() {

        // Arrange
        Long id = 1L;
        Long userId = 2L;
        int score = 700;
        LocalDateTime localDateTime = LocalDateTime.now();

        // Act
        CreditScoreHistory creditScoreHistory = new CreditScoreHistory(id, userId, score, localDateTime);

        // Assert
        assertEquals(id, creditScoreHistory.getId());
        assertEquals(userId, creditScoreHistory.getUserId());
        assertEquals(score, creditScoreHistory.getScore());
        assertEquals(localDateTime, creditScoreHistory.getTimestamp());
    }
}
