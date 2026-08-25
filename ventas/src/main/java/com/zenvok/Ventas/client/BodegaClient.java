package com.zenvok.Ventas.client;

import com.zenvok.Ventas.dto.BodegaProductoResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "bodega-client", url = "${bodega.service.url}")
public interface BodegaClient {

    @GetMapping("/producto/{productoId}")
    BodegaProductoResponseDTO buscarPorProducto(@PathVariable("productoId") Long productoId);
}