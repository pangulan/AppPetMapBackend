package co.edu.ucentral.petmap.service;

import co.edu.ucentral.petmap.dto.publicacion.PublicacionEncontradaDTO;
import co.edu.ucentral.petmap.entity.*;
import co.edu.ucentral.petmap.repository.PublicacionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import java.time.LocalDateTime;
import co.edu.ucentral.petmap.dto.PublicacionEliminarDTO;

@ApplicationScoped
public class PublicacionService {

    @Inject
    PublicacionRepository publicacionRepository;

    @Transactional
    public Publicacion crearEncontrada(PublicacionEncontradaDTO dto) {
        Usuario autor = Usuario.findById(dto.autorId);
        if (autor == null) {
            throw new WebApplicationException("Usuario no encontrado", 404);
        }

        Publicacion pub = new Publicacion();
        pub.tipo = TipoPublicacion.ENCONTRADA;
        pub.especie = dto.especie;
        pub.descripcion = dto.descripcion;
        pub.foto = dto.foto;
        pub.latitud = dto.latitud;
        pub.longitud = dto.longitud;
        pub.fecha = dto.fecha;
        pub.estado = EstadoPublicacion.PENDIENTE;
        pub.autor = autor;

        publicacionRepository.persist(pub);
        return pub;
    }

    @Transactional
public void eliminar(Long publicacionId, PublicacionEliminarDTO dto) {
    Publicacion publicacion = publicacionRepository.findById(publicacionId);
    if (publicacion == null) {
        throw new WebApplicationException("Publicación no encontrada", 404);
    }

    if (publicacion.autor == null || !publicacion.autor.id.equals(dto.autorId)) {
        throw new WebApplicationException("Solo el autor puede eliminar esta publicación", 403);
    }

    if (Boolean.TRUE.equals(publicacion.eliminada)) {
        throw new WebApplicationException("La publicación ya fue eliminada", 409);
    }

    publicacion.eliminada = true;
    publicacion.motivoEliminacion = dto.motivo;
    publicacion.fechaEliminacion = LocalDateTime.now();
}
}