FROM openjdk:8-jre-alpine
WORKDIR /app
COPY target/rest-web-0.0.1-SNAPSHOT.jar /app/target/rest-web-0.0.1-SNAPSHOT.jar
COPY src/main/bin/run.sh .
COPY src/main/bin/wait-for-it.sh .
EXPOSE 8080
RUN ["chmod", "+x", "run.sh"]
CMD /bin/bash wait-for-it.sh -t 0 mysql:3306 --strict -- \
    /bin/bash wait-for-it.sh -t 0 kafka:9092 --strict -- \
    /bin/bash run.sh