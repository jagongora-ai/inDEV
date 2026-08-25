package com.zenvok.envios.config;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.zenvok.envios.dto.DireccionResponse;


@Configuration
public class AppConfig {


    @FeignClient(name = "ms.direccion.url")
    public interface DireccionClient{
        @GetMapping("/api/direccion/{id}")
        DireccionResponse getDireccionById(@PathVariable Long id);
    }

    @FeignClient(name = "ms.venta.url")
    public interface VentaClient{
        @GetMapping("/api/venta/{id}")
        String getVentaById(@PathVariable Long id);
    }

    @FeignClient(name = "ms.estado.url")
    public interface EstadoClient{
        @GetMapping("/api/estado/{id}")
        String getEstadoById(@PathVariable Long id);
    }
}
