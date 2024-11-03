package org.example.sbs.repository;

import org.example.sbs.enums.PaymentStatus;
import org.example.sbs.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    public Optional<List<Payment>> findAllByStatus(PaymentStatus status);
}
