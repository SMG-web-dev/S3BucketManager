package com.BucketManager.S3Manager;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.BucketManager.S3Manager.S3ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class S3Service {

    @Autowired
    private AmazonS3 amazonS3;
    private final S3Client s3Client;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    // Método para crear un nuevo bucket
    public void crearBucket(String nombreBucket) {
        try {
            List<Bucket> buckets = listarBuckets();
            if (buckets.size() >= 3) {
                throw new S3ServiceException("Error: Se ha alcanzado el límite de 3 buckets.");
            }
            s3Client.createBucket(CreateBucketRequest.builder()
                    .bucket(nombreBucket)
                    .build());
            System.out.println("Bucket " + nombreBucket + " creado exitosamente.");
        } catch (S3Exception e) {
            if (e.statusCode() == 409) {
                throw new S3ServiceException("Error: Ya existe un bucket con ese nombre: " + e.awsErrorDetails().errorMessage());
            } else {
                throw new S3ServiceException("Error creando el bucket: " + e.awsErrorDetails().errorMessage());
            }
        }
    }

    // Método para borrar un bucket
    public void borrarBucket(String bucketName) {
        try {
            List<S3Object> objetos = listarArchivos(bucketName);
            if (!objetos.isEmpty()) {
                throw new S3ServiceException("Error: El bucket no está vacío. Debes vaciarlo antes de borrarlo.");
            }
            DeleteBucketRequest deleteBucketRequest = DeleteBucketRequest.builder()
                    .bucket(bucketName)
                    .build();
            s3Client.deleteBucket(deleteBucketRequest);
            System.out.println("Bucket " + bucketName + " ha sido eliminado exitosamente.");
        } catch (S3Exception e) {
            throw new S3ServiceException("Error eliminando el bucket: " + e.awsErrorDetails().errorMessage());
        }
    }

    // Método para listar todos los buckets
    public List<Bucket> listarBuckets() {
        try {
            return s3Client.listBuckets().buckets();
        } catch (S3Exception e) {
            throw new S3ServiceException("Error listando los buckets: " + e.awsErrorDetails().errorMessage());
        }
    }

    // Método para listar objetos en un bucket específico
    public List<S3Object> listarArchivos(String bucketName) {
        try {
            return s3Client.listObjects(ListObjectsRequest.builder()
                            .bucket(bucketName)
                            .build())
                    .contents();
        } catch (S3Exception e) {
            throw new S3ServiceException("Error listando archivos en el bucket: " + e.awsErrorDetails().errorMessage());
        }
    }

    // Método para subir un archivo a un bucket
    public void subirArchivo(String bucketName, MultipartFile archivo) {
        try {
            if (archivo.getSize() > 2 * 1024 * 1024) {
                throw new S3ServiceException("Error: El archivo excede el límite de 2MB.");
            }
            List<S3Object> objetos = listarArchivos(bucketName);
            if (objetos.size() >= 3) {
                throw new S3ServiceException("Error: El número máximo de archivos por bucket es 3.");
            }
            String nombreArchivo = archivo.getOriginalFilename();
            s3Client.putObject(PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(nombreArchivo)
                    .build(), RequestBody.fromBytes(archivo.getBytes()));
            System.out.println("Archivo " + nombreArchivo + " subido exitosamente al bucket " + bucketName);
        } catch (S3Exception e) {
            throw new S3ServiceException("Error subiendo el archivo: " + e.awsErrorDetails().errorMessage());
        } catch (IOException e) {
            throw new S3ServiceException("Error al leer el archivo: " + e.getMessage());
        }
    }

    // Método para descargar un archivo de un bucket
    public byte[] descargarArchivo(String bucketName, String nombreArchivo) {
        try {
            ResponseBytes<GetObjectResponse> objeto = s3Client.getObjectAsBytes(GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(nombreArchivo)
                    .build());
            return objeto.asByteArray();
        } catch (S3Exception e) {
            throw new S3ServiceException("Error descargando el archivo: " + e.awsErrorDetails().errorMessage());
        }
    }

    // Método para vaciar un bucket
    public void vaciarBucket(String bucketName) {
        try {
            List<S3Object> objetos = listarArchivos(bucketName);
            for (S3Object objeto : objetos) {
                DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                        .bucket(bucketName)
                        .key(objeto.key())
                        .build();
                s3Client.deleteObject(deleteObjectRequest);
            }
            System.out.println("El bucket " + bucketName + " ha sido vaciado exitosamente.");
        } catch (S3Exception e) {
            throw new S3ServiceException("Error vaciando el bucket: " + e.awsErrorDetails().errorMessage());
        }
    }

    // Método para borrar un archivo de un bucket
    public void borrarArchivo(String bucketName, String fileName) {
        try {
            amazonS3.deleteObject(bucketName, fileName);
            System.out.println("Archivo " + fileName + " eliminado del bucket " + bucketName);
        } catch (AmazonS3Exception e) {
            throw new S3ServiceException("Error al eliminar el archivo: " + e.getMessage());
        }
    }
}
