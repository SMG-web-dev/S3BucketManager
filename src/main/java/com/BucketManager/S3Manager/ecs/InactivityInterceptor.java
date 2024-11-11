package com.BucketManager.S3Manager.ecs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class InactivityInterceptor implements HandlerInterceptor {

    private final InactivityHandler inactivityHandler;

    @Autowired
    public InactivityInterceptor(InactivityHandler inactivityHandler) {
        this.inactivityHandler = inactivityHandler;
    }

    // Este método se ejecuta para cada solicitud entrante
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Reiniciar el temporizador de inactividad en cada solicitud
        inactivityHandler.resetInactivityTimer();
        return true;
    }
}
