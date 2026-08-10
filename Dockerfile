FROM ubuntu:latest

RUN apt update && \
    apt upgrade -y && \
    apt install openjdk-17-jdk -y && \
    apt install -y maven

WORKDIR /opt/app

COPY . .

RUN mvn clean package

CMD [ "java", "-jar", "target/app.jar" ]
