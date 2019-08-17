# Spring Boot Cloud Native Java Workshop

This repository contains a workshop for developing cloud native application using java based on Spring Framework.

## Cloud Native Java Workshop Configuration Properties

The `config` folder serves as a configuration base for `Config Service(Server) Application` and contains properties files for each application used in the workshop.

After updating or editing any file on the fly in this configuration folder, changes can be committed with git command as the example below in order to reflect changes to the runnig services.

```git
$ git commit -a -m YOLO

[master 0160ee8] YOLO
 1 file changed, 3 insertions(+), 2 deletions(-)
```

Following curl command should executed to refresh the application

```curl
$ curl -d{} -X POST -H "Content-Type:application/json" http://localhost:8000/actuator/refresh

```

## Messaging with RabbitMQ

Docker commands to pull `RabbitMQ` and `RabbitMQ Management` images respectively from docker hub is below

```docker

$ docker pull rabbitmq:3-alpine

$ docker pull rabbitmq:3-management-alpine

```

Docker commands to run the `RabbitMQ` and `RabbitMQ Management` images respectively.

```docker

$ docker run --rm -d --hostname ca-rabbit -p 5672:5672 --name rca -d rabbitmq:3-alpine

$ docker run --rm -d --hostname cam-rabbit -p 15672:15672 --name rmca rabbitmq:3-management-alpine

```

Below is the curl command to insert reservation through messaging service

```curl

$ curl -d '{ "reservationName": "Emirhan"}' -H "Content-Type:application/json" http://localhost:9999/reservations

```

## Hystrix Dashboard

To enable hystrix dashboard for monitoring the application [hystrix stream endpoint](https://localhost:9999/actuator/hystrix.stream) should be fed to dashboard ui which is available at [http://localhost:8010/hystrix.html](http://localhost:8010/hystrix.html).

## Zipkin Service

In order to monitor distributed traces of the applications, we need run Zipkin UI on docker separetly. Docker commands to run `zipkin server` image on local machine is below.

```docker

$ docker run --rm -d -p 9411:9411 --name zca openzipkin/zipkin

```

Then, we need to add following dependencies to pom files of both `reservations-service` and `reservation-client` applications to enable them send traces to zipkin server.

```maven

<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-zipkin</artifactId>
</dependency>
<dependency>
<groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-sleuth</artifactId>
</dependency>

```
