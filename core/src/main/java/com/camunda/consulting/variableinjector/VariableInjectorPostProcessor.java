package com.camunda.consulting.variableinjector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class VariableInjectorPostProcessor implements BeanPostProcessor {
  private static final Logger LOG = LoggerFactory.getLogger(VariableInjectorPostProcessor.class);

  private final VariableInjectorRegistry variableInjectorRegistry;

  public VariableInjectorPostProcessor(VariableInjectorRegistry variableInjectorRegistry) {
    this.variableInjectorRegistry = variableInjectorRegistry;
  }

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    if (bean instanceof VariableInjector variableInjector) {
      LOG.info("Registering variable injector {}", beanName);
      variableInjectorRegistry.registry().put(beanName, variableInjector);
    }
    return bean;
  }
}
