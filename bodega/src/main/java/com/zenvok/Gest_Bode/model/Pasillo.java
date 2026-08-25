package com.zenvok.Gest_Bode.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pasillo")
@Schema(description = "Entidad que representa un pasillo físico en la bodega")
public class Pasillo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pasillo")
    @Schema(description = "Identificador único del pasillo", example = "1")
    private Long idPasillo;

    @NotBlank(message = "El nombre del pasillo es obligatorio")
    @Column(name = "nombre_pasillo", nullable = false, length = 50)
    @Schema(description = "Nombre o código identificador del pasillo", example = "Pasillo A", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombrePasillo;
}