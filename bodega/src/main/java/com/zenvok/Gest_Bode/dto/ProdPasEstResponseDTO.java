package com.zenvok.Gest_Bode.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Modelo de respuesta con la informacion de ubicacion detallada de un producto en bodega")
public class ProdPasEstResponseDTO {
    
    @Schema(description = "Identificador unico de la relacion producto-estante", example = "1")
    private Long idProdEst;
    
    @Schema(description = "Identificador del producto ubicado", example = "1")
    private Long productoId;
    
    @Schema(description = "Identificador de la relacion estructural pasillo-estante", example = "1")
    private Long pasEstId;
    
    @Schema(description = "Nombre o codigo del pasillo donde se encuentra", example = "Pasillo A")
    private String nombrePasillo;
    
    @Schema(description = "Nombre o codigo del estante donde se encuentra", example = "Estante 3")
    private String nombreEstante;
}