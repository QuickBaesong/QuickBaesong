package com.qb.itemservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.qb.common.interceptor.FeignClientInterceptor;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableJpaAuditing(auditorAwareRef = "auditorAwareImpl")
public class ItemServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(ItemServiceApplication.class, args);
  }

}
