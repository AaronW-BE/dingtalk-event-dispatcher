FROM openjdk:17-alpine

LABEL author="AaronW"

VOLUME /tmp

COPY build/libs/*.jar /app.jar

ENV JVM_OPTS="-Xmx256m -Xms256m"

ENTRYPOINT [ "sh", "-c", "java $JVM_OPTS -jar /app.jar"]