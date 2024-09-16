package com.skillstorm.taxdemo.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.skillstorm.taxdemo.models.CreditAccount;
import com.skillstorm.taxdemo.models.CreditScoreHistory;
import com.skillstorm.taxdemo.models.UserCreditData;
import com.skillstorm.taxdemo.repositories.CreditScoreHistoryRepository;
import com.skillstorm.taxdemo.repositories.UserCreditDataRepository;

/* ---------------------------------------------------------------------------|
     *                                                                        |
     * The service class has 4 private methods that will not be tested until  |
     * their access modified is changed to public.                            |
     * Some of the main service methods appear incomplete based on comments   |
     * and hard coded values that should be parameterized in the methods.     |
     * If a method appears incomplete I will add a comment to explain below.  |
     *                                                                        |
-----------------------------------------------------------------------------*/
@ExtendWith(MockitoExtension.class)
public class CreditScoreServiceTests {

    @Mock
    UserCreditData userCreditData;
    @Mock
    private UserCreditDataRepository userCreditDataRepository;
    @Mock
    private CreditScoreHistoryRepository creditScoreHistoryRepository;
    @Mock
    private CreditAccount creditAccount;
    @InjectMocks
    private CreditScoreService creditScoreService;

    /*
     * This test is incomplete b/c on the main/taxdemo/services folder the actual
     * service method has to be updates and completed.
     * As an example a method named "calculateAmountOwedScore", which is responsible
     * for calculating 30% of the FICO score is currently commented
     * out in the main service class and it's implementation is also commentted out
     * implying that the method is incomplete.
     */
    @Test
    void testCalculateFICOScore() {
        // todo - must implement only after dev team updates main service class
    }

    /*
     * Test that the calculateFICOScore method throws a RuntimeException when the
     * userCreditDataRepository.findByUserId method returns an empty Optional
     */
    @Test
    void testCalculateFICOScoreThrowsRuntimeException() {
        // Arrange
        Long userId = 1L;
        Optional<UserCreditData> optionalCreditData = Optional.empty();
        when(userCreditDataRepository.findByUserId(anyLong())).thenReturn(optionalCreditData);
        // Act & Assert
        Exception actual = assertThrows(RuntimeException.class, () -> {
            creditScoreService.calculateFICOScore(userId);
        });
        assert (actual.getMessage().contains("Credit data not found for user with ID: " + userId));
    }

    @Test
    void testGenerateCreditReport() {

        // Arrange
        Long userId = 1L;
        Optional<UserCreditData> optionalCreditData = Optional.of(userCreditData);

        // All userCreditData data to be used with stubs
        int onTimePayments = 100;
        int latePayments = 0;
        int missedPayments = 0;
        int publicRecords = 2;
        double creditUtilization = 25.00;
        double totalDebt = 10_000.00;
        int oldestAccountAge = 10; 
        int recentInquiries = 0; 
        int newAccounts = 0;

        // All userCreditData stubs
        when(userCreditData.getOnTimePayments()).thenReturn(onTimePayments);
        when(userCreditData.getLatePayments()).thenReturn(latePayments);
        when(userCreditData.getMissedPayments()).thenReturn(missedPayments);
        when(userCreditData.getPublicRecords()).thenReturn(publicRecords);
        when(userCreditData.getCreditUtilization()).thenReturn(creditUtilization);
        when(userCreditData.getTotalDebt()).thenReturn(totalDebt);
        when(userCreditData.getOldestAccountAge()).thenReturn(oldestAccountAge);
        when(userCreditData.getRecentInquiries()).thenReturn(recentInquiries);
        when(userCreditData.getNewAccounts()).thenReturn(newAccounts);
        when(userCreditData.getCreditAccounts()).thenReturn(Arrays.asList(creditAccount));

        // Stub for credit repository 
        when(userCreditDataRepository.findByUserId(anyLong())).thenReturn(optionalCreditData);
        // All CreditAccount data to be used with stubs
        String accountType = "Credit Card";
        double balance = 10_000.00;
        double creditLimit = 20_000.00;
        // All CreditAccount stubs
        when(creditAccount.getAccountType()).thenReturn(accountType);
        when(creditAccount.getBalance()).thenReturn(balance);
        when(creditAccount.getCreditLimit()).thenReturn(creditLimit);
        

        // ---------------------------------------------------------------
        List<CreditScoreHistory> creditScoreHistory = new ArrayList<>();
        creditScoreHistory.add(new CreditScoreHistory(1L, 1L, 700, LocalDateTime.now()));

        //----------------------------------------------------------------
        when(creditScoreHistoryRepository.findByUserIdOrderByTimestampDesc(anyLong())).thenReturn(creditScoreHistory);
        
        // Act
        String actual = creditScoreService.generateCreditReport(userId);

        // Assert
        assertTrue(actual.contains("On-Time Payments: " + onTimePayments));
        assertTrue(actual.contains("Late Payments: " + latePayments));
        assertTrue(actual.contains("Missed Payments: " + missedPayments));
        assertTrue(actual.contains("Public Records: " + publicRecords));
        assertTrue(actual.contains("Credit Utilization: " + creditUtilization));
        assertTrue(actual.contains("Total Debt: $" + totalDebt));
        assertTrue(actual.contains("Oldest Account Age: " + oldestAccountAge));
        assertTrue(actual.contains("Recent Inquiries: " + recentInquiries));
        assertTrue(actual.contains("New Accounts: " + newAccounts));

        assertTrue(actual.contains("  - Account Type: " + accountType));
        assertTrue(actual.contains("    Balance: $" + balance));
        assertTrue(actual.contains("    Credit Limit: $" + creditLimit));
    }

