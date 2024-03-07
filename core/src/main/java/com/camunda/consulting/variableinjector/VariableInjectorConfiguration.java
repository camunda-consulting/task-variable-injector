package com.camunda.consulting.variableinjector;

import java.util.concurrent.ConcurrentHashMap;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(VariableInjectorProperties.class)
public class VariableInjectorConfiguration {

  @Bean
  public VariableInjectorRegistry variableInjectorRegistry() {
    return new VariableInjectorRegistry(new ConcurrentHashMap<>());
  }

  @Bean
  public VariableInjectorPostProcessor variableInjectorRegistryFactory(
      VariableInjectorRegistry variableInjectorRegistry) {
    return new VariableInjectorPostProcessor(variableInjectorRegistry);
  }
}
