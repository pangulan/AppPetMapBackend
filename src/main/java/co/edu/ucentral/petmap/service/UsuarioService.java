package co.edu.ucentral.petmap.service;

import co.edu.ucentral.petmap.dto.usuario.*;
import co.edu.ucentral.petmap.entity.RolUsuario;
import co.edu.ucentral.petmap.entity.Usuario;
import co.edu.ucentral.petmap.repository.UsuarioRepository;
import co.edu.ucentral.petmap.security.JwtUtil;
import co.edu.ucentral.petmap.security.PasswordUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class UsuarioService {

    @Inject
    UsuarioRepository usuarioRepository;

    @Transactional
    public UsuarioResponseDTO registrar(UsuarioRegistroDTO dto) {
        if (usuarioRepository.buscarPorCorreo(dto.correo).isPresent()) {
            throw new WebApplicationException("El correo ya está registrado", Response.Status.CONFLICT);
        }

        Usuario usuario = new Usuario();
        usuario.nombre = dto.nombre;
        usuario.correo = dto.correo;
        usuario.contrasena = PasswordUtil.hash(dto.contrasena);
        usuario.telefono = dto.telefono;
        usuario.rol = RolUsuario.PUBLICADOR;

        usuarioRepository.persist(usuario);

        return toResponseDTO(usuario);
    }

    public LoginResponseDTO login(UsuarioLoginDTO dto) {
        Usuario usuario = usuarioRepository.buscarPorCorreo(dto.correo)
                .orElseThrow(() -> new WebApplicationException("Credenciales inválidas", Response.Status.UNAUTHORIZED));

        if (!PasswordUtil.verificar(dto.contrasena, usuario.contrasena)) {
            throw new WebApplicationException("Credenciales inválidas", Response.Status.UNAUTHORIZED);
        }

        String token = JwtUtil.generarToken(usuario);
        return new LoginResponseDTO(token, toResponseDTO(usuario));
    }

    private UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        return new UsuarioResponseDTO(usuario.id, usuario.nombre, usuario.correo, usuario.telefono, usuario.rol.name());
    }
}