package com.BucketManager.S3Manager.ecs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final InactivityInterceptor inactivityInterceptor;

    public WebConfig(InactivityInterceptor inactivityInterceptor) {
        this.inactivityInterceptor = inactivityInterceptor;
    }

    // Registrar el InactivityInterceptor para que se ejecute con cada solicitud
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(inactivityInterceptor)
                .addPathPatterns("/**")  // Aplica el interceptor a todas las rutas
                .excludePathPatterns("/static/**", "/images/**", "/css/**", "/js/**"); // Excluir rutas estáticas
    }
}
