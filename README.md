# S3BucketsManager 📂

**S3BucketsManager** es una aplicación para administrar y monitorear buckets de Amazon S3. Construida con Java Maven y Spring para su integración con AWS, la aplicación cuenta con un frontend en HTML y TailwindCSS. Este proyecto soporta despliegue en Docker, facilitando el manejo de contenedores y el acceso en la nube.

## 🚀 Características
- **Gestión de Buckets S3**: Crear, listar y eliminar buckets de S3.
- **Monitoreo en Tiempo Real**: Rastreo de métricas de uso y registros de actividad en los buckets.
- **Manejo de Errores**: Excepciones personalizadas para mejorar el rendimiento.
- **Integración con ECS**: Preparada para despliegues en AWS ECS.
- **Interfaz Responsiva**: Construida con TailwindCSS para una experiencia de usuario óptima en cualquier dispositivo.

## 🛠️ Tecnologías
- **Java** (Spring Framework)
- **Servicios AWS** (S3, ECS)
- **HTML** & **TailwindCSS**
- **Docker** (Imagen a AWS ECR)

## 📂 Estructura del Proyecto
La estructura del código se organiza de la siguiente manera:

```plaintext
src/main/java/com/example/S3BucketsManager
│
├── ecs          # Integración con AWS ECS
├── s3           # Operaciones con Amazon S3
├── exception    # Manejo personalizado de excepciones
└── metrics      # Monitoreo y registro de métricas
```

## 📦 Dependencias
Las principales dependencias, incluidas en `pom.xml`, son:

- **Spring Boot**: Para la funcionalidad central de la aplicación.
- **Spring Cloud AWS**: Para integración fluida con AWS.
- **Dockerfile**: Para despliegues en contenedores.
- **Metrics**: Monitoreo en tiempo real.
- **TailwindCSS**: Para un diseño frontend responsivo.

## ⚙️ Instalación
Para ejecutar el proyecto localmente, sigue estos pasos:

1. **Clona el Repositorio**
    ```bash
    git clone https://github.com/SMG-web-dev/S3BucketManager.git
    ```
2. **Navega al Directorio del Proyecto**
    ```bash
    cd S3BucketManager
    ```
3. **Construye el Proyecto**
    ```bash
    mvn clean install
    ```
4. **Ejecuta la Aplicación**
    ```bash
    mvn spring-boot:run
    ```
5. **Despliegue en Docker** *(opcional)*
    ```bash
    docker build -t s3bucketmanager .
    docker run -p 8081:8081 s3bucketmanager
    ```

## 📖 Uso
Una vez en ejecución, accede a la aplicación en `http://localhost:8081` para administrar tus buckets de S3:

1. **Crear Buckets**: Crea y configura buckets de S3 fácilmente.
2. **Monitorear Buckets**: Accede a métricas en tiempo real y registros de actividad.
3. **Gestionar Recursos**: Visualiza, actualiza o elimina buckets existentes.

## 🤝 Contribuciones
¡Todas las contribuciones son bienvenidas! Siéntete libre de hacer un fork del repositorio, realizar mejoras y enviar un pull request.

## 📝 Licencia
Este proyecto está licenciado bajo la licencia MIT. Consulta el archivo `LICENSE` para más detalles.
