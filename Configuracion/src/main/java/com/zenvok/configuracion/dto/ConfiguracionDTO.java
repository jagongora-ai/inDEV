package com.zenvok.configuracion.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para la transferencia de datos de configuracion")
public class ConfiguracionDTO {

    public ConfiguracionDTO(String clave, String valor) {
        this.clave = clave;
        this.valor = valor;
    }

    @Schema(description = "ID unico de la configuracion", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "La clave de configuración es obligatoria")
    @Schema(description = "Nombre clave del parametro", example = "iva", requiredMode = Schema.RequiredMode.REQUIRED)
    private String clave;

    @NotBlank(message = "El valor de configuración es obligatorio")
    @Schema(description = "Valor asociado al parametro", example = "19", requiredMode = Schema.RequiredMode.REQUIRED)
    private String valor;
}