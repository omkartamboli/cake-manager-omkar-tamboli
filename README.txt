
Enhanced Cake Manager Micro Service (fictitious)
=======================================

--------------
Changes:
--------------

Project structural reforms:
1. Used Spring Boot for faster development and easy containerization of the code for cloud native development. Provides extensive support of opensource libraries.
2. Changed javax.persistence.* imports to jakarta.persistence.*, as javax.persistence.* is not supported by spring 3.x.
3. Used JpaRepository to avoid DAO boilerplate code.
4. Used different objects for persistence (entity) and data transfer layer (dto).
5. Improved package structure by adding persistence, security, dto, service and few more packages and grouped classes accordingly

Security additions:
1. Implemented Authentication and Role based access for APIs. Create, Update, Delete can be invoked by ROLE_ADMIN and Get apis can be invoked by both ROLE_ADMIN and ROLE_CUSTOMER
2. Enabled SSL config for security, to use HTTPS protocol instead of HTTP
3. XSS protection at parameter level and JSON body level
4. Added Global exception handler to abstract application exception details to be exposed to API consumers.
5. Changed application context path from root (/) to /cakeshop, as best coding practice and abstracting the application for generic attacks on root context path by robots

Monitoring support:
1. Implemented Logging and Performance Monitoring as Aspect to log entry, inputs, exit, return values and time taken for service and controller methods

CI/CD:
1. Added Docker file for containerization support.
2. Setup Spring Boot CI using GitHub workflows and tested CI config locally using act with Docker
3. Added Unit and Integration tests at service level.
4. Added Controller level Integration tests using Postman collection, executed within test class using newman cli
        (requires newman cli to be available on build server. install using 'npm install -g newman')

Documentation:
1. Added Swagger documentation for REST APIs
2. Added extensive javadoc

--------------
Actions:
--------------
1. To Run SpringBoot app from command line using >>  mvn clean install spring-boot:run
              Context Path for the App is >> https://localhost:9901/cakeshop
2. To access Swagger UI Documentation >> https://localhost:9901/cakeshop/swagger-ui/index.html
3. To run E2E Postman collection to test all APIs run (from application root dir) (requires newman)>> newman run src/test/resources/CakeController.postman_collection.json -e src/test/resources/CakeController.env.json --insecure
4. To test CI support using GitHub workflows (requires act installed) >> act push

--------------
Further Improvements:
--------------
1. Real persistence DB (like MySQL)
2. IAM based user auth instead of in-memory user config
3. Frontend or mobile APP of user-friendly access of APIs
4. Enhance ER DataModel to include more Entities and provide enriched experience. Pricing, Cart, Order, Delivery etc.



=======================================
Original README contents below
=======================================

=======================================
Cake Manager Micro Service (fictitious)
=======================================

A summer intern started on this project but never managed to get it finished.
The developer assured us that some of the above is complete, but at the moment accessing the /cakes endpoint
returns a 404, so getting this working should be the first priority.

Requirements:
* By accessing /cakes, it should be possible to list the cakes currently in the system. JSON would be an acceptable response format.

* It must be possible to add a new cake.

* It must be possible to update an existing cake.

* It must be possible to delete an existing cake.

Comments:
* We feel like the software stack used by the original developer is quite outdated, it would be good to migrate the entire application to something more modern. If you wish to update the repo in this manner, feel free! An explanation of the benefits of doing so (and any downsides) can be discussed at interview.

* Any other changes to improve the repo are appreciated (implementation of design patterns, seperation of concerns, ensuring the API follows REST principles etc)

Bonus points:
* Add some suitable tests (unit/integration...)
* Add some Authentication / Authorisation to the API
* Continuous Integration via any cloud CI system
* Containerisation

Scope
* Only the API and related code is in scope. No GUI of any kind is required


Original Project Info
=====================

To run a server locally execute the following command:

`mvn jetty:run`

and access the following URL:

`http://localhost:8282/`

Feel free to change how the project is run, but clear instructions must be given in README
You can use any IDE you like, so long as the project can build and run with Maven or Gradle.

The project loads some pre-defined data in to an in-memory database, which is acceptable for this exercise.  There is
no need to create persistent storage.


Submission
==========

Please provide your version of this project as a git repository (e.g. Github, BitBucket, etc).

A fork of this repo, or a Pull Request would be suitable.

Good luck!
