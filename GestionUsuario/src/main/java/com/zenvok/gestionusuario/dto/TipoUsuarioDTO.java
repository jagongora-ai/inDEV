package com.zenvok.gestionusuario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO que representa un tipo de usuario del sistema")
public class TipoUsuarioDTO {

    @Schema(description = "Identificador unico del tipo de usuario", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    
    @Schema(
        description = "Nombre del tipo de usuario",
        example = "Administrador",
        allowableValues = {"Administrador","Bodeguero","Repartidor","Cliente"})
    private String nombreTipo;
}