package com.zenvok.Gest_Bode.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "prod_pas_est")
@Schema(description = "Entidad que representa la asignacion y ubicacion fisica de un producto especifico en una estructura de la bodega")
public class ProdPasEst {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_prod_est") 
    @Schema(description = "Identificador unico de la relacion producto-estante", example = "1")
    private Long idProdEst;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pas_est_id_pas_est", nullable = false)
    @Schema(description = "Estructura de pasillo-estante asignada para almacenar el producto")
    private Pas_Est pasEst;

    @Column(name = "producto_id_producto", nullable = false)
    @Schema(description = "Identificador unico del producto referenciado", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long productoId;
}