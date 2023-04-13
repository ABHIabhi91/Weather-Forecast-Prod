FROM adoptopenjdk:11-jre-hotspot
COPY  target/weatherforecast-0.0.1-SNAPSHOT.jar weatherforecast.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "weatherforecast.jar"]