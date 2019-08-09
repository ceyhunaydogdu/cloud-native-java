# Spring Boot Cloud Native Eureka Service Application

Since eureka service application runs on `Java 11`, we will face with `javax/xml/bind/JAXBException` when we launch the application. The reason is that the `java.xml.bind` module is not available as of `Java 11`,  so we need to add the JAXB RI module to the `pom.xml` as shown below in order to tackle exception.

```maven

<dependency>
    <groupId>org.glassfish.jaxb</groupId>
    <artifactId>jaxb-runtime</artifactId>
</dependency>

```

You may find details on [spring docs at Github](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-with-Java-9-and-above#jaxb)
