package com.camunda.consulting.variableinjector.camunda8;

import com.camunda.consulting.variableinjector.VariableInjector;
import com.camunda.consulting.variableinjector.VariableInjectorProperties;
import com.camunda.consulting.variableinjector.VariableInjectorRegistry;
import com.camunda.consulting.variableinjector.VariableInjectorUtil;
import io.camunda.zeebe.client.api.JsonMapper;
import io.camunda.zeebe.spring.client.bean.ParameterInfo;
import io.camunda.zeebe.spring.client.jobhandling.parameter.DefaultParameterResolverStrategy;
import io.camunda.zeebe.spring.client.jobhandling.parameter.ParameterResolver;
import java.util.Map;

public class VariableInjectorParameterResolverStrategy extends DefaultParameterResolverStrategy {
  private final VariableInjectorRegistry registry;
  private final VariableInjectorProperties properties;

  public VariableInjectorParameterResolverStrategy(
      JsonMapper jsonMapper,
      VariableInjectorRegistry registry,
      VariableInjectorProperties properties) {
    super(jsonMapper);
    this.registry = registry;
    this.properties = properties;
  }

  @Override
  public ParameterResolver createResolver(ParameterInfo parameterInfo) {
    Class<?> parameterType = parameterInfo.getParameterInfo().getType();
    if (isVariable(parameterInfo)) {
      String variableName = getVariableName(parameterInfo);
      Map<String, VariableInjector> variableInjectorByProcessDefinition =
          VariableInjectorUtil.resolveForVariableName(registry, properties, variableName);
      if (variableInjectorByProcessDefinition != null
          && !variableInjectorByProcessDefinition.isEmpty()) {
        return new VariableInjectorParameterResolver(
            variableName, parameterType, jsonMapper, variableInjectorByProcessDefinition);
      }
    }
    return super.createResolver(parameterInfo);
  }
}
