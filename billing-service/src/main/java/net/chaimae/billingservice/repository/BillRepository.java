package net.chaimae.billingservice.repository;

import net.chaimae.billingservice.entities.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository  extends JpaRepository<Bill, Long> {
}