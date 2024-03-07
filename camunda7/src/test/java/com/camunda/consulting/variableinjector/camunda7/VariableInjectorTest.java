package com.camunda.consulting.variableinjector.camunda7;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.*;

import java.time.Duration;
import java.util.Map;
import org.awaitility.Awaitility;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class VariableInjectorTest {
  @Autowired ProcessEngine processEngine;

  @Test
  void shouldInjectOrderEntity() {
    processEngine
        .getRepositoryService()
        .createDeployment()
        .addClasspathResource("test-process.bpmn")
        .deploy();
    ProcessInstance processInstance =
        processEngine
            .getRuntimeService()
            .startProcessInstanceByKey("OrderHandlingProcess", Map.of("order", "123"));
    Awaitility.await()
        .timeout(Duration.ofSeconds(10))
        .untilAsserted(() -> assertThat(processInstance).isEnded());
  }
}
