FROM openjdk:8-jdk-alpine

MAINTAINER jm5619

RUN mkdir /app

WORKDIR /app

ADD ./target/ir-image-retrieval-1.0.0-SNAPSHOT.jar /app

EXPOSE 8085

CMD ["java", "-jar", "ir-image-retrieval-1.0.0-SNAPSHOT.jar"]
