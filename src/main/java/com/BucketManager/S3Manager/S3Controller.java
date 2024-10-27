package com.BucketManager.S3Manager;

import com.BucketManager.S3Manager.S3Service;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class S3Controller {

    private final S3Service s3Service;

    public S3Controller(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @GetMapping("/")
    public String listarBuckets(@RequestParam(required = false) String nombreBucket, Model model) {
        List<Bucket> buckets = s3Service.listarBuckets();
        model.addAttribute("buckets", buckets);

        List<S3Object> archivos = null;
        if (nombreBucket != null) {
            archivos = s3Service.listarArchivos(nombreBucket);
        }

        model.addAttribute("archivos", archivos);
        model.addAttribute("nombreBucket", nombreBucket);

        Map<String, List<S3Object>> archivosPorBucket = new HashMap<>();
        for (Bucket bucket : buckets) {
            List<S3Object> archivosDelBucket = s3Service.listarArchivos(bucket.name());
            archivosPorBucket.put(bucket.name(), archivosDelBucket);
        }

        model.addAttribute("archivosPorBucket", archivosPorBucket);

        return "index";
    }

    @PostMapping("/subir")
    public String subirArchivo(@RequestParam("nombreArchivo") MultipartFile archivo,
                               @RequestParam("nombreBucket") String nombreBucket) throws IOException {
        s3Service.subirArchivo(nombreBucket, archivo);
        return "redirect:/";
    }

    @PostMapping("/crear")
    public String crearBucket(@RequestParam String nombreBucket) {
        s3Service.crearBucket(nombreBucket);
        return "redirect:/";
    }

    @PostMapping("/borrar")
    public String borrarBucket(@RequestParam String nombreBucket) {
        s3Service.borrarBucket(nombreBucket);
        return "redirect:/";
    }

    @PostMapping("/borrarArchivo")
    public String borrarArchivo(@RequestParam String nombreBucket, @RequestParam String nombreArchivo, RedirectAttributes redirectAttributes) {
        System.out.println("Bucket: " + nombreBucket + ", Archivo: " + nombreArchivo);
        try {
            s3Service.borrarArchivo(nombreBucket, nombreArchivo);
            redirectAttributes.addFlashAttribute("message", "Archivo eliminado exitosamente del bucket " + nombreBucket);
        } catch (S3ServiceException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar el archivo: " + e.getMessage());
        }
        return "redirect:/";
    }


    @PostMapping("/vaciar")
    public String vaciarBucket(@RequestParam String nombreBucket) {
        s3Service.vaciarBucket(nombreBucket);
        return "redirect:/";
    }

    @PostMapping("/descargarArchivo")
    public ResponseEntity<ByteArrayResource> descargarArchivo(@RequestParam String nombreBucket,
                                                              @RequestParam String nombreArchivo) {
        byte[] datos = s3Service.descargarArchivo(nombreBucket, nombreArchivo);

        ByteArrayResource resource = new ByteArrayResource(datos);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nombreArchivo + "\"")
                .contentLength(datos.length)
                .body(resource);
    }

    // Manejo de excepciones
    @ExceptionHandler(S3ServiceException.class)
    public String handleS3ServiceException(S3ServiceException e, Model model) {
        model.addAttribute("errorMessage", e.getErrorMessage());
        return "error";
    }
}
