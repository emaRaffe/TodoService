./mvnw clean install && docker build -t todoservice-0.0.1-snapshot.jar . && docker run -p 9005:9010 todoservice-0.0.1-snapshot.jar