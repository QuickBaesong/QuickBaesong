package com.qb.hubservice.domain.repository;

import com.qb.hubservice.domain.model.HubRoute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HubRouteRepository extends JpaRepository<HubRoute, UUID> {


}
