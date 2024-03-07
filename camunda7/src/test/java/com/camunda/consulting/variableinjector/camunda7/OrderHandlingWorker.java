package com.camunda.consulting.variableinjector.camunda7;

import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.stereotype.Component;

@ExternalTaskSubscription("order-handling")
@Component
public class OrderHandlingWorker implements ExternalTaskHandler {
  @Override
  public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
    OrderEntity order = externalTask.getVariable("order");
    externalTaskService.complete(externalTask);
  }
}
