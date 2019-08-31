# Spring Boot OAuth2 Authorization Server Application

The [`clientDetailsService`](./src/main/java/com/ca/samples/cloud/authservice/AuthorizationServerConfiguration.java) method in the server configuration overrides the original one in the spring framework, thus property of **`spring.main.allow-bean-definition-overriding`** should be set to **`true`** in the `bootstrap.properties` file as shown below.

```properties

spring.application.name=auth-service
spring.cloud.config.uri=http://localhost:8888
spring.main.allow-bean-definition-overriding=true

```

Alternatively, we can use [`custom ClientDetailsService`](./src/main/java/com/ca/samples/cloud/authservice/ClientConfiguration.java) in the auth service. In order to do that, we need to add the followings to the [`AuthorizationServerConfiguration`](./src/main/java/com/ca/samples/cloud/authservice/AuthorizationServerConfiguration.java) class.

```java

@Autowired
private ClientDetailsService myClientDetailsService;

@Override
public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients.withClientDetails(myClientDetailsService);
}

```

After setting up the oauth2-server, we can get access token by using curl command as shown below.

```curl

$ curl -X POST -d "username=ceyhun&password=native&grant_type=password&scope=openid&client_id=reservation-client&client_secret=res-secret" -H "Accept:application/json" -vu reservation-client:res-secret http://localhost:9191/uaa/oauth/token

```

Authorization server application should also secure `/uaa/user` endpoint with `@EnableResourceServer` annotation. After getting the access token with curl command above, we can access the authenticated user info by providing the access token to the user endpoint as shown below.

```curl

$ curl -H"authorization: bearer insert-token-value-here" http://localhost:9191/uaa/user

```

After locking down the reservation-client application, we can access to resources by providing access token like the following.

```curl

$ curl -H"authorization: bearer insert-token-value-here" http://localhost:9999/reservations/names

```
