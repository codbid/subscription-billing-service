package org.example.sbs.repository;

import org.example.sbs.enums.SubscriptionStatus;
import org.example.sbs.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Optional<List<Subscription>> findAllByStatusAndEndDate(SubscriptionStatus status, LocalDate endDate);

    Optional<List<Subscription>> findAllByStatus(SubscriptionStatus status);
}
