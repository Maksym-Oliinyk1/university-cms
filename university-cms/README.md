# University CMS.

### Table of contents

1. [ Project description ](#project-description)

2. [ Initial requirements ](#initial-requirements)

3. [ Build, deploy and run ](#build-deploy-and-run)

### Project description

This is a service responsible for holding and processing data related to users, user preferences and users' children

### Initial requirements

To build and run the application, you need to be installed and configured:

1. Java 17
2. Maven
3. Git
4. Postgres 16
5. Enabled annotation processing in your IDE settings
   (for IntelliJ IDEA Build, Execution, Deployment -> Compiler -> Annotation processors)

### Build, deploy and run

1. You can build the application and run tests with maven command:

   `mvn clean package`

2. You can run the application with Maven command (default 'prod' profile):

   `mvn spring-boot:start`
   or using Run configuration of your favorite IDE

3. Use next endpoints to ensure that application hac started successfully:

- [http://host:port/auth]
