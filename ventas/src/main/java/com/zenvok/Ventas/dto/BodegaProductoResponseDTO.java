package com.zenvok.Ventas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos de la ubicación física del producto en la bodega")
public class BodegaProductoResponseDTO {

    @Schema(description = "ID de la relación entre el producto y el estado en bodega", example = "10")
    private Long idProdEst;

    @Schema(description = "ID del producto almacenado", example = "45")
    private Long productoId;

    @Schema(description = "ID del pasillo y estante asignado", example = "3")
    private Long pasEstId;

    @Schema(description = "Nombre o código del pasillo en la bodega", example = "Pasillo A - Alimentos")
    private String nombrePasillo;

    @Schema(description = "Nombre o número del estante específico", example = "Estante 04")
    private String nombreEstante;
}
