package com.zenvok.envios.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.zenvok.envios.dto.DireccionResponse;

@FeignClient(name = "direccion-client", url = "${ms.direccion.url}")
public interface DireccionClient {

    @GetMapping("/api/direcciones/{id}")
    DireccionResponse buscarPorId(@PathVariable("id") Long id);
}