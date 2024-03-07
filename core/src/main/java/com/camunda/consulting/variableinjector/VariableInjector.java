package com.camunda.consulting.variableinjector;

/** To register the injector, annotate it with @ */
public interface VariableInjector {
  Object inject(Object identifier);
}
