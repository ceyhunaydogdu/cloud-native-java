# Reservation Service REST API

This is the reservation rest application served as microservices in cloud environment. As a microservices,
there might be a need to refresh the application, so we should enalbe refreshing using actuator refresh endpoint by adding following property to `reservation-service.properties` file which is under `../config` folder.

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
