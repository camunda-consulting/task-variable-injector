package com.camunda.consulting.variableinjector.camunda7;

import com.camunda.consulting.variableinjector.VariableInjector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("orderInjector")
public class OrderInjector implements VariableInjector {
  private static final Logger LOG = LoggerFactory.getLogger(OrderInjector.class);

  @Override
  public Object inject(Object identifier) {
    if (identifier instanceof String idString) {
      LOG.info("Injecting order for id {}", idString);
      return new OrderEntity(idString);
    }
    throw new IllegalStateException("Identifier not of type string");
  }
}
