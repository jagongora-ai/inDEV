package com.zenvok.producto.model;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "producto")
@Schema(description = "Entidad que representa la tabla producto en la base de datos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador unico del producto generado automaticamente", example = "1")
    private long id;

    @Column(nullable = false, length = 100)
    @Schema(description = "Nombre del producto", example = "Teclado Mecanico RGB", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;

    @Column(length = 255)
    @Schema(description = "Descripcion detallada del producto", example = "Teclado con switches red y distribucion en español", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String descripcion;

    @Column(nullable = false)
    @Schema(description = "Precio unitario del producto", example = "24990.0", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal precio;

    @Column(nullable = false)
    @Schema(description = "Identificador del estado asignado al producto", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long estadoId;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    @Schema(description = "Categoria asociada a la que pertenece el producto", requiredMode = Schema.RequiredMode.REQUIRED)
    private Categoria categoria; 
}