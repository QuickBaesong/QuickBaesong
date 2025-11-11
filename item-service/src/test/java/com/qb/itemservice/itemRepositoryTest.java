package com.qb.itemservice;

import static org.assertj.core.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.qb.itemservice.domain.entity.Item;
import com.qb.itemservice.domain.repository.ItemRepository;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

@DataJpaTest
@Slf4j
@DisplayName("Item Entity 테스트")
public class itemRepositoryTest {

	private EntityManager em;

	@Autowired
	private ItemRepository itemRepository;

	@DisplayName("모든 요소가 있을 때, 정상적으로 SAVE")
	@Test
	void ItemEntitySaveTest(){
		// given
		Item item = new Item(UUID.randomUUID(), UUID.randomUUID(), "상품A", 100000L, 1000L);

		// when
		Item result = itemRepository.save(item);
		log.info(result.getItemId().toString());

		// then
		assertThat(result.getItemId()).isNotNull();
		assertThat(result.getItemName()).isEqualTo("상품A");
	}

}
