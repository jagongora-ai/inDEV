package com.zenvok.gestionusuario.model;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tipos_usuarios")
@Schema(description = "Entidad que representa un tipo de usuario en el sistema")
public class TipoUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único generado automáticamente por la BD", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Pattern(regexp = "Administrador|Bodeguero|Repartidor|Cliente", message = "El tipo de usuario debe ser Administrador, Bodeguero, Repartidor o Cliente")
    @Column(nullable = false)
    @Schema(description = "Nombre del tipo de usuario", example = "Administrador", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombreTipo;

    @OneToMany(mappedBy = "tipoUsuario")
    @Schema(description = "Lista de usuarios asociados a este tipo de usuario", accessMode = Schema.AccessMode.READ_ONLY)
    private List<Usuario> usuarios;

    public TipoUsuario(Long id, String nombreTipo) {
        this.id = id;
        this.nombreTipo = nombreTipo;
    }
}