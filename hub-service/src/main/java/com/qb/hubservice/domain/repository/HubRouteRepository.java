package com.qb.hubservice.domain.repository;

import com.qb.hubservice.domain.model.Hub;
import com.qb.hubservice.domain.model.Hub_Route;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HubRouteRepository extends JpaRepository<Hub_Route, UUID> {

}
