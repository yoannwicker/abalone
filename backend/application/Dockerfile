FROM eclipse-temurin:21.0.2_13-jre

LABEL maintainer="Yoann Wicker <yoann.wicker@gmail.com>"

RUN mkdir /app
WORKDIR /app

COPY build/libs/pet-care.jar pet-care.jar

CMD ["/bin/sh", "-c", "java -Xms100M -Xmx250M -jar pet-care.jar --spring.profiles.active=qa"]

EXPOSE 8080

HEALTHCHECK CMD curl --fail http://localhost:8080 || exit 1

