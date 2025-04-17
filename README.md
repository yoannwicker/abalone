# pet-care

## Environments

- QA: http://206.189.30.36:4200/

QA is deployed automatically on each push on develop branch.

# Requirements

- Java 21

# Developer settings

See contributing documentation: [CONTRIBUTING.md](CONTRIBUTING.md)   

# Build and run

Run the app in dev mode with Intellij Idea (backend and frontend)

## Compile and run the fat jar

```shell
./gradlew bootJar
 java -jar backend/application/build/libs/babel.jar
```