package co.edu.ucentral.petmap.repository;

import co.edu.ucentral.petmap.entity.Publicacion;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PublicacionRepository implements PanacheRepository<Publicacion> {
}