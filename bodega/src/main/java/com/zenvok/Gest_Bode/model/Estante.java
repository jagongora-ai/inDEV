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
@Table(name = "Estante")
@Schema(description = "Entidad que representa un estante físico en la bodega")
public class Estante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estante")
    @Schema(description = "Identificador único del estante", example = "1")
    private Long idEstante;

    @NotBlank(message = "El nombre o código del estante es obligatorio")
    @Column(name = "nombre_estante", nullable = false, length = 50)
    @Schema(description = "Nombre o código identificador del estante", example = "Estante A1", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombreEstante;
}