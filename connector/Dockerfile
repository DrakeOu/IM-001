FROM openjdk:8-jre-alpine
WORKDIR /app
COPY target/connector-1.0-SNAPSHOT.jar /app/target/connector-1.0-SNAPSHOT.jar
COPY src/main/bin/run.sh .
COPY src/main/bin/wait-for-it.sh .
EXPOSE 6061
RUN ["chmod", "+x", "run.sh"]
CMD /bin/bash wait-for-it.sh -t 0 transfer:6062 --strict -- \
    /bin/bash run.sh