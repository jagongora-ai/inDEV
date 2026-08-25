package com.zenvok.Gest_Bode.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Modelo de respuesta para la asociacion entre un pasillo y un estante")
public class PasEstResponseDTO {
    
    @Schema(description = "Identificador unico de la relacion pasillo-estante", example = "1")
    private Long idPasEst;
    
    @Schema(description = "Identificador del pasillo", example = "1")
    private Long pasilloId;
    
    @Schema(description = "Nombre o codigo del pasillo", example = "Pasillo A")
    private String nombrePasillo;
    
    @Schema(description = "Identificador del estante", example = "2")
    private Long estanteId;
    
    @Schema(description = "Nombre o codigo del estante", example = "Estante 3")
    private String nombreEstante;
}