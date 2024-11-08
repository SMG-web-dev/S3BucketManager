package com.BucketManager.S3Manager.ecs;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class InactivityHandler {

    private long lastRequestTime = System.currentTimeMillis();

    // Método que se ejecuta en cada solicitud entrante
    public void resetInactivityTimer() {
        lastRequestTime = System.currentTimeMillis();
    }

    // Este método se ejecuta cada 30 segundos
    @Scheduled(fixedRate = 30000) // 30 segundos
    public void checkInactivity() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastRequestTime > 30000) {
            // No hubo actividad en los últimos 30 segundos, detener la tarea ECS
            stopTaskIfInactive();
        }
    }

    private void stopTaskIfInactive() {
        // Aquí llamamos a ECS para detener la tarea
        // (implementaremos esta parte en el siguiente paso)
    }
}
