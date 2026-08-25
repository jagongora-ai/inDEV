package com.zenvok.producto.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Modelo de respuesta con la informacion detallada de un producto")
public class ProductoResponseDTO {

    @Schema(description = "Identificador unico del producto", example = "1")
    private Long id;

    @Schema(description = "Nombre del producto", example = "Teclado Mecanico RGB")
    private String nombre;

    @Schema(description = "Descripcion del producto", example = "Teclado con switches red y distribucion en español")
    private String descripcion;

    @Schema(description = "Precio unitario del producto", example = "24990.0")
    private BigDecimal precio;

    @Schema(description = "Identificador del estado asignado al producto", example = "1")
    private Long estadoId;

    @Schema(description = "Identificador de la categoria a la que pertenece el producto", example = "1")
    private Long categoriaId;

    @Schema(description = "Nombre descriptivo de la categoria", example = "Tecnologia")
    private String categoriaNombre;
}