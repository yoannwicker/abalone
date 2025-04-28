# Abalone

## Environments

TODO

# Requirements

- Java 21

# Developer settings

See contributing documentation: [CONTRIBUTING.md](CONTRIBUTING.md)

# Build and run

Run the app in dev mode with Intellij Idea (backend and frontend)

```shell
npm start
./gradlew :backend:application:bootRun
```

## Compile and run the fat jar

```shell
./gradlew bootJar
 java -jar backend/application/build/libs/abalone.jar
```