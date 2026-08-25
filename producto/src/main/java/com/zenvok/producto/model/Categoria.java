package com.zenvok.producto.model;

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
@Table(name = "categoria")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Entidad que representa la tabla categoria en la base de datos")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador unico de la categoria generado automaticamente", example = "1")
    private Long id;
    
    @Column(nullable = false, length = 100)
    @Schema(description = "Nombre de la categoria", example = "Tecnologia", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;

    @Column(length=255)
    @Schema(description = "Descripcion de la categoria", example = "Dispositivos electronicos y accesorios tecnologicos", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String descripcion;
}