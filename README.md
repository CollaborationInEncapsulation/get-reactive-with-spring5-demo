# Get Reactive With Spring 5 and Project Reactor Demo App

## Structure 

  In this repo, you will find a bunch of useful code examples and patterns.
  
  There are five branches in this repo: 
  
### Brunches 

  - **master** - contains initial Spring 4 project with the working Chat application, integrated with Gitter Channel.
  - **reactive** - include completed migration to Spring 5 with the adaptation of blocking JDBC driver + integration with Server-Sent Event.
  - **reactive+ws** - include completed migration to Spring 5 with adaptation of blocking JDBC driver + integration with reactive WebSockets.
  - **template** - includes the template for migration to Reactive Spring 5 with Blocking Database adaptation. This repo prepared to code and contained the list of TODOs and FIXMEs. It will be useful if you would like to start coding with Spring 5 and learn essentials of Reactive Programming with Project Reactor. If you are not familiar with Reactor 3 or RxJava, please take a look on one of [those](https://projectreactor.io/learn) useful tutorials, and practice your self with the [next](https://tech.io/playgrounds/929/reactive-programming-with-reactor-3/Intro) codding tutorial.
  - **migration-commit-machine** - commit by commit migration to Reactive Spring 5 + Non-blocking, reactive DB (MongoDB in that case).

### Project 

This project is based on the typical layered architecture. It includes four main folders: 

  - **controller** - include list of endpoint for client-server communication purpose
  - **services** - include conceptual business logic: 
    - Integration with Gitter Service
    - Statistic service
    - Messaging service for transferring messages from Gitter service through the server, store messages in the database, etc.
  - **repository** - Spring Data repositories for data-access purpose
  - **domain** - database structure representation in Java classes.
  
## Issues

You are free to create an issue if you found one in the project.
