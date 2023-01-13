FROM adoptopenjdk:11-jdk-hotspot

ARG TERM=xterm
ARG DEBIAN_FRONTEND=noninteractive

EXPOSE 9010
WORKDIR /opt/app
ARG JAR_FILE=target/todoservice-0.0.1-snapshot.jar

COPY ${JAR_FILE} todoservice-0.0.1-snapshot.jar
ENTRYPOINT ["java","-jar","todoservice-0.0.1-snapshot.jar"]