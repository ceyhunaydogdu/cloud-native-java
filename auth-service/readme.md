# Spring Boot OAuth2 Authorization Server Application

```curl

$ curl -d "username=ceyhun&password=native&grant_type=password&scope=openid&client_id=reservation-client&client_secret=res-secret" -H "Accept:application/json" -X POST -vu reservation-client:res-secret http://localhost:9191/uaa/oauth/token

```

After locking down the reservation-client application, we can access to resources by providing access token as shown below.

```curl

$ curl -H"authorization: bearer insert-token-value-here" http://localhost:9999/reservations/names

```