package com.zenvok.gestionusuario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioRequestDTO {

    @NotBlank(message = "El Rut es obligatorio")
    @Size(min = 9, max = 15)
    @Schema(description = "RUT del usuario", example = "12345678-9", minLength = 9, maxLength = 15, requiredMode = Schema.RequiredMode.REQUIRED)
    private String rut;
    
    @NotBlank(message = "El nombre no puede estar vacío")
    @Schema(description = "Nombre del usuario", example = "Juan", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacío")
    @Schema(description = "Apellido del usuario", example = "Pérez", requiredMode = Schema.RequiredMode.REQUIRED)
    private String apellido;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Debe ser un formato de correo válido")
    @Schema(description = "Correo electronico del usuario", example = "juan.perez@zenvok.cl", requiredMode = Schema.RequiredMode.REQUIRED)
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    @Schema(description = "Contraseña del usuario. Debe tener al menos 8 caracteres.", example = "ClaveSegura123", minLength = 8, requiredMode = Schema.RequiredMode.REQUIRED)
    private String contrasena;
    
    @NotNull(message = "El ID del tipo de usuario es obligatorio")
    @Schema(description = "Identificador del tipo de usuario asociado", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long idTipoUsuario;
}
