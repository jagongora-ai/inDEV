package com.zenvok.Gest_Bode.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Modelo de peticion para crear o actualizar un estante en la bodega")
public class EstanteRequestDTO {

    @Schema(description = "Nombre o codigo identificador del estante", example = "Estante A1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El nombre del estante no puede estar vacío")
    @Size(max = 50, message = "El nombre del estante no puede superar los 50 caracteres")
    private String nombreEstante;
}