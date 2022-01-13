package com.test.ezypay.repository;

import com.test.ezypay.model.SubscriptionRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepo extends JpaRepository<SubscriptionRequest, Long> {
}
