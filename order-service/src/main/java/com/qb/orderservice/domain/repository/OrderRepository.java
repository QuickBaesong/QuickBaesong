package com.qb.orderservice.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qb.orderservice.domain.entity.Order;

public interface OrderRepository extends JpaRepository<Order, UUID> {

	Optional<Order> findByOrderIdAndDeletedAtIsNull(UUID id);

}
