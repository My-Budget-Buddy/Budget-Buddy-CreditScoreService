package com.skillstorm.taxdemo.repositories;
// Replace with your actual package name

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skillstorm.taxdemo.models.CreditScoreHistory;

import java.util.List;

@Repository
public interface CreditScoreHistoryRepository extends JpaRepository<CreditScoreHistory, Long> {
    List<CreditScoreHistory> findByUserIdOrderByTimestampDesc(Long userId);
}

