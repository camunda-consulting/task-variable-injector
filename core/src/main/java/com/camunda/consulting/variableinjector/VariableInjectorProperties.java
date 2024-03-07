package com.camunda.consulting.variableinjector;

import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @param strategy the mapping processDefinition -> variableName -> strategy
 */
@ConfigurationProperties("camunda.variable-injector")
public record VariableInjectorProperties(
    @NestedConfigurationProperty Map<String, Map<String, String>> strategy) {}
