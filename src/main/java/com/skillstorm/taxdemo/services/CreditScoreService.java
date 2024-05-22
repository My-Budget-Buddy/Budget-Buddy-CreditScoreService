// file: CreditScoreService.java

package com.skillstorm.taxdemo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skillstorm.taxdemo.models.CreditAccount;
import com.skillstorm.taxdemo.models.CreditScoreHistory;
import com.skillstorm.taxdemo.models.UserCreditData;
import com.skillstorm.taxdemo.repositories.CreditScoreHistoryRepository;
import com.skillstorm.taxdemo.repositories.UserCreditDataRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class CreditScoreService {

    private static final Logger logger = Logger.getLogger(CreditScoreService.class.getName());

    @Autowired
    private UserCreditDataRepository repository;

    @Autowired
    private CreditScoreHistoryRepository creditScoreHistoryRepository;

    public int calculateFICOScore(Long userId) {
        Optional<UserCreditData> optionalCreditData = repository.findByUserId(userId);
        UserCreditData creditData = optionalCreditData
                .orElseThrow(() -> new RuntimeException("Credit data not found for user with ID: " + userId));

        int score = 0;

        // Payment History (35%)
        score += calculatePaymentHistoryScore(creditData.getOnTimePayments(), 
                                              creditData.getLatePayments(), 
                                              creditData.getMissedPayments(),
                                              creditData.getPublicRecords());

        // Amounts Owed (30%)
        score += calculateAmountsOwedScore(creditData.getCreditUtilization(), creditData.getTotalDebt());

        // Length of Credit History (15%)
        score += calculateCreditHistoryLengthScore(creditData.getOldestAccountAge());

        // Credit Mix (10%)
        score += calculateCreditMixScore(creditData.getCreditAccounts());

        // New Credit (10%)
        score += calculateNewCreditScore(creditData.getRecentInquiries(), creditData.getNewAccounts());

        // Ensure the score falls within the FICO range
        score = Math.min(850, Math.max(300, score));

        // Save history
        CreditScoreHistory history = new CreditScoreHistory();
        history.setUserId(userId);
        history.setScore(score);
        history.setTimestamp(LocalDateTime.now());
        creditScoreHistoryRepository.save(history);

        return score;
    }

    public List<CreditScoreHistory> getCreditScoreHistory(Long userId) {
        return creditScoreHistoryRepository.findByUserIdOrderByTimestampDesc(userId);
    }

    private int calculatePaymentHistoryScore(int onTimePayments, int latePayments, int missedPayments, int publicRecords) {
        int totalPayments = onTimePayments + latePayments + missedPayments;
        if (totalPayments == 0) return 0;

        double onTimePercentage = (double) onTimePayments / totalPayments;
        int score = (int) (350 * onTimePercentage); // Base score based on percentage

        // Adjust score based on severity and number of negative marks
        score -= 50 * missedPayments;
        score -= 25 * latePayments;
        score -= 100 * publicRecords;

        return Math.max(0, score); // Ensure score doesn't go below 0
    }

    private int calculateAmountsOwedScore(double creditUtilization, double totalDebt) {
        int score = 300;

        // Penalize for high credit utilization
        if (creditUtilization > 30) {
            score -= (creditUtilization - 30) * 5; // 5 points lost for each percentage point over 30%
        }

        // Penalize for high total debt
        if (totalDebt > 50000) {
            score -= (totalDebt - 50000) / 1000; // 1 point lost for every $1000 over $50,000
        }

        return Math.max(0, score);
    }

    private int calculateCreditHistoryLengthScore(int oldestAccountAge) {
        return Math.min(150, oldestAccountAge); // Max 150 points for accounts over 12.5 years old
    }

    private int calculateCreditMixScore(List<CreditAccount> creditAccounts) {
        // This is a simplified version, you could expand on this
        boolean hasCreditCards = false;
        boolean hasLoans = false;

        for (CreditAccount account : creditAccounts) {
            if (account.getAccountType().equalsIgnoreCase("credit card")) {
                hasCreditCards = true;
            } else if (account.getAccountType().equalsIgnoreCase("loan")) {
                hasLoans = true;
            }
        }

        return (hasCreditCards && hasLoans) ? 100 : 50; // 100 points for having both, 50 for one type
    }

    private int calculateNewCreditScore(int recentInquiries, int newAccounts) {
        int score = 100;

        // Penalize for recent inquiries
        score -= 5 * recentInquiries;

        // Penalize for new accounts
        score -= 10 * newAccounts;

        return Math.max(0, score);
    }

    @Transactional
    public String generateCreditReport(Long userId) {
        Optional<UserCreditData> optionalCreditData = repository.findByUserId(userId);
        if (!optionalCreditData.isPresent()) {
            throw new RuntimeException("User Credit Data not found for userId: " + userId);
        }

        UserCreditData creditData = optionalCreditData.get();
        List<CreditScoreHistory> creditScoreHistory = getCreditScoreHistory(userId);

        logger.info("Credit score history count for userId " + userId + ": " + creditScoreHistory.size());

        StringBuilder report = new StringBuilder();
        report.append("Credit Report for User ID: ").append(userId).append("\n\n");

        report.append("On-Time Payments: ").append(creditData.getOnTimePayments()).append("\n");
        report.append("Late Payments: ").append(creditData.getLatePayments()).append("\n");
        report.append("Missed Payments: ").append(creditData.getMissedPayments()).append("\n");
        report.append("Public Records: ").append(creditData.getPublicRecords()).append("\n");
        report.append("Credit Utilization: ").append(creditData.getCreditUtilization()).append("%\n");
        report.append("Total Debt: $").append(creditData.getTotalDebt()).append("\n");
        report.append("Oldest Account Age: ").append(creditData.getOldestAccountAge()).append(" months\n");
        report.append("Recent Inquiries: ").append(creditData.getRecentInquiries()).append("\n");
        report.append("New Accounts: ").append(creditData.getNewAccounts()).append("\n\n");

        report.append("Credit Accounts:\n");
        for (CreditAccount account : creditData.getCreditAccounts()) {
            report.append("  - Account Type: ").append(account.getAccountType()).append("\n");
            report.append("    Balance: $").append(account.getBalance()).append("\n");
            report.append("    Credit Limit: $").append(account.getCreditLimit()).append("\n");
        }

        report.append("\nCredit Score History:\n");
        for (CreditScoreHistory history : creditScoreHistory) {
            report.append("  - Score: ").append(history.getScore()).append(", Date: ").append(history.getTimestamp()).append("\n");
        }

        // Fetch and add latest credit score to the report
        Optional<CreditScoreHistory> latestScore = creditScoreHistoryRepository.findTopByUserIdOrderByTimestampDesc(userId);
        if (latestScore.isPresent()) {
            CreditScoreHistory latest = latestScore.get();
            logger.info("Latest credit score for userId " + userId + ": " + latest.getScore());
            report.append("\nLatest Credit Score:\n");
            report.append("  - Score: ").append(latest.getScore()).append(", Date: ").append(latest.getTimestamp()).append("\n");
        } else {
            report.append("\nNo Credit Score History Available.\n");
        }

        return report.toString();
    }
}
