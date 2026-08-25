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
@Schema(description = "Modelo de peticion para crear o actualizar un pasillo en la bodega")
public class PasilloRequestDTO {

    @Schema(description = "Nombre o codigo identificador del pasillo", example = "Pasillo A", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El nombre del pasillo no puede estar vacío")
    @Size(max = 50, message = "El nombre del pasillo no puede superar los 50 caracteres")
    private String nombrePasillo;
}