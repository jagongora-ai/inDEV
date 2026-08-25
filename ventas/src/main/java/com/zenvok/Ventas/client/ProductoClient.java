package com.zenvok.Ventas.client;

import com.zenvok.Ventas.dto.ProductoResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "producto-client", url = "${producto.service.url}")
public interface ProductoClient {

    @GetMapping("/{id}")
    ProductoResponseDTO buscarPorId(@PathVariable("id") Long id);
}