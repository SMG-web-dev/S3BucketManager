package com.BucketManager.S3Manager.metrics;

import com.BucketManager.S3Manager.ecs.ECSTaskManager;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MetricsController {

    @Autowired
    private ECSTaskManager ecsTaskManager;

    @Autowired
    private MeterRegistry meterRegistry;

    @GetMapping("/recibir-solicitud")
    public String recibirSolicitud() {
        // Incrementa el contador cada vez que se recibe una solicitud
        meterRegistry.counter("solicitudes_recibidas").increment();
        ecsTaskManager.startTaskIfNeeded();
        return "Solicitud recibida";
    }
}


