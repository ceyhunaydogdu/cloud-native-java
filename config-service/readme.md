# Spring Boot Cloud Native Java Workshop Config Service(Server) Application

In order to assign a specific folder to store config files, `application.yml` should be configured as follows:

```yaml

spring:
  cloud:
    config:
      server:
        git:
          uri: ${CA}/cloud-native-java
          search-paths:
          - config-service/config
server:
  port: 8888

```
