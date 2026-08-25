package com.zenvok.Ventas.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Ventas")
@Schema(description = "Entidad que representa el registro de la Venta.")
public class Ventas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Venta")
    @Schema(
        description = "Identificador único de la venta el cual es autoincrementable.",
        example = "1"
    )
    private Long idVenta;

    @NotNull(message = "La fecha de venta no puede estar vacía")
    @Column(name = "Fecha_ventas", nullable = false)
    @Schema(
        description = "Fecha de venta es para poder llevar un monitoreo de fechas",
        example = "20/05/2026 = fecha"
    )
    private LocalDate fechaVentas;

    @NotNull(message = "El total no puede estar vacío")
    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    @Schema(
        description = "Precio del producto pedido.",
        example = "15500.0"
    )
    private BigDecimal total;

    @NotNull(message = "El usuario es obligatorio")
    @Column(name = "usuario_id", nullable = false)
    @Schema(
        description = "Identificador del usuario que realizó la compra.",
        example = "45"
    )
    private Long usuarioId;

    @NotNull(message = "El estado es obligatorio")
    @Column(name = "estado_id", nullable = false)
    @Schema(
        description="Con esto damos a entender en que estado esta producto",
        example = "ID:2 = Entregado"
    )
    private Long estadoId;
}
