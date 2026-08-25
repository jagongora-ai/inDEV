package com.zenvok.configuracion.model;

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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "configuraciones")
@Schema(description = "Modelo que representa una configuración del sistema")
public class Configuracion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único de la configuración", example = "1")
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    @Schema(description = "Nombre clave de la configuración", example = "iva")
    private String clave; 

    @Column(nullable = false, length= 255)
    @Schema(description = "Valor asociado a la clave", example = "19")
    private String valor;
}