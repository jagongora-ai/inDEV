package com.zenvok.envios.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "venta-client", url = "${ms.venta.url}")
public interface VentaClient {

    @GetMapping("/api/ventas/{id}")
    String buscarPorId(@PathVariable("id") Long id);
}