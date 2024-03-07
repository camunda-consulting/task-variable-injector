# Task Variable Injector

## What is the purpose of this project?

### Problem statement

Data handling can be an issue when using Camunda with big process variables (like documents or very verbose data structures).

The general recommendation is to externalize this data to other data sources like databases or blob storage and only reference them from the process instance.

If a project was started using the Camunda internal storage for this kind of data, migration to an external storage can become much harder.

### Solution

Instead of rewriting all implementations, the Camunda client can be extended so that a mapping from an id variable to the actual object can be done in a generic manner.

This project presents one possible implementation, spanning across Camunda 7 and Camunda 8.

## How does it work?

In both cases, there some sort of interception happening right before the implementation for a task is called.

This interception checks the task context for variables that should be mapped according to a configuration and finds the right mapper for this.

Instead of the actual value coming from the process engine, the value fetched by the mapper is then injected to the implementation.

>Limitation: This does only work one-way. Submitted data will not be intercepted.

## Show me an example!

Given is an implementation that uses an object that should come from an external source.

[Example for Camunda 7](./camunda7/src/test/java/com/camunda/consulting/variableinjector/camunda7/OrderHandlingWorker.java):
```java
package com.camunda.consulting.variableinjector.camunda7;

import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.stereotype.Component;

@ExternalTaskSubscription("order-handling")
@Component
public class OrderHandlingWorker implements ExternalTaskHandler {
  @Override
  public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
    OrderEntity order = externalTask.getVariable("order");
    externalTaskService.complete(externalTask);
  }
}

```

[Example for Camunda 8](./camunda8/src/test/java/com/camunda/consulting/variableinjector/camunda8/ZeebeController.java):
```java
package com.camunda.consulting.variableinjector.camunda8;

import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import org.springframework.stereotype.Component;

@Component
public class ZeebeController {

  @JobWorker
  public void orderHandling(@Variable OrderEntity order) {}
}

```

To use the task variable injector, you need to add the core package as dependency:

```xml
<dependency>
  <groupId>com.camunda.consulting</groupId>
  <artifactId>task-variable-injector-core</artifactId>
</dependency>
```

In addition, you need to add the dependency for the used platform:

```xml
<dependency>
  <groupId>com.camunda.consulting</groupId>
  <artifactId>task-variable-injector-camunda7</artifactId>
</dependency>
```
When using Camunda 7, you need to configure an additional spring property in your `application.yml`:

```yaml
spring:
  main:
    allow-bean-definition-overriding: true
```

```xml
<dependency>
  <groupId>com.camunda.consulting</groupId>
  <artifactId>task-variable-injector-camunda8</artifactId>
</dependency>
```

Then, you can create your own `VariableInjector`. Here, some aspects are relevant:

* spring boot is used
* the received parameter is the value coming from the process / task
* the returned value if the value you want the task implementation to handle
* provide a name to the bean as this will be required when registering the `VariableInjector`

[Example](./camunda7/src/test/java/com/camunda/consulting/variableinjector/camunda7/OrderInjector.java):
```java
package com.camunda.consulting.variableinjector.camunda7;

import com.camunda.consulting.variableinjector.VariableInjector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("orderInjector")
public class OrderInjector implements VariableInjector {
  private static final Logger LOG = LoggerFactory.getLogger(OrderInjector.class);

  @Override
  public Object inject(Object identifier) {
    if (identifier instanceof String idString) {
      LOG.info("Injecting order for id {}", idString);
      return new OrderEntity(idString);
    }
    throw new IllegalStateException("Identifier not of type string");
  }
}
```

Next, you need to register the `VariableInjector`. This is done via `application.yaml`. Here, you have a given structure:

* root context for the registration is `camunda.variable-injector.strategy`
* the strategy is a context that maps from `bpmnProcessId` to an inner context
* this inner context maps from `variableName` to `injectorBeanName`

[Example](./camunda7/src/test/resources/application.yaml):
```yaml
camunda:
  variable-injector:
    strategy:
      OrderHandlingProcess:
        order: orderInjector
```

Then, you can run a process instance of the process with the configured `bpmnProcessId` that has a variable with the configured `variableName` (and the right type of course).
