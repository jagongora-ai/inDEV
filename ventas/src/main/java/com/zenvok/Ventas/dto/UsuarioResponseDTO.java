package com.zenvok.Ventas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos del usuario obtenidos del microservicio de Autenticación/Clientes")
public class UsuarioResponseDTO {

    @Schema(description = "ID único del usuario",
    example = "1")
    private Long id;

    @Schema(description = "RUT / Documento de identidad del usuario",
    example = "12345678-9")
    private String rut;

    @Schema(description = "Nombre del usuario", 
    example = "Juan")
    private String nombre;

    @Schema(description = "Apellido del usuario", 
    example = "Pérez")
    private String apellido;

    @Schema(description = "Correo electrónico de contacto", 
    example = "juan.perez@email.com")
    private String correo;

    @Schema(description = "Nombre del rol asignado en el sistema", 
    example = "CLIENTE")
    private String nombreRol;
}