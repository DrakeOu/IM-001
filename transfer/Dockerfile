FROM openjdk:8-jre-alpine
WORKDIR /app
ADD target/transfer-0.0.1-SNAPSHOT.jar /app/target/transfer-0.0.1-SNAPSHOT.jar
ADD src/main/bin/run.sh /app
EXPOSE 6062
ENTRYPOINT /app/run.sh