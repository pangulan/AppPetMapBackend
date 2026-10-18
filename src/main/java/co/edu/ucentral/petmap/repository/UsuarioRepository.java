package co.edu.ucentral.petmap.repository;

import co.edu.ucentral.petmap.entity.Usuario;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class UsuarioRepository implements PanacheRepository<Usuario> {

    public Optional<Usuario> buscarPorCorreo(String correo) {
        return find("correo", correo).firstResultOptional();
    }
}