    @Test
    void testGenerateCreditReportThrowsRuntimeException() {
        // Arrange
        Long userId = 1L;
        Optional<UserCreditData> optionalCreditData = Optional.empty();
        when(userCreditDataRepository.findByUserId(anyLong())).thenReturn(optionalCreditData);
        // Act & Assert
        Exception actual = assertThrows(RuntimeException.class, () -> {
            creditScoreService.generateCreditReport(userId);
        });
        assert (actual.getMessage().contains("User Credit Data not found for userId: " + userId));
    }

    /*
     * Test that the getCreditScoreHistory method returns the expected list of
     * CreditScoreHistory objects with valid user ID
     */
    @Test
    void testGetCreditScoreHistory() {
        // Arrange
        List<CreditScoreHistory> userCreditData = new ArrayList<>();
        when(creditScoreHistoryRepository.findByUserIdOrderByTimestampDesc(anyLong())).thenReturn(userCreditData);
        // Act
        List<CreditScoreHistory> actual = creditScoreService.getCreditScoreHistory(1L);
        // Assert
        assertEquals(userCreditData, actual);
    }

    /*
     * Test that the getCreditScoreHistory method returns null when the user ID is
     * invalid
     */
    @Test
    void testGetCreditScoreHistoryWithNull() {
        // Arrange
        List<CreditScoreHistory> userCreditData = null;
        when(creditScoreHistoryRepository.findByUserIdOrderByTimestampDesc(anyLong())).thenReturn(userCreditData);
        // Act
        List<CreditScoreHistory> actual = creditScoreService.getCreditScoreHistory(1L);
        // Assert
        assertEquals(userCreditData, actual);
    }

    /*
     * Test that the we get the tip for high credit utilization
     */
    @Test
    void testGetCreditImprovementTipsOnlyCreditUtilizationTip() {
        // Arrange
        when(userCreditData.getCreditUtilization()).thenReturn(31.0); // Triggers tip
        when(userCreditData.getMissedPayments()).thenReturn(0);
        when(userCreditData.getTotalDebt()).thenReturn(0.0);
        when(userCreditData.getRecentInquiries()).thenReturn(0);
        when(userCreditData.getNewAccounts()).thenReturn(0);
        when(userCreditDataRepository.findByUserId(1L)).thenReturn(Optional.of(userCreditData));
        // Act
        List<String> tips = creditScoreService.getCreditImprovementTips(1L);
        // Assert
        assertEquals(tips.get(0), "Keep your credit utilization below 30% to improve your score.");
    }

    /*
     * Test that the we get the tip for missed payments
     */
    @Test
    void testGetCreditImprovementTipsOnlyMissedPaymentTip() {
        // Arrange
        when(userCreditData.getCreditUtilization()).thenReturn(0.0);
        when(userCreditData.getMissedPayments()).thenReturn(1); // Triggers tip
        when(userCreditData.getTotalDebt()).thenReturn(0.0);
        when(userCreditData.getRecentInquiries()).thenReturn(0);
        when(userCreditData.getNewAccounts()).thenReturn(0);
        when(userCreditDataRepository.findByUserId(1L)).thenReturn(Optional.of(userCreditData));
        // Act
        List<String> tips = creditScoreService.getCreditImprovementTips(1L);
        // Assert
        assertEquals(tips.get(0), "Make all your payments on time to avoid negative marks on your credit report.");
    }

