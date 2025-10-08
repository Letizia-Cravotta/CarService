FROM amazoncorretto:21-alpine-jdk

# install utility tools
RUN apk add --no-cache curl

# Set working directory in container
WORKDIR /app

# Copy script and JAR into image
COPY entrypoint.sh /app/entrypoint.sh
COPY target/CarService-0.0.1-SNAPSHOT.jar /app/app.jar

# Make script executable
RUN chmod +x /app/entrypoint.sh
RUN chmod +x /app/app.jar

# Expose port in container (e.g., Spring Boot default port)
EXPOSE 8080

# Start up Car-Service
ENTRYPOINT [ "/bin/sh" ]
CMD [ "/app/entrypoint.sh" ]