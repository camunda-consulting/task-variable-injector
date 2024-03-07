package com.camunda.consulting.variableinjector.camunda7;

import com.camunda.consulting.variableinjector.VariableInjectorProperties;
import com.camunda.consulting.variableinjector.VariableInjectorRegistry;
import org.camunda.bpm.client.spring.boot.starter.impl.PropertiesAwareSpringTopicSubscription;

public class VariableInjectorSpringTopicSubscriptionImpl
    extends PropertiesAwareSpringTopicSubscription {
  private final VariableInjectorRegistry variableInjectorRegistry;
  private final VariableInjectorProperties properties;

  public VariableInjectorSpringTopicSubscriptionImpl(
      VariableInjectorRegistry variableInjectorRegistry, VariableInjectorProperties properties) {
    this.variableInjectorRegistry = variableInjectorRegistry;
    this.properties = properties;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    externalTaskHandler =
        new VariableInjectorExternalTaskHandler(
            externalTaskHandler, variableInjectorRegistry, properties);
    super.afterPropertiesSet();
  }
}
