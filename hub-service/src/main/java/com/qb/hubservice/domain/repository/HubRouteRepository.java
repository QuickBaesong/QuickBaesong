package com.qb.hubservice.domain.repository;

import com.qb.hubservice.domain.model.Hub;
import com.qb.hubservice.domain.model.HubRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HubRouteRepository  extends JpaRepository<HubRoute, UUID> {


    @Query("SELECT hr FROM HubRoute hr " +
            "JOIN FETCH hr.hub " +
            "JOIN FETCH hr.startHub " +
            "JOIN FETCH hr.destinationHub " +
            "WHERE hr.hubRouteId = :id")
    Optional<HubRoute> findByIdWithHubs(@Param("id") UUID id);

    boolean existsByHub_HubIdAndStartHub_HubIdAndDestinationHub_HubId(
            UUID hubId, UUID startHubId, UUID destinationHubId);

}
