package com.zenvok.Gest_Bode.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Modelo de peticion para asociar un estante a un pasillo")
public class PasEstRequestDTO {

    @Schema(description = "ID del pasillo asociado", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El ID del pasillo es obligatorio")
    private Long pasilloId;

    @Schema(description = "ID del estante asociado", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El ID del estante es obligatorio")
    private Long estanteId;
}