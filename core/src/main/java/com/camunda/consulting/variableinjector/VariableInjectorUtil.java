package com.camunda.consulting.variableinjector;

import static java.util.Collections.*;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class VariableInjectorUtil {
  /**
   * Resolves a single variable injector by bpmn process id and variable name
   *
   * @param registry the registry of the variable injectors
   * @param properties the properties where the variable injectors are defined
   * @param bpmnProcessId the bpmn id of the process
   * @param variableName the name of the variable
   * @return the single variable injector matching or null
   */
  public static VariableInjector resolve(
      VariableInjectorRegistry registry,
      VariableInjectorProperties properties,
      String bpmnProcessId,
      String variableName) {
    return injectorForInjectorName(
        registry,
        injectorNameForVariableName(
            variableNamesForProcessDefinition(properties, bpmnProcessId), variableName));
  }

  public static Map<String, VariableInjector> resolveForProcess(
      VariableInjectorRegistry registry,
      VariableInjectorProperties properties,
      String bpmnProcessId) {
    return injectorForInjectorName(
        variableNamesForProcessDefinition(properties, bpmnProcessId), registry);
  }

  public static Map<String, VariableInjector> resolveForVariableName(
      VariableInjectorRegistry registry,
      VariableInjectorProperties properties,
      String variableName) {
    return injectorForInjectorName(
        processDefinitionForVariableName(properties, variableName), registry);
  }

  private static Map<String, String> processDefinitionForVariableName(
      VariableInjectorProperties properties, String variableName) {
    return properties.strategy().entrySet().stream()
        .filter(e -> e.getValue().containsKey(variableName))
        .map(e -> Map.entry(e.getKey(), injectorNameForVariableName(e.getValue(), variableName)))
        .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
  }

  private static Map<String, String> variableNamesForProcessDefinition(
      VariableInjectorProperties properties, String bpmnProcessId) {
    if (bpmnProcessId != null && properties.strategy().containsKey(bpmnProcessId)) {
      return properties.strategy().get(bpmnProcessId);
    }
    return emptyMap();
  }

  private static String injectorNameForVariableName(
      Map<String, String> injectorForVariableNames, String variableName) {
    if (variableName != null && injectorForVariableNames.containsKey(variableName)) {
      return injectorForVariableNames.get(variableName);
    }
    return null;
  }

  private static VariableInjector injectorForInjectorName(
      VariableInjectorRegistry registry, String injectorName) {
    if (injectorName != null && registry.registry().containsKey(injectorName)) {
      return registry.registry().get(injectorName);
    }
    return null;
  }

  private static Map<String, VariableInjector> injectorForInjectorName(
      Map<String, String> injectorNamesAsValues, VariableInjectorRegistry registry) {
    return injectorNamesAsValues.entrySet().stream()
        .map(e -> Map.entry(e.getKey(), injectorForInjectorName(registry, e.getValue())))
        .filter(e -> e.getValue() != null)
        .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
  }
}
