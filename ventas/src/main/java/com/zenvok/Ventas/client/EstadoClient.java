package com.zenvok.Ventas.client;

import com.zenvok.Ventas.dto.EstadoResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "estado-client", url = "${estado.service.url}")
public interface EstadoClient {

    @GetMapping("/{id}")
    EstadoResponseDTO buscarPorId(@PathVariable("id") Long id);
}