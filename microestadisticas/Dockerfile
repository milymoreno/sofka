# Dockerfile para Estadísticas
FROM openjdk:17-slim
VOLUME /tmp
COPY target/estadisticas-0.0.1.jar estadisticas-0.0.1.jar
EXPOSE 7081  
ENTRYPOINT ["java", "-jar", "/estadisticas-0.0.1.jar"]
