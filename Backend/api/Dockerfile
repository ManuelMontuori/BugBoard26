# 1. Partiamo da un'immagine leggera che contiene già Java 21
FROM eclipse-temurin:21-jre-alpine

# 2. Creiamo una cartella di lavoro dentro il container
WORKDIR /app

# 3. Copiamo il file .jar generato da Maven dentro il container
COPY target/api-0.0.1-SNAPSHOT.jar app.jar

# 4. Diciamo a Docker che l'app risponde alla porta 8080
EXPOSE 8080

# 5. Il comando per avviare l'applicazione
ENTRYPOINT ["java", "-jar", "app.jar"]