package com.qb.hubservice.domain.repository;

import com.qb.hubservice.domain.model.Hub;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface HubRepository extends JpaRepository<Hub, UUID> {

}
