package com.zenvok.envios.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "envios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa un envio registrado en el sistema")
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Envios")
    @Schema(description = "Identificador unico del envio", example = "1")
    private Long id;

    @Column(name = "F_Envios", nullable = false, length = 20)
    @Schema(description = "Fecha planificada o realizada del envio", example = "2026-07-05", requiredMode = Schema.RequiredMode.REQUIRED)
    private String fechaEnvio;

    @Column(name = "F_Embargue", nullable = false, length = 20)
    @Schema(description = "Fecha de embarque o salida de la carga", example = "2026-07-06", requiredMode = Schema.RequiredMode.REQUIRED)
    private String fechaEmbargue;

    @Column(name = "Comentarios", length = 350)
    @Schema(description = "Observaciones o anotaciones adicionales sobre la entrega", example = "Dejar en consergeria si no responde")
    private String comentarios;

    @Column(name = "Direccion_ID_Direccion", nullable = false, length = 60)
    @Schema(description = "Identificador de la direccion de destino", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long direccionId;

    @Column(name = "Venta_ID_Venta", nullable = false, length = 60)
    @Schema(description = "Identificador de la venta asociada al envio", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long ventaId;

    @Column(name = "Estado_ID_Estado", nullable = false, length = 60)
    @Schema(description = "Identificador del estado actual del envio", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long estadoId;
}