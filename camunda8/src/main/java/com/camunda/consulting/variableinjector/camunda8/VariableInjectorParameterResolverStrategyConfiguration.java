package com.camunda.consulting.variableinjector.camunda8;

import com.camunda.consulting.variableinjector.VariableInjectorProperties;
import com.camunda.consulting.variableinjector.VariableInjectorRegistry;
import io.camunda.zeebe.client.api.JsonMapper;
import io.camunda.zeebe.spring.client.jobhandling.parameter.ParameterResolverStrategy;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@EnableConfigurationProperties(VariableInjectorProperties.class)
public class VariableInjectorParameterResolverStrategyConfiguration {
  @Bean
  @Primary
  public ParameterResolverStrategy parameterResolverStrategy(
      JsonMapper jsonMapper,
      VariableInjectorRegistry variableInjectorRegistry,
      VariableInjectorProperties properties) {
    return new VariableInjectorParameterResolverStrategy(
        jsonMapper, variableInjectorRegistry, properties);
  }
}
