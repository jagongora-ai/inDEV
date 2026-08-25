package com.zenvok.Gest_Bode.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Modelo de peticion para asignar la ubicacion de un producto en la estructura de la bodega")
public class ProdPasEstRequestDTO {

    @Schema(description = "ID de la relacion Pasillo-Estante previamente establecida", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El ID de la relación Pasillo-Estante (pasEst) es obligatorio")
    private Long pasEstId;

    @Schema(description = "ID del producto que se desea ubicar", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El ID del producto es obligatorio")
    private Long productoId;
}