package com.qb.deliveryservice.application.service;

import com.qb.deliveryservice.application.dto.DeliveryCreateRequest;
import com.qb.deliveryservice.application.dto.DeliveryResponse;
import com.qb.deliveryservice.domain.model.Delivery;
import com.qb.deliveryservice.domain.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    @Transactional
    public DeliveryResponse create(DeliveryCreateRequest req){
        Delivery delivery = req.toEntity();
        Delivery saved = deliveryRepository.save(delivery);
        return DeliveryResponse.from(saved);
    }

}
