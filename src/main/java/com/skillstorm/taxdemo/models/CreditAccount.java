package com.skillstorm.taxdemo.models;

import jakarta.persistence.*;

@Entity
@Table(name = "credit_account")
public class CreditAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_type")
    private String accountType;

    @Column(name = "balance")
    private double balance;

    @Column(name = "credit_limit")
    private double creditLimit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_credit_data_id")
    private UserCreditData userCreditData;

    public CreditAccount() {
        // You can leave this empty, or initialize any fields with default values if needed
    }

    public CreditAccount(Long id, String accountType, double balance, double creditLimit,
            UserCreditData userCreditData) {
        this.id = id;
        this.accountType = accountType;
        this.balance = balance;
        this.creditLimit = creditLimit;
        this.userCreditData = userCreditData;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(double creditLimit) {
        this.creditLimit = creditLimit;
    }

    public UserCreditData getUserCreditData() {
        return userCreditData;
    }

    public void setUserCreditData(UserCreditData userCreditData) {
        this.userCreditData = userCreditData;
    }

    
}

