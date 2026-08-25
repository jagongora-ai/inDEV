package com.zenvok.gestionusuario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO que representa la informacion de un usuario devuelta por el sistema")
public class UsuarioResponseDTO {
    
    @Schema(description = "Identificador unico del usuario", example = "1")
    private Long id;

    @Schema(description = "RUT del usuario", example = "12345678-9")
    private String rut;

    @Schema(description = "Nombre del usuario", example = "Juan")
    private String nombre;

    @Schema(description = "Apellido del usuario", example = "Perez")
    private String apellido;

    @Schema(description = "Correo electronico del usuario", example = "juan.perez@zenvok.cl")
    private String correo;

    @Schema(description = "Nombre del rol o tipo de usuario asignado", example = "Administrador")
    private String nombreRol;

}
