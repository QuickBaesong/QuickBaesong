package com.qb.orderservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class TestAuditingConfig {

	@Bean
	public AuditorAware<String> auditorProvider() {
		return () -> Optional.of("test_user");
	}
}