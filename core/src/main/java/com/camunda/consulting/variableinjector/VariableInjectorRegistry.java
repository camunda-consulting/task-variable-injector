package com.camunda.consulting.variableinjector;

import java.util.Map;

/**
 * @param registry the mapping injector name -> injector
 */
public record VariableInjectorRegistry(Map<String, VariableInjector> registry) {}
