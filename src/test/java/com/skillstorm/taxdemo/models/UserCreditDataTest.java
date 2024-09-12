package com.skillstorm.taxdemo.models;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class UserCreditDataTest {

    UserCreditData userCreditData;

    // Test the constructor with 12 arguments not covered by MeanBean
    @Test
    void testUserCreditData() {

        // Arrange
        List<CreditAccount> creditAccounts = new ArrayList<>();
        creditAccounts.add(new CreditAccount(10L, "account", 50, 50, new UserCreditData()));

        Long id = 10L;
        Long userId = 11L;

        int onTimePayment = 50;
        int latePayments = 0;
        int missedPayments = 0;
        int publicRecords = 5;

        double creditUtilization = 40.00;
        double totalDebt = 30_000.00;

        int oldestAccountAge = 10;
        int recentInquiries = 0;
        int newAccounts = 3;

        // Act
        userCreditData = new UserCreditData(id, userId, onTimePayment, latePayments,
                missedPayments, publicRecords, creditUtilization, totalDebt,
                oldestAccountAge, creditAccounts, recentInquiries, newAccounts);
        // Assert
        assertEquals(id, userCreditData.getId());
        assertEquals(userId, userCreditData.getUserId());
        assertEquals(onTimePayment, userCreditData.getOnTimePayments());
        assertEquals(latePayments, userCreditData.getLatePayments());
        assertEquals(missedPayments, userCreditData.getMissedPayments());
        assertEquals(publicRecords, userCreditData.getPublicRecords());
        assertEquals((Double) creditUtilization, (Double) userCreditData.getCreditUtilization());
        assertEquals((Double) totalDebt, (Double) userCreditData.getTotalDebt());
        assertEquals(oldestAccountAge, userCreditData.getOldestAccountAge());
        assertEquals(creditAccounts, userCreditData.getCreditAccounts());
        assertEquals(recentInquiries, userCreditData.getRecentInquiries());
        assertEquals(newAccounts, userCreditData.getNewAccounts());

    }
}
