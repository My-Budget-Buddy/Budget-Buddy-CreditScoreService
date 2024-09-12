package com.skillstorm.taxdemo.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.skillstorm.taxdemo.models.CreditAccount;
import com.skillstorm.taxdemo.models.CreditScoreHistory;
import com.skillstorm.taxdemo.models.UserCreditData;
import com.skillstorm.taxdemo.repositories.UserCreditDataRepository;
import com.skillstorm.taxdemo.services.CreditScoreService;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.*;

/* ---------------------------------------------------------------------------|
     *                                                                        |
     * The method updateCreditAccount is currently omitted from testing       |
     * because attempting to test a private method,                           |
     * is not considered good practice. Please consider testing if dev team   |
     * changes access modifier to public                                      |
     *                                                                        |
-----------------------------------------------------------------------------*/
@ExtendWith(MockitoExtension.class)
public class CreditScoreControllerTest {

    @Mock
    private CreditScoreService creditScoreService;
    @Mock
    private UserCreditDataRepository userCreditDataRepository;
    @Mock
    private UserCreditData userCreditDataMock;
    @Mock
    private List<CreditAccount> existingAccountsMock;
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
        assertEquals(expected, actual.getBody());
    }

    @Test
    void testUpdateUserCreditData() {
        // Arrange
        Optional<UserCreditData> optionalExistingData = Optional.of(new UserCreditData(1L, 1L, 0, 0,
                0, 0, 0,
                0, 0, existingAccountsMock,
                0, 0));
        UserCreditData expected = optionalExistingData.get();
        when(userCreditDataRepository.findByUserId(anyLong())).thenReturn(optionalExistingData);
        when(userCreditDataRepository.save(any(UserCreditData.class))).thenReturn(expected);
        // Act
        ResponseEntity<UserCreditData> actual = creditScoreController.updateUserCreditData(1L, userCreditDataMock);
        // Assert
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(expected, actual.getBody());
    }

    @Test
    void testUpdateUserCreditDataFailWithRunTimeException() {
        // Arrange
        Optional<UserCreditData> optionalExistingData = Optional.ofNullable(null);
        when(userCreditDataRepository.findByUserId(anyLong())).thenReturn(optionalExistingData);
        // Assert Act
        assertThrows(RuntimeException.class,
                () -> creditScoreController.updateUserCreditData(10L, new UserCreditData()));
    }

    @Test
    void testGetCreditReport() {
        // Arrange
        String creditReport = "Credit Report";
        when(creditScoreService.generateCreditReport(anyLong())).thenReturn(creditReport);
        // Act
        ResponseEntity<String> actual = creditScoreController.getCreditReport(1L);
        // Assert
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(creditReport, actual.getBody());
    }

    @Test
    void testGetCreditImprovementTips() {
        // Arrange
        List<String> tips = new ArrayList<>();
        tips.add("Keep your credit utilization below 30% to improve your score.");
        when(creditScoreService.getCreditImprovementTips(anyLong())).thenReturn(tips);
        // Act
        ResponseEntity<List<String>> actual = creditScoreController.getCreditImprovementTips(1L);
        // Assert
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(tips, actual.getBody());
    }
}
