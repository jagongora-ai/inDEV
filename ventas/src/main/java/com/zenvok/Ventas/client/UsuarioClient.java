package com.zenvok.Ventas.client;

import com.zenvok.Ventas.dto.UsuarioResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "usuario-client", url = "${usuario.service.url}")
public interface UsuarioClient {

    @GetMapping("/{id}")
    UsuarioResponseDTO buscarPorId(@PathVariable("id") Long id);
}
