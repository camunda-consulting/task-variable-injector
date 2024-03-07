package com.camunda.consulting.variableinjector.camunda8;

import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import org.springframework.stereotype.Component;

@Component
public class ZeebeController {

  @JobWorker
  public void orderHandling(@Variable OrderEntity order) {}
}
