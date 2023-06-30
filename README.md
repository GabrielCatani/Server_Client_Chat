# Chat Server-Client

## Description
Simple Server with multiple clients chat, to explore the Java Socket API along with the Spring Framework. 
Used the Hikari lib to establish a DataSource abstraction, in order to work with different Data Bases (default, I'm using Postgres, to learn more about the inner works of it).

Apart from getting used to the Spring Framework and work with Sockets in Java, I used this project to learn a little bit more on how to interact with databased using JDBCTemplates, and work with simple encodig/decoding functionality within spring. 
### Getting Started
  To use/test the project, first thing is to compile the SockerServer module (which contains all Server context: receive messages, broadcats messages, handle multiple clients, persist users and messages). After that, you can start one or more SocketClient instances, to connect to the server, and exchange messages with other connected users.
### Dependencies
I'm using Maven to handle external dependencies, and everything is wrapped and configured in Spring Framework, through the use of annotations and a Config class for Beans registration.

### Usage
After Server and client are up, the following functions are available, executed from the client perspective.


#### Initial Interaction
```
1. Sign up
2. Sign in
3. Exit
```

#### Functions, after successfully log in

```
1. Create Chat Room
2. Select Chat Room
3. Exit
```