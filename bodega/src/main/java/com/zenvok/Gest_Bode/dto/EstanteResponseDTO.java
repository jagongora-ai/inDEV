package com.zenvok.Gest_Bode.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Modelo de respuesta con la informacion de un estante")
public class EstanteResponseDTO {
    
    @Schema(description = "Identificador unico del estante", example = "1")
    private Long idEstante;
    
    @Schema(description = "Nombre o codigo identificador del estante", example = "Estante A1")
    private String nombreEstante;
}