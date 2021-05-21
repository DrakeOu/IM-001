FROM openjdk:8-jre-alpine
WORKDIR /app
ADD target/connector-1.0-SNAPSHOT.jar /app/target/connector-1.0-SNAPSHOT.jar
ADD src/main/bin/run.sh /app
EXPOSE 6061
RUN ["chmod", "+x", "/app/run.sh"]
ENTRYPOINT ["sh", "/app/run.sh"]