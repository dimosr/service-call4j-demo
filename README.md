# Demo of ServiceCall-4j library
This repo contains a demo of how the ServiceCall-4j library can be used. It consists of:
- a Java web application, making remote calls to a "supposedly" remote dependency
- a monitoring application, where metrics are emitted about the calls to the dependency

# Getting started

To start the web application, execute the following command:
```sh
mvn tomcat7:run
```
The application is available in the location: http://localhost:8080/ServiceCall4j-Demo

To start the graphite monitoring application:
```sh
docker run -d\
 --name graphite\
 --restart=always\
 -p 80:80\
 -p 2003-2004:2003-2004\
 -p 2023-2024:2023-2024\
 -p 8125:8125/udp\
 -p 8126:8126\
 hopsoft/graphite-statsd
```
The graphite web interface is accessible in http://localhost:80/webapp
Metrics can be emitted through carbon in port 2023