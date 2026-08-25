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
@Table(name = "pas_est")
@Schema(description = "Entidad que representa la relacion estructural entre un pasillo y un estante en la bodega")
public class Pas_Est {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pas_est")
    @Schema(description = "Identificador unico de la relacion pasillo-estante", example = "1")
    private Long idPasEst;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pasillo_id_pasillo", nullable = false)
    @Schema(description = "Pasillo asociado a la estructura de almacenamiento")
    private Pasillo pasillo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estante_id_estante", nullable = false)
    @Schema(description = "Estante asociado a la estructura de almacenamiento")
    private Estante estante;
}