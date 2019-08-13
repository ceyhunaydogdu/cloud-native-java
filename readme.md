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

## Messaging with RabbitMQ

Docker commands to pull `RabbitMQ` and `RabbitMQ Management` images respectively from docker hub is below

```docker

$ docker pull rabbitmq:3-alpine

$ docker pull rabbitmq:3-management-alpine

```

Docker commands to run the `RabbitMQ` and `RabbitMQ Management` images respectively.

```docker

$ docker run -rm -d --hostname ca-rabbit -p 5672:5672 --name rca -d rabbitmq:3-alpine

$ docker run -rm -d --hostname cam-rabbit -p 15672:15672 --name rmca rabbitmq:3-management-alpine

```

Below is the curl command to insert reservation through messaging service

```curl

$ curl -d '{ "reservationName": "Emirhan"}' -H "Content-Type:application/json" http://localhost:9999/reservations

```