FROM amazoncorretto:21-alpine-jdk

# Set working directory in container
WORKDIR /app

# Copy script and JAR into image
COPY entrypoint.sh entrypoint.sh
COPY target/CarService-0.0.1-SNAPSHOT.jar app.jar

# Expose port in container (e.g., Spring Boot default port)
EXPOSE 8080

# Start up Car-Service
ENTRYPOINT [ "/bin/sh" ]
CMD [ "entrypoint.sh" ]