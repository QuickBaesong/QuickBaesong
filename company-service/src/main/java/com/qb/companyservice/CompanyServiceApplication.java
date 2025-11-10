package com.qb.companyservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@ComponentScan(basePackages = {"com.qb.common"})
@EnableJpaAuditing  // 이 어노테이션 추가
public class CompanyServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(CompanyServiceApplication.class, args);
  }

}