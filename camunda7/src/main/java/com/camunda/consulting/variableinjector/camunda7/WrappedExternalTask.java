package com.camunda.consulting.variableinjector.camunda7;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.TypedValue;

public class WrappedExternalTask implements ExternalTask {
  private final ExternalTask wrappedTask;
  private final Map<String, Object> variableOverrides = new HashMap<>();

  public void addVariableOverride(String name, Object value) {
    variableOverrides.put(name, value);
  }

  public WrappedExternalTask(ExternalTask wrappedTask) {
    this.wrappedTask = wrappedTask;
  }

  @Override
  public String getActivityId() {
    return wrappedTask.getActivityId();
  }

  @Override
  public String getActivityInstanceId() {
    return wrappedTask.getActivityInstanceId();
  }

  @Override
  public String getErrorMessage() {
    return wrappedTask.getErrorMessage();
  }

  @Override
  public String getErrorDetails() {
    return wrappedTask.getErrorDetails();
  }

  @Override
  public String getExecutionId() {
    return wrappedTask.getExecutionId();
  }

  @Override
  public String getId() {
    return wrappedTask.getId();
  }

  @Override
  public Date getLockExpirationTime() {
    return wrappedTask.getLockExpirationTime();
  }

  @Override
  public String getProcessDefinitionId() {
    return wrappedTask.getProcessDefinitionId();
  }

  @Override
  public String getProcessDefinitionKey() {
    return wrappedTask.getProcessDefinitionKey();
  }

  @Override
  public String getProcessDefinitionVersionTag() {
    return wrappedTask.getProcessDefinitionVersionTag();
  }

  @Override
  public String getProcessInstanceId() {
    return wrappedTask.getProcessInstanceId();
  }

  @Override
  public Integer getRetries() {
    return wrappedTask.getRetries();
  }

  @Override
  public String getWorkerId() {
    return wrappedTask.getWorkerId();
  }

  @Override
  public String getTopicName() {
    return wrappedTask.getTopicName();
  }

  @Override
  public String getTenantId() {
    return wrappedTask.getTenantId();
  }

  @Override
  public long getPriority() {
    return wrappedTask.getPriority();
  }

  @Override
  public <T> T getVariable(String variableName) {
    if (variableOverrides.containsKey(variableName)) {
      return (T) variableOverrides.get(variableName);
    }
    return wrappedTask.getVariable(variableName);
  }

  @Override
  public <T extends TypedValue> T getVariableTyped(String variableName) {
    return getVariableTyped(variableName, true);
  }

  @Override
  public <T extends TypedValue> T getVariableTyped(
      String variableName, boolean deserializeObjectValue) {
    if (variableOverrides.containsKey(variableName)) {
      return (T) Variables.untypedValue(variableOverrides.get(variableName));
    }
    return wrappedTask.getVariableTyped(variableName, deserializeObjectValue);
  }

  @Override
  public Map<String, Object> getAllVariables() {
    Map<String, Object> allVariables = wrappedTask.getAllVariables();
    allVariables.putAll(variableOverrides);
    return null;
  }

  @Override
  public VariableMap getAllVariablesTyped() {
    return getAllVariablesTyped(true);
  }

  @Override
  public VariableMap getAllVariablesTyped(boolean deserializeObjectValues) {
    VariableMap variableMap = wrappedTask.getAllVariablesTyped();
    variableMap.putAll(variableOverrides);
    return null;
  }

  @Override
  public String getBusinessKey() {
    return wrappedTask.getBusinessKey();
  }

  @Override
  public String getExtensionProperty(String propertyKey) {
    return wrappedTask.getExtensionProperty(propertyKey);
  }

  @Override
  public Map<String, String> getExtensionProperties() {
    return wrappedTask.getExtensionProperties();
  }
}
