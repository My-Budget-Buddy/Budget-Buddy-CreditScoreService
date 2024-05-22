package com.skillstorm.taxdemo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skillstorm.taxdemo.models.CreditAccount;
import com.skillstorm.taxdemo.models.CreditScoreHistory;
import com.skillstorm.taxdemo.models.UserCreditData;
import com.skillstorm.taxdemo.repositories.CreditScoreHistoryRepository;
import com.skillstorm.taxdemo.repositories.UserCreditDataRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CreditScoreService {

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

}