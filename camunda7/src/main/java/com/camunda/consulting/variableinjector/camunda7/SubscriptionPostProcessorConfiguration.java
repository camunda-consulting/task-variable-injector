package com.camunda.consulting.variableinjector.camunda7;

import com.camunda.consulting.variableinjector.VariableInjectorRegistry;
import org.camunda.bpm.client.spring.boot.starter.ClientProperties;
import org.camunda.bpm.client.spring.boot.starter.impl.ClientAutoConfiguration;
import org.camunda.bpm.client.spring.impl.subscription.SubscriptionPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({ClientProperties.class})
@AutoConfigureAfter(ClientAutoConfiguration.class)
public class SubscriptionPostProcessorConfiguration {

  @Bean
  public static SubscriptionPostProcessor subscriptionPostprocessor(
      VariableInjectorRegistry variableInjectorRegistry) {
    return new VariableInjectorSubscriptionPostProcessor(
        VariableInjectorSpringTopicSubscriptionImpl.class, variableInjectorRegistry);
  }
}
