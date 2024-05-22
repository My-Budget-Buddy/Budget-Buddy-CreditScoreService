package com.skillstorm.taxdemo.repositories;

import com.skillstorm.taxdemo.models.UserCreditData;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCreditDataRepository extends JpaRepository<UserCreditData, Long> {
    Optional <UserCreditData> findByUserId(Long userId);
}
