package com.qb.hubservice.domain.repository;

import com.qb.hubservice.domain.model.Hub;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface HubRepository extends JpaRepository<Hub, UUID> {


    Optional<Hub> findByIdAndIsDeletedFalse(UUID id);


    Page<Hub> findAllByIsDeletedFalse(Pageable pageable);



}
