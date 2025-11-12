package com.qb.deliveryservice.domain.repository;

import com.qb.deliveryservice.domain.model.DeliveryManagerSequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryManagerSequenceRepository extends JpaRepository<DeliveryManagerSequence, String> {

    @Query("SELECT dms FROM DeliveryManagerSequence dms WHERE dms.sequenceType = :sequenceType AND dms.hubId IS NULL")
    Optional<DeliveryManagerSequence> findBySequenceTypeAndHubIdIsNull(@Param("sequenceType") String sequenceType);

    @Query("SELECT dms FROM DeliveryManagerSequence dms WHERE dms.sequenceType = :sequenceType AND dms.hubId = :hubId")
    Optional<DeliveryManagerSequence> findBySequenceTypeAndHubId(
            @Param("sequenceType") String sequenceType,
            @Param("hubId") UUID hubId
    );
}
