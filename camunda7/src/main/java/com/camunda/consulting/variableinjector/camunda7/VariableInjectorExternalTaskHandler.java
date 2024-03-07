package com.camunda.consulting.variableinjector.camunda7;

import com.camunda.consulting.variableinjector.VariableInjector;
import com.camunda.consulting.variableinjector.VariableInjectorProperties;
import com.camunda.consulting.variableinjector.VariableInjectorRegistry;
import com.camunda.consulting.variableinjector.VariableInjectorUtil;
import java.util.Map;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;

public class VariableInjectorExternalTaskHandler implements ExternalTaskHandler {
  private final ExternalTaskHandler externalTaskHandler;
  private final VariableInjectorRegistry variableInjectorRegistry;
  private final VariableInjectorProperties properties;

  public VariableInjectorExternalTaskHandler(
      ExternalTaskHandler externalTaskHandler,
      VariableInjectorRegistry variableInjectorRegistry,
      VariableInjectorProperties properties) {
    this.externalTaskHandler = externalTaskHandler;
    this.variableInjectorRegistry = variableInjectorRegistry;
    this.properties = properties;
  }

  @Override
  public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
    WrappedExternalTask wrappedExternalTask = new WrappedExternalTask(externalTask);
    Map<String, VariableInjector> variableInjectorForVariableName =
        VariableInjectorUtil.resolveForProcess(
            variableInjectorRegistry, properties, externalTask.getProcessDefinitionKey());
    externalTask
        .getAllVariables()
        .keySet()
        .forEach(
            variableName -> {
              if (variableInjectorForVariableName.containsKey(variableName)) {
                wrappedExternalTask.addVariableOverride(
                    variableName,
                    variableInjectorForVariableName
                        .get(variableName)
                        .inject(externalTask.getVariable(variableName)));
              }
            });
    externalTaskHandler.execute(wrappedExternalTask, externalTaskService);
  }
}
