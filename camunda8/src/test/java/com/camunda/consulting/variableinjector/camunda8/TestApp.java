package com.camunda.consulting.variableinjector.camunda8;

import io.camunda.zeebe.spring.client.annotation.Deployment;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Deployment(resources = "classpath:test-process.bpmn")
public class TestApp {
  public static void main(String[] args) {
    SpringApplication.run(TestApp.class, args);
  }
}
