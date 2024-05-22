// file: CreditScoreHistoryRepository.java

package com.skillstorm.taxdemo.repositories;

import com.skillstorm.taxdemo.models.CreditScoreHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CreditScoreHistoryRepository extends JpaRepository<CreditScoreHistory, Long> {
    List<CreditScoreHistory> findByUserIdOrderByTimestampDesc(Long userId);
    Optional<CreditScoreHistory> findTopByUserIdOrderByTimestampDesc(Long userId); // Fetch the latest credit score
}
