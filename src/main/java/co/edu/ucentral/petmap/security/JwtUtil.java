package co.edu.ucentral.petmap.security;

import co.edu.ucentral.petmap.entity.Usuario;
import io.smallrye.jwt.build.Jwt;
import java.time.Duration;

public class JwtUtil {
    public static String generarToken(Usuario usuario) {
        return Jwt.issuer("petmap-backend")
                .upn(usuario.correo)
                .groups(usuario.rol.name())
                .claim("id", usuario.id)
                .claim("nombre", usuario.nombre)
                .expiresIn(Duration.ofHours(24))
                .sign();
    }
}