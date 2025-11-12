package com.qb.deliveryservice.domain.repository;

import com.qb.deliveryservice.domain.model.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryRepository extends JpaRepository<Delivery, UUID>, JpaSpecificationExecutor<Delivery> {
    @Query("SELECT d FROM Delivery d WHERE d.id = :id AND d.deletedAt IS NULL")
    Optional<Delivery> findByIdAndNotDeleted(@Param("id") UUID id);

    @Query("SELECT d FROM Delivery d WHERE d.orderId = :orderId AND d.deletedAt IS NULL")
    Optional<Delivery> findByOrderIdAndNotDeleted(@Param("orderId") UUID orderId);
}
