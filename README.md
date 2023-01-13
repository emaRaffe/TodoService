

## Service description

### Tech stack (runtime environment, frameworks, key libraries)
The service consists of a REST Api built with spring boot and JDK 11, 
using Hibernate with JPA and H2 in memory DB.

### Specification

The service logic is implemented in TodoServiceImpl.java and it provides the 
operations for adding, fetching and updating the todos

A scheduler running once per day takes care of updating the status of the uncompleted
todos to the PAST_DUE status.


### assumptions
-Within a Kubernetes cluster, there should be as many pods as the traffic is requiring

-Additional not required functionalities (e.g. deletion of a todo may be implemented in 
subsequent versions of the application)

-Todos created with a due date in the past shall be updated on the first execution of the
automated scheduler 

-According the given requirement, in the current version the update only supports changing 
the description and the state, any other fields are not editable by the user

## build, run unit tests and run the spring boot app



execute from the root folder which will run the application on port 9010:

./run.sh

which will perform the following:
./mvnw clean && ./mvnw spring-boot:run

## docker version

execute from the root folder the following commands which will run the application on the docker available at port 9005:

./runDocker.sh

which will perform the following:
./mvnw clean install && docker build -t todoservice-0.0.1-snapshot.jar . && docker run -p 9005:9010 todoservice-0.0.1-snapshot.jar


## Swagger UI

http://localhost:9005/swagger-ui/index.html



## Example of requests (port needs to be adjusted when using the dockerized application)


### Create todo
curl --header "Content-Type: application/json" --request POST --data '{"description":"desc-1","dueDate":"2023-05-05'T'10:10:10"}' http://localhost:9010/todos/


### Get todo

curl http://localhost:9010/todos/{todo-id}


## Get todos

curl http://localhost:9010/todos


## Get uncompleted todos

curl http://localhost:9010/todos?status=NOT_DONE


## Update todo (description, status)

curl --header "Content-Type: application/json" --request PATCH --data '{"description":"desc-2","status":"DONE"}' http://localhost:9010/todos/{todo-id}




 