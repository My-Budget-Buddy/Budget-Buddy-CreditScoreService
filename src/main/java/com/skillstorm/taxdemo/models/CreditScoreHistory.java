package com.skillstorm.taxdemo.models;
 // Replace with your actual package name

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "credit_score_history")
public class CreditScoreHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "score", nullable = false)
    private int score;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    public CreditScoreHistory() {
    }

    public CreditScoreHistory(Long id, Long userId, int score, LocalDateTime timestamp) {
        this.id = id;
        this.userId = userId;
        this.score = score;
        this.timestamp = timestamp;
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    // Constructors, getters, and setters (omitted for brevity)
    
}

