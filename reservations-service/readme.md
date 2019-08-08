# Reservation Service REST API

This is the reservation rest application served as microservices in cloud environment. Configuration properties are taken from config-service app which is a server for centralized configuration for all microservices. Dedicated configuration properties are in file named `reservation-service.properties` under `../config` folder. Below is the example

```properties

server.port=${PORT:8000}
message = iyi gunler istanbul!!!!!!!!

# define the destination to which the input MessageChannel should be bound
spring.cloud.stream.bindings.input.destination = reservations

# ensures 1 node in a group gets message (point-to-point, not a broadcast)
spring.cloud.stream.bindings.input.group = reservations-group

# ensure that the Q is durable
spring.cloud.stream.bindings.input.durableSubscription = true

# enables to refresh app throgh /actuator/refresh endpoint.
# management.endpoints.web.exposure.include=metrics, health, info, env, mappings, beans, refresh
management.endpoints.web.exposure.include=*
```

As a microservices, there might be a need to refresh the application, so we should enalbe refreshing using actuator refresh endpoint by adding following property to `reservation-service.properties` file.

```properties
# both of them works
# management.endpoints.web.exposure.include=metrics
management.endpoints.web.exposure.include=*
```

In order to refresh reservation-service application, run following curl command. The output is a list of changed properties as shown below.

```curl
$ curl -d{} -H "Content-Type:application/json"  http://localhost:8000/actuator/refresh

["config.client.version","message"]
```
