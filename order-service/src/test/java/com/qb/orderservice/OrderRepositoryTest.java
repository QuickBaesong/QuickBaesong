// package com.qb.orderservice;
//
// import static org.assertj.core.api.Assertions.*;
//
// import java.time.LocalDateTime;
// import java.util.UUID;
//
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
// import org.springframework.context.annotation.Import;
//
// import com.qb.orderservice.domain.entity.Order;
// import com.qb.orderservice.domain.entity.OrderItem;
// import com.qb.orderservice.domain.entity.OrderStatus;
// import com.qb.orderservice.domain.repository.OrderRepository;
//
// import jakarta.persistence.EntityManager;
//
//
// @DataJpaTest
// @Import(TestAuditingConfig.class)
// @DisplayName("Order Entity 테스트")
// public class OrderRepositoryTest {
//
// 	@Autowired
// 	private EntityManager em;
//
// 	@Autowired
// 	private OrderRepository orderRepository;
//
// 	@DisplayName("모든 요소가 있을 때 정상적으로 SAVE")
// 	@Test
// 	void OrderEntitySaveTest(){
// 		// given
//
// 		Order order = Order.builder()
// 			.hubId(UUID.randomUUID())
// 			.receiver(UUID.randomUUID())
// 			.sender(UUID.randomUUID())
// 			.requiredDeliveryAt(LocalDateTime.now())
// 			.userId(UUID.randomUUID())
// 			.build();
//
// 		OrderItem orderItemOne = OrderItem.builder()
// 			.itemId(UUID.randomUUID())
// 			.price(5500L)
// 			.quantity(10L)
// 			.build();
//
// 		OrderItem orderItemTwo = OrderItem.builder()
// 			.itemId(UUID.randomUUID())
// 			.price(9900L)
// 			.quantity(5L)
// 			.build();
//
// 		order.addOrderItem(orderItemOne);
// 		order.addOrderItem(orderItemTwo);
//
//
// 		// when
// 		Order result = orderRepository.save(order);
//
// 		em.flush();
// 		em.clear();
//
// 		Order newOrder = orderRepository.findById(result.getOrderId()).orElseThrow();
//
// 		assertThat(newOrder.getOrderItems().size()).isEqualTo(2);
//
// 		// then
// 		assertThat(newOrder.getOrderStatus()).isEqualTo(OrderStatus.CREATED);
// 		assertThat(newOrder.getOrderItems().get(0).getItemId()).isEqualTo(orderItemOne.getItemId());
//
// 	}
// }
