package com.zenvok.Gest_Bode.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Modelo de respuesta con la informacion de un pasillo")
public class PasilloResponseDTO {
    
    @Schema(description = "Identificador unico del pasillo", example = "1")
    private Long idPasillo;
    
    @Schema(description = "Nombre o codigo identificador del pasillo", example = "Pasillo A")
    private String nombrePasillo;
}