    /*
     * Test that the we get the tip for high total debt
     */
    @Test
    void testGetCreditImprovementTipsOnlyTotalDebtTip() {
        // Arrange
        when(userCreditData.getCreditUtilization()).thenReturn(0.0);
        when(userCreditData.getMissedPayments()).thenReturn(0);
        when(userCreditData.getTotalDebt()).thenReturn(50001.0); // Triggers tip
        when(userCreditData.getRecentInquiries()).thenReturn(0);
        when(userCreditData.getNewAccounts()).thenReturn(0);
        when(userCreditDataRepository.findByUserId(1L)).thenReturn(Optional.of(userCreditData));
        // Act
        List<String> tips = creditScoreService.getCreditImprovementTips(1L);
        // Assert
        assertEquals(tips.get(0), "Consider paying down your debt to reduce your total debt and improve your score.");
    }

    /*
     * Test that the we get the tip for high recent inquiries
     */
    @Test
    void testGetCreditImprovementTipsOnlyRecentInquiriesTip() {
        // Arrange
        when(userCreditData.getCreditUtilization()).thenReturn(0.0);
        when(userCreditData.getMissedPayments()).thenReturn(0);
        when(userCreditData.getTotalDebt()).thenReturn(0.0);
        when(userCreditData.getRecentInquiries()).thenReturn(3); // Triggers tip
        when(userCreditData.getNewAccounts()).thenReturn(0);
        when(userCreditDataRepository.findByUserId(1L)).thenReturn(Optional.of(userCreditData));
        // Act
        List<String> tips = creditScoreService.getCreditImprovementTips(1L);
        // Assert
        assertEquals(tips.get(0), "Limit the number of new credit inquiries to improve your score.");
    }

    /*
     * Test that the we get the tip for high new accounts
     */
    @Test
    void testGetCreditImprovementTipsOnlyNewAccountTip() {
        // Arrange
        when(userCreditData.getCreditUtilization()).thenReturn(0.0);
        when(userCreditData.getMissedPayments()).thenReturn(0);
        when(userCreditData.getTotalDebt()).thenReturn(0.0);
        when(userCreditData.getRecentInquiries()).thenReturn(0);
        when(userCreditData.getNewAccounts()).thenReturn(3); // Triggers tip
        when(userCreditDataRepository.findByUserId(1L)).thenReturn(Optional.of(userCreditData));
        // Act
        List<String> tips = creditScoreService.getCreditImprovementTips(1L);
        // Assert
        assertEquals(tips.get(0), "Avoid opening too many new accounts in a short period to improve your score.");
    }

    @Test
    void testGetCreditImprovementTipsLooksGoodTip() {
        // Arrange
        when(userCreditData.getCreditUtilization()).thenReturn(0.0);
        when(userCreditData.getMissedPayments()).thenReturn(0);
        when(userCreditData.getTotalDebt()).thenReturn(0.0);
        when(userCreditData.getRecentInquiries()).thenReturn(0);
        when(userCreditData.getNewAccounts()).thenReturn(0);
        when(userCreditDataRepository.findByUserId(1L)).thenReturn(Optional.of(userCreditData));
        // Act
        List<String> tips = creditScoreService.getCreditImprovementTips(1L);
        // Assert
        assertEquals(tips.get(0), "Your credit profile looks good! Keep up the good work.");
    }

    /*
     * Test that the getCreditImprovementTips method returns RuntimeException with
     * invalid user ID
     */
    @Test
    void testGetCreditImprovementTipsWithNull() {
        // Arrange
        Long userId = 1L;
        Optional<UserCreditData> optionalCreditData = Optional.empty();
        when(userCreditDataRepository.findByUserId(anyLong())).thenReturn(optionalCreditData);
        // Act
        Exception actual = assertThrows(RuntimeException.class,
                () -> creditScoreService.getCreditImprovementTips(userId));
        // Assert
        assert (actual.getMessage().contains("User Credit Data not found for userId: " + userId));
    }
}
