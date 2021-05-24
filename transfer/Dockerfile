FROM openjdk:8-jre-alpine
WORKDIR /app
COPY target/transfer-0.0.1-SNAPSHOT.jar /app/target/transfer-0.0.1-SNAPSHOT.jar
COPY src/main/resources/kafkaProducer.properties .
COPY src/main/resources/transfer.properties .
COPY src/main/bin/run.sh /app
COPY src/main/bin/wait-for-it.sh .
EXPOSE 6062
RUN ["chmod", "+x", "/app/run.sh"]
ENTRYPOINT ["sh", "/app/run.sh"]