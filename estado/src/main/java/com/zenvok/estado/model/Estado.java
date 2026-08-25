package com.zenvok.estado.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "estados")
@Schema(description = "Entidad que representa un estado en el sistema")
public class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del estado", example = "1")
    private Long id;

    @Column(nullable = false, length = 100)
    @Schema(description = "Nombre identificador del estado", example = "Activo", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;

    @Column(length = 255)
    @Schema(description = "Detalle o descripción sobre la utilidad del estado", example = "Indica que el recurso está disponible y operativo")
    private String descripcion;
}