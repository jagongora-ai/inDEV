package com.zenvok.envios.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Modelo de respuesta externa con la información detallada de una dirección")
public class DireccionResponse {
    @Schema(description = "Identificador único de la dirección", example = "1")
    private Long id;
    @Schema(description = "Nombre de la calle o avenida", example = "Avenida Vitacura")
    private String calle;
    @Schema(description = "Numeración o altura del domicilio", example = "1234")
    private String numeracion;
    @Schema(description = "Block, torre o departamento si aplica", example = "Depto 402")
    private String block;

    @Schema(description = "Identificador único del estado asociado", example = "1")
    private Long Estado_ID_Estado;
    @Schema(description = "Identificador único de la comuna asociada", example = "15")
    private Long Comunas_ID_Comunas;
    @Schema(description = "Identificador único del usuario dueño de la dirección", example = "42")
    private Long Usuarios_ID_Usuarios;
}