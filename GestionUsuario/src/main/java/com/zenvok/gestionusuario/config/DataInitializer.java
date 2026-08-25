package com.zenvok.gestionusuario.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.zenvok.gestionusuario.model.TipoUsuario;
import com.zenvok.gestionusuario.model.Usuario;
import com.zenvok.gestionusuario.repository.TipoUsuarioRepository;
import com.zenvok.gestionusuario.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final TipoUsuarioRepository tipoUsuarioRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (tipoUsuarioRepository.count() == 0) {
            log.info("Cargando tipos de usuario iniciales");

            TipoUsuario admin = tipoUsuarioRepository.save(new TipoUsuario(null, "Administrador"));
            TipoUsuario bodeguero = tipoUsuarioRepository.save(new TipoUsuario(null, "Bodeguero"));
            TipoUsuario repartidor = tipoUsuarioRepository.save(new TipoUsuario(null, "Repartidor"));
            TipoUsuario cliente = tipoUsuarioRepository.save(new TipoUsuario(null, "Cliente"));

            log.info("Tipos de usuario cargados correctamente");

            if (usuarioRepository.count() == 0) {
                crearUsuarioInicial("12345678-9", "Adbeel", "Rodriguez", "adbeel@test.com", "12345678", admin);
                crearUsuarioInicial("98765432-1", "Camilo", "Jimenez", "camilo@test.com", "12345678", bodeguero);
                crearUsuarioInicial("11111111-1", "Javier", "Dumfer", "javier@test.com", "12345678", cliente);
                crearUsuarioInicial("22222222-2", "Mario", "Repartidor", "mario@test.com", "12345678", repartidor);
                log.info("Usuarios iniciales cargados correctamente");
            }

            return;
        }

        if (usuarioRepository.count() == 0) {
            log.info("Tipos ya existentes. Cargando usuarios iniciales");

            TipoUsuario admin = tipoUsuarioRepository.findById(1L)
                    .orElseThrow(() -> new RuntimeException("No existe TipoUsuario con ID 1"));

            TipoUsuario bodeguero = tipoUsuarioRepository.findById(2L)
                    .orElseThrow(() -> new RuntimeException("No existe TipoUsuario con ID 2"));

            TipoUsuario repartidor = tipoUsuarioRepository.findById(3L)
                    .orElseThrow(() -> new RuntimeException("No existe TipoUsuario con ID 3"));

            TipoUsuario cliente = tipoUsuarioRepository.findById(4L)
                    .orElseThrow(() -> new RuntimeException("No existe TipoUsuario con ID 4"));

            crearUsuarioInicial("12345678-9", "Adbeel", "Rodriguez", "adbeel@test.com", "12345678", admin);
            crearUsuarioInicial("98765432-1", "Camilo", "Jimenez", "camilo@test.com", "12345678", bodeguero);
            crearUsuarioInicial("11111111-1", "Javier", "Dumfer", "javier@test.com", "12345678", cliente);
            crearUsuarioInicial("22222222-2", "Mario", "Repartidor", "mario@test.com", "12345678", repartidor);

            log.info("Usuarios iniciales cargados correctamente");
        } else {
            log.info("Usuarios y tipos de usuario ya existen. Se omite inicialización");
        }
    }

    private void crearUsuarioInicial(String rut, String nombre, String apellido, String correo, String contrasena, TipoUsuario tipoUsuario) {
        Usuario usuario = new Usuario();
        usuario.setRut(rut);
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setCorreo(correo);
        usuario.setContrasena(passwordEncoder.encode(contrasena));
        usuario.setTipoUsuario(tipoUsuario);
        usuarioRepository.save(usuario);
    }
}