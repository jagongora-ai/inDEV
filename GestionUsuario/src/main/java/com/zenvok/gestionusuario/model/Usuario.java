package com.zenvok.gestionusuario.model;

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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarios")
@Schema(description = "Entidad que representa un usuario del sistema")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único generado automáticamente por la BD", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(nullable = false, length = 12)
    @Schema(description = "RUT del usuario", example = "12345678-9", requiredMode = Schema.RequiredMode.REQUIRED)
    private String rut;

    @Column(nullable = false, length = 50)
    @Schema(description = "Nombres del usuario", example = "Juan Jose", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;

    @Column(nullable = false, length = 50)
    @Schema(description = "Apellidos del usuario", example = "Gonzalez Perez", requiredMode = Schema.RequiredMode.REQUIRED)
    private String apellido;

    @Column(nullable = false, unique = true)
    @Schema(description = "Email del usuario", example = "juangonzales@zenvok.cl", requiredMode = Schema.RequiredMode.REQUIRED)
    private String correo;

    @Column(nullable = false)
    @Schema(description = "Contraseña del usuario", example = "********", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contrasena;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_usuario", nullable = false)
    @Schema(description = "Tipo de usuario asociado", requiredMode = Schema.RequiredMode.REQUIRED)
    private TipoUsuario tipoUsuario;
}