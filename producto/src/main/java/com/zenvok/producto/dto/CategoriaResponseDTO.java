package com.zenvok.producto.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Modelo de respuesta con la informacion de una categoria")
public class CategoriaResponseDTO {

    @Schema(description = "Identificador unico de la categoria", example = "1")
    private Long id;

    @Schema(description = "Nombre de la categoria", example = "Tecnologia")
    private String nombre;

    @Schema(description = "Descripcion de la categoria", example = "Dispositivos electronicos y accesorios tecnologicos")
    private String descripcion;
}
