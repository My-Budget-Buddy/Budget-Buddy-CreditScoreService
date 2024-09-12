package com.skillstorm.taxdemo.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.skillstorm.taxdemo.models.CreditScoreHistory;
import com.skillstorm.taxdemo.models.UserCreditData;
import com.skillstorm.taxdemo.repositories.UserCreditDataRepository;
import com.skillstorm.taxdemo.services.CreditScoreService;

import java.util.List;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreditScoreControllerTest {

    @Mock
    private CreditScoreService creditScoreService;
    @Mock
    private UserCreditDataRepository userCreditDataRepository;
    @InjectMocks
    private CreditScoreController creditScoreController;

    @Test
    void testGetCreditScoreSuccess() {
        // Arrange
        int score = 700;
        when(creditScoreService.calculateFICOScore(anyLong())).thenReturn(score);
        // Act
        ResponseEntity<Integer> actual = creditScoreController.getCreditScore(1L);
        // Assert
        assertEquals((HttpStatus.OK), actual.getStatusCode());
        assertEquals(score, (int) actual.getBody());
    }

    @Test
    void testGetCreditScoreNotFoundForUser() {
        // Arrange
        when(creditScoreService.calculateFICOScore(anyLong())).thenThrow(RuntimeException.class);
        // Assert Act
        assertThrows(RuntimeException.class, () -> creditScoreController.getCreditScore(1L));
    }

    @Test
    void testSaveCreditData() {
        // Arrange
        UserCreditData userCreditData = new UserCreditData();
        when(userCreditDataRepository.save(any(UserCreditData.class))).thenReturn(userCreditData);
        // Act
        ResponseEntity<UserCreditData> actual = creditScoreController.saveCreditData(userCreditData);
        // Assert
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(userCreditData, actual.getBody());
    }

    @Test
    void testGetCreditScoreHistory() {
        // Arrange
        List<CreditScoreHistory> expected = new ArrayList<>();
        expected.add(new CreditScoreHistory());
        when(creditScoreService.getCreditScoreHistory(anyLong())).thenReturn(expected);
        // Act
        ResponseEntity<List<CreditScoreHistory>> actual = creditScoreController.getCreditScoreHistory(1L);
        // Assert
        assertEquals(HttpStatus.OK, actual.getStatusCode());
    }

    @Test
    void testUpdateUserCreditData() {

    }

    @Test
    void testGetCreditReport() {

    }

    @Test
    void testGetCreditImprovementTips() {

    }
}
