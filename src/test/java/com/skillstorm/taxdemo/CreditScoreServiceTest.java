// file: CreditScoreServiceTest.java

package com.skillstorm.taxdemo;

import com.skillstorm.taxdemo.models.CreditAccount;
import com.skillstorm.taxdemo.models.CreditScoreHistory;
import com.skillstorm.taxdemo.models.UserCreditData;
import com.skillstorm.taxdemo.repositories.CreditScoreHistoryRepository;
import com.skillstorm.taxdemo.repositories.UserCreditDataRepository;
import com.skillstorm.taxdemo.services.CreditScoreService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreditScoreServiceTest {

    @Mock
    private UserCreditDataRepository userCreditDataRepository;

    @Mock
    private CreditScoreHistoryRepository creditScoreHistoryRepository;

    @InjectMocks
    private CreditScoreService creditScoreService;

    private UserCreditData userCreditData;
    private CreditScoreHistory creditScoreHistory;

    @BeforeEach
    public void setUp() {
        List<CreditAccount> creditAccounts = new ArrayList<>();
        creditAccounts.add(new CreditAccount(1L, "credit card", 1000.0, 5000.0, null));
        creditAccounts.add(new CreditAccount(2L, "loan", 15000.0, 15000.0, null));

        userCreditData = new UserCreditData();
        userCreditData.setUserId(1L);
        userCreditData.setOnTimePayments(10);
        userCreditData.setLatePayments(2);
        userCreditData.setMissedPayments(1);
        userCreditData.setPublicRecords(0);
        userCreditData.setCreditUtilization(25);
        userCreditData.setTotalDebt(30000);
        userCreditData.setOldestAccountAge(60);
        userCreditData.setRecentInquiries(1);
        userCreditData.setNewAccounts(1);
        userCreditData.setCreditAccounts(creditAccounts);

        creditScoreHistory = new CreditScoreHistory();
        creditScoreHistory.setUserId(1L);
        creditScoreHistory.setScore(750);
        creditScoreHistory.setTimestamp(LocalDateTime.now());
    }

    @Test
    public void testCalculateFICOScore() {
        when(userCreditDataRepository.findByUserId(1L)).thenReturn(Optional.of(userCreditData));
        when(creditScoreHistoryRepository.save(any(CreditScoreHistory.class))).thenReturn(creditScoreHistory);

        int score = creditScoreService.calculateFICOScore(1L);

        assertEquals(714, score);
        verify(creditScoreHistoryRepository, times(1)).save(any(CreditScoreHistory.class));
    }

    
}
