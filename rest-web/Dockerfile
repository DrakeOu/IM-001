FROM openjdk:8-jre-alpine
WORKDIR /app
ADD target/rest-web-0.0.1-SNAPSHOT.jar /app/target/rest-web-0.0.1-SNAPSHOT.jar
ADD src/main/bin/run.sh /app
EXPOSE 8080
RUN ["chmod", "+x", "/app/run.sh"]
ENTRYPOINT ["sh", "/app/run.sh"]