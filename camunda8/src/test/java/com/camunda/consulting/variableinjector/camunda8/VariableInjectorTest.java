package com.camunda.consulting.variableinjector.camunda8;

import static io.camunda.zeebe.process.test.assertions.BpmnAssert.*;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import io.camunda.zeebe.spring.test.ZeebeSpringTest;
import io.camunda.zeebe.spring.test.ZeebeTestThreadSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@ZeebeSpringTest
@SpringBootTest
public class VariableInjectorTest {
  @Autowired ZeebeClient zeebeClient;

  @Test
  void should() {
    ProcessInstanceEvent processInstance =
        zeebeClient
            .newCreateInstanceCommand()
            .bpmnProcessId("OrderHandlingProcess")
            .latestVersion()
            .variable("order", "123")
            .send()
            .join();
    ZeebeTestThreadSupport.waitForProcessInstanceCompleted(processInstance);
  }
}
