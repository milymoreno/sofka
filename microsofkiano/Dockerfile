# Dockerfile para Sofkianos
FROM openjdk:17-slim
VOLUME /tmp
COPY target/sofkianos-0.0.1.jar sofkianos-0.0.1.jar
EXPOSE 7080  
ENTRYPOINT ["java", "-jar", "/sofkianos-0.0.1.jar"]
