package com.qb.deliveryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableJpaAuditing
@ComponentScan(basePackages = {"com.qb.deliveryservice", "com.qb.common"})
public class DeliveryServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(DeliveryServiceApplication.class, args);
  }

}
