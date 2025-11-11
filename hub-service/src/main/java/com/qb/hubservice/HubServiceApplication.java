package com.qb.hubservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableJpaAuditing
@ComponentScan(basePackages = {"com.qb.common"})
public class HubServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(HubServiceApplication.class, args);
  }

}
