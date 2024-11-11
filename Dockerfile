# Usa una imagen base de Java con JDK 21
FROM openjdk:21-jdk-slim

# Establece el directorio de trabajo en el contenedor
WORKDIR /app

# Copia el archivo JAR de tu aplicación al contenedor
COPY target/S3Manager-0.0.3-SNAPSHOT.jar app.jar

# Expone el puerto en el que corre tu aplicación
EXPOSE 8081

# Define el comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
