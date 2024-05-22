// file: CreditScoreController.java

package com.skillstorm.taxdemo.controllers;

import com.skillstorm.taxdemo.models.CreditScoreHistory;
import com.skillstorm.taxdemo.models.UserCreditData;
import com.skillstorm.taxdemo.models.CreditAccount;
import com.skillstorm.taxdemo.repositories.UserCreditDataRepository;
import com.skillstorm.taxdemo.services.CreditScoreService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/credit")
public class CreditScoreController {

    @Autowired
    private CreditScoreService creditScoreService;

    @Autowired
    private UserCreditDataRepository userCreditDataRepository;

    @GetMapping("/score/{userId}")
    public ResponseEntity<Integer> getCreditScore(@PathVariable Long userId) {
        int score = creditScoreService.calculateFICOScore(userId);
        return ResponseEntity.ok(score);
    }

    @PostMapping("/data")
    public ResponseEntity<UserCreditData> saveCreditData(@RequestBody UserCreditData creditData) {
        UserCreditData savedData = userCreditDataRepository.save(creditData);
        return ResponseEntity.ok(savedData);
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<List<CreditScoreHistory>> getCreditScoreHistory(@PathVariable Long userId) {
        List<CreditScoreHistory> history = creditScoreService.getCreditScoreHistory(userId);
        return ResponseEntity.ok(history);
    }

    @PutMapping("/data/{userId}")
    public ResponseEntity<UserCreditData> updateUserCreditData(@PathVariable Long userId, @RequestBody UserCreditData updatedCreditData) {
        Optional<UserCreditData> optionalExistingData = userCreditDataRepository.findByUserId(userId);
        if (!optionalExistingData.isPresent()) {
            throw new RuntimeException("User Credit Data not found for userId: " + userId);
        }

        UserCreditData existingData = optionalExistingData.get();

        // Update fields individually
        existingData.setOnTimePayments(updatedCreditData.getOnTimePayments());
        existingData.setLatePayments(updatedCreditData.getLatePayments());
        existingData.setMissedPayments(updatedCreditData.getMissedPayments());
        existingData.setPublicRecords(updatedCreditData.getPublicRecords());
        existingData.setCreditUtilization(updatedCreditData.getCreditUtilization());
        existingData.setTotalDebt(updatedCreditData.getTotalDebt());
        existingData.setOldestAccountAge(updatedCreditData.getOldestAccountAge());
        existingData.setRecentInquiries(updatedCreditData.getRecentInquiries());
        existingData.setNewAccounts(updatedCreditData.getNewAccounts());

        // Handle credit accounts collection
        updateCreditAccounts(existingData.getCreditAccounts(), updatedCreditData.getCreditAccounts());

        UserCreditData savedData = userCreditDataRepository.save(existingData);
        return ResponseEntity.ok(savedData);
    }

    private void updateCreditAccounts(List<CreditAccount> existingAccounts, List<CreditAccount> updatedAccounts) {
        existingAccounts.clear();
        existingAccounts.addAll(updatedAccounts);
    }

    @GetMapping("/report/{userId}")
    public ResponseEntity<String> getCreditReport(@PathVariable Long userId) {
        String report = creditScoreService.generateCreditReport(userId);
        return ResponseEntity.ok(report);
    }
}
