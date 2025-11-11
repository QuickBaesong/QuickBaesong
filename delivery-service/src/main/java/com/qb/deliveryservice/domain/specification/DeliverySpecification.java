package com.qb.deliveryservice.domain.specification;

import com.qb.deliveryservice.application.dto.DeliverySearchCondition;
import com.qb.deliveryservice.domain.model.Delivery;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class DeliverySpecification {

    public static Specification<Delivery> searchWith(DeliverySearchCondition condition) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.isNull(root.get("deletedAt")));

            if (condition == null) {
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }

            if (condition.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("currentStatus"), condition.getStatus()));
            }

            if (condition.getManagerId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("companyManagerId"), condition.getManagerId()));
            }

            if (condition.getHubId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("destinationHubId"), condition.getHubId()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
