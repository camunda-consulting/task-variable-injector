package com.camunda.consulting.variableinjector.camunda8;

import com.camunda.consulting.variableinjector.VariableInjector;
import io.camunda.zeebe.client.api.JsonMapper;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.jobhandling.parameter.VariableResolver;
import java.util.Map;

public class VariableInjectorParameterResolver extends VariableResolver {
  private final Map<String, VariableInjector> variableInjectorByProcessDefinition;

  public VariableInjectorParameterResolver(
      String variableName,
      Class<?> variableType,
      JsonMapper jsonMapper,
      Map<String, VariableInjector> variableInjectorByProcessDefinition) {
    super(variableName, variableType, jsonMapper);
    this.variableInjectorByProcessDefinition = variableInjectorByProcessDefinition;
  }

  @Override
  public Object resolve(JobClient jobClient, ActivatedJob job) {
    if (variableInjectorByProcessDefinition.containsKey(job.getBpmnProcessId())) {
      VariableInjector variableInjector =
          variableInjectorByProcessDefinition.get(job.getBpmnProcessId());
      return variableInjector.inject(getVariable(job));
    }
    return super.resolve(jobClient, job);
  }
}
