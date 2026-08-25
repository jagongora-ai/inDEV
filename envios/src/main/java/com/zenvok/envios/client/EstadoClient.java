package com.zenvok.envios.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "estado-client", url = "${ms.estado.url}")
public interface EstadoClient {

    @GetMapping("/api/estados/{id}")
    String buscarPorId(@PathVariable("id") Long id);
}