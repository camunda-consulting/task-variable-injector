package com.camunda.consulting.variableinjector.camunda7;

import com.camunda.consulting.variableinjector.VariableInjectorRegistry;
import org.camunda.bpm.client.spring.impl.subscription.SpringTopicSubscriptionImpl;
import org.camunda.bpm.client.spring.impl.subscription.SubscriptionConfiguration;
import org.camunda.bpm.client.spring.impl.subscription.SubscriptionPostProcessor;
import org.springframework.beans.factory.config.BeanDefinition;

public class VariableInjectorSubscriptionPostProcessor extends SubscriptionPostProcessor {
  private final VariableInjectorRegistry variableInjectorRegistry;

  public VariableInjectorSubscriptionPostProcessor(
      Class<? extends SpringTopicSubscriptionImpl> springTopicSubscription,
      VariableInjectorRegistry variableInjectorRegistry) {
    super(springTopicSubscription);
    this.variableInjectorRegistry = variableInjectorRegistry;
  }

  @Override
  protected BeanDefinition getBeanDefinition(
      String beanName, SubscriptionConfiguration subscriptionConfiguration) {
    BeanDefinition beanDefinition = super.getBeanDefinition(beanName, subscriptionConfiguration);
    beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(variableInjectorRegistry);
    return beanDefinition;
  }
}
