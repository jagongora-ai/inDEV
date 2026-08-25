package com.zenvok.Ventas.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Detalle")
@Schema(description = "Entidad que representa el detalle de una boleta de Ventas.")
public class Detalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Detalle")
    @Schema(
        description = "Identificador único del registro de detalle que se autoincrementa.",
        example = "1"
    )
    private Long id;

    @NotNull(message = "El producto es obligatorio")
    @Column(name = "Producto_ID_Producto", nullable = false)
    @Schema(
        description = "Identificador del producto asociado a este detalle.",
        example = "102"
    )
    private Long productoId;

    @NotNull(message = "La cantidad es obligatoria")
    @Column(name = "Cantidad", nullable = false)
    @Schema(
        description = "Cantidad de unidades solicitadas del producto.",
        example = "3"
    )
    private Integer cantidad;

    @NotNull(message = "El subtotal no puede estar vacío")
    @Column(name = "Subtotal", nullable = false, precision = 10, scale = 2)
    @Schema(
        description = "Monto del subtotal.",
        example = "46500.00"
    )
    private BigDecimal subtotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Venta_ID_Venta", nullable = false) 
    @Schema(
        description = "Información de la venta principal a la que pertenece este detalle."
    )
    private Ventas venta;
}
