FROM openjdk:8-jre-alpine
WORKDIR /app
COPY target/rest-web-0.0.1-SNAPSHOT.jar /app/target/rest-web-0.0.1-SNAPSHOT.jar
COPY src/main/bin/run.sh .
COPY src/main/bin/wait-for-it.sh .
EXPOSE 8080
RUN apk add --no-cache bash
RUN ["chmod", "+x", "run.sh" ,"wait-for-it.sh"]
CMD bash ./wait-for-it.sh -t 0 mysql:3306 --strict -- echo "mysql is up" -- \
    bash ./wait-for-it.sh -t 0 kafka:9092 --strict -- echo "kafka is up" -- \
    sh ./run.sh