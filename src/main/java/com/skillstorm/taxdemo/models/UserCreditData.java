package com.skillstorm.taxdemo.models;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "user_credit_data")
public class UserCreditData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "on_time_payments")
    private int onTimePayments;

    @Column(name = "late_payments")
    private int latePayments;

    @Column(name = "missed_payments")
    private int missedPayments;

    @Column(name = "public_records")
    private int publicRecords;

    @Column(name = "credit_utilization")
    private double creditUtilization; // As a percentage

    @Column(name = "total_debt")
    private double totalDebt;

    @Column(name = "oldest_account_age")
    private int oldestAccountAge; // In months

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_credit_data_id")
    private List<CreditAccount> creditAccounts;

    @Column(name = "recent_inquiries")
    private int recentInquiries;

    @Column(name = "new_accounts")
    private int newAccounts;

    public UserCreditData() {
        // You can leave this empty, or initialize any fields with default values if needed
    }

    public UserCreditData(Long id, Long userId, int onTimePayments, int latePayments, int missedPayments,
            int publicRecords, double creditUtilization, double totalDebt, int oldestAccountAge,
            List<CreditAccount> creditAccounts, int recentInquiries, int newAccounts) {
        this.id = id;
        this.userId = userId;
        this.onTimePayments = onTimePayments;
        this.latePayments = latePayments;
        this.missedPayments = missedPayments;
        this.publicRecords = publicRecords;
        this.creditUtilization = creditUtilization;
        this.totalDebt = totalDebt;
        this.oldestAccountAge = oldestAccountAge;
        this.creditAccounts = creditAccounts;
        this.recentInquiries = recentInquiries;
        this.newAccounts = newAccounts;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getOnTimePayments() {
        return onTimePayments;
    }

    public void setOnTimePayments(int onTimePayments) {
        this.onTimePayments = onTimePayments;
    }

    public int getLatePayments() {
        return latePayments;
    }

    public void setLatePayments(int latePayments) {
        this.latePayments = latePayments;
    }

    public int getMissedPayments() {
        return missedPayments;
    }

    public void setMissedPayments(int missedPayments) {
        this.missedPayments = missedPayments;
    }

    public int getPublicRecords() {
        return publicRecords;
    }

    public void setPublicRecords(int publicRecords) {
        this.publicRecords = publicRecords;
    }

    public double getCreditUtilization() {
        return creditUtilization;
    }

    public void setCreditUtilization(double creditUtilization) {
        this.creditUtilization = creditUtilization;
    }

    public double getTotalDebt() {
        return totalDebt;
    }

    public void setTotalDebt(double totalDebt) {
        this.totalDebt = totalDebt;
    }

    public int getOldestAccountAge() {
        return oldestAccountAge;
    }

    public void setOldestAccountAge(int oldestAccountAge) {
        this.oldestAccountAge = oldestAccountAge;
    }

    public List<CreditAccount> getCreditAccounts() {
        return creditAccounts;
    }

    public void setCreditAccounts(List<CreditAccount> creditAccounts) {
        this.creditAccounts = creditAccounts;
    }

    public int getRecentInquiries() {
        return recentInquiries;
    }

    public void setRecentInquiries(int recentInquiries) {
        this.recentInquiries = recentInquiries;
    }

    public int getNewAccounts() {
        return newAccounts;
    }

    public void setNewAccounts(int newAccounts) {
        this.newAccounts = newAccounts;
    }

    

    
}
