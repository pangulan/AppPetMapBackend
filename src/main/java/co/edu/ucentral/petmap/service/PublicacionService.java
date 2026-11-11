package co.edu.ucentral.petmap.service;

import co.edu.ucentral.petmap.dto.PublicacionEditarDTO;
import co.edu.ucentral.petmap.dto.PublicacionEncontradaDTO;
import co.edu.ucentral.petmap.dto.PublicacionPerdidaDTO;
import co.edu.ucentral.petmap.dto.PublicacionEliminarDTO;
import co.edu.ucentral.petmap.dto.PublicacionResueltaDTO;
import co.edu.ucentral.petmap.entity.*;
import co.edu.ucentral.petmap.repository.PublicacionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import java.time.LocalDateTime;
import java.util.List;
import co.edu.ucentral.petmap.dto.PublicacionResumenDTO;
import co.edu.ucentral.petmap.dto.PublicacionCerradaDTO;
import co.edu.ucentral.petmap.dto.PublicacionModeracionDTO;
import co.edu.ucentral.petmap.dto.PublicacionMapaDTO;

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
    public Publicacion crearPerdida(PublicacionPerdidaDTO dto) {
        Usuario autor = Usuario.findById(dto.autorId);
        if (autor == null) {
            throw new WebApplicationException("Usuario no encontrado", 404);
        }
        Publicacion pub = new Publicacion();
        pub.tipo = TipoPublicacion.PERDIDA;
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

    @Transactional
    public Publicacion editarPublicacion(Long id, PublicacionEditarDTO dto) {
        Publicacion pub = publicacionRepository.findById(id);
        if (pub == null) {
            throw new WebApplicationException("Publicacion no encontrada", 404);
        }
        if (pub.estado != EstadoPublicacion.PENDIENTE && pub.estado != EstadoPublicacion.APROBADA) {
            throw new WebApplicationException("No se puede editar en este estado", 409);
        }
        if (dto.descripcion != null) pub.descripcion = dto.descripcion;
        if (dto.foto != null) pub.foto = dto.foto;
        if (dto.latitud != null) pub.latitud = dto.latitud;
        if (dto.longitud != null) pub.longitud = dto.longitud;
        return pub;
    }

    @Transactional
    public Publicacion marcarResuelta(Long id, PublicacionResueltaDTO dto) {
        Publicacion pub = publicacionRepository.findById(id);
        if (pub == null) {
            throw new WebApplicationException("Publicacion no encontrada", 404);
        }
        if (pub.tipo != TipoPublicacion.PERDIDA) {
            throw new WebApplicationException("Solo aplica a publicaciones de tipo PERDIDA", 409);
        }
        if (pub.autor == null || !pub.autor.id.equals(dto.autorId)) {
            throw new WebApplicationException("Solo el autor puede marcar esta publicación como resuelta", 403);
        }
        pub.estado = EstadoPublicacion.RESUELTA;
        return pub;
    }

    public List<PublicacionResumenDTO> listarPorAutor(Long autorId) {
        return publicacionRepository
                .list("autor.id = ?1 and (eliminada is null or eliminada = false) order by id desc",
                        autorId)
                .stream()
                .map(PublicacionResumenDTO::desde)
                .toList();
    }

    @Transactional
    public void marcarCerrada(Long id, PublicacionCerradaDTO dto) {
        Publicacion pub = publicacionRepository.findById(id);
        if (pub == null || Boolean.TRUE.equals(pub.eliminada)) {
            throw new WebApplicationException("Publicacion no encontrada", 404);
        }
        if (pub.tipo != TipoPublicacion.ENCONTRADA) {
            throw new WebApplicationException("Solo aplica a publicaciones de tipo ENCONTRADA", 409);
        }
        if (pub.autor == null || !pub.autor.id.equals(dto.autorId)) {
            throw new WebApplicationException("Solo el autor puede cerrar esta publicacion", 403);
        }
        if (pub.estado != EstadoPublicacion.PENDIENTE && pub.estado != EstadoPublicacion.APROBADA) {
            throw new WebApplicationException("No se puede cerrar en este estado", 409);
        }
        pub.estado = EstadoPublicacion.CERRADA;
    }

    @Transactional
    public Publicacion aprobar(Long id, PublicacionModeracionDTO dto) {
        Publicacion pub = buscarPendiente(id, dto);
        pub.estado = EstadoPublicacion.APROBADA;
        return pub;
    }

    @Transactional
    public Publicacion rechazar(Long id, PublicacionModeracionDTO dto) {
        if (dto.motivo == null || dto.motivo.isBlank()) {
            throw new WebApplicationException("El motivo del rechazo es obligatorio", 400);
        }
        Publicacion pub = buscarPendiente(id, dto);
        pub.estado = EstadoPublicacion.RECHAZADA;
        pub.motivoRechazo = dto.motivo;
        return pub;
    }

    private Publicacion buscarPendiente(Long id, PublicacionModeracionDTO dto) {
        Usuario moderador = Usuario.findById(dto.moderadorId);
        if (moderador == null) {
            throw new WebApplicationException("Moderador no encontrado", 404);
        }
        Publicacion pub = publicacionRepository.findById(id);
        if (pub == null) {
            throw new WebApplicationException("Publicacion no encontrada", 404);
        }
        if (pub.estado != EstadoPublicacion.PENDIENTE) {
            throw new WebApplicationException("Solo se pueden moderar publicaciones pendientes", 409);
        }
        return pub;
    }

    public java.util.List<java.util.Map<String, Object>> listarPendientes() {
        java.util.List<Publicacion> lista = publicacionRepository.list(
                "estado = ?1 and (eliminada is null or eliminada = false) order by id",
                EstadoPublicacion.PENDIENTE);
        java.util.List<java.util.Map<String, Object>> resultado = new java.util.ArrayList<>();
        for (Publicacion p : lista) {
            java.util.Map<String, Object> item = new java.util.LinkedHashMap<>();
            item.put("id", p.id);
            item.put("tipo", p.tipo);
            item.put("especie", p.especie);
            item.put("descripcion", p.descripcion);
            item.put("foto", p.foto);
            item.put("estado", p.estado);
            resultado.add(item);
        }
        return resultado;
    }

    public List<PublicacionMapaDTO> listarAprobadas() {
        return publicacionRepository
                .list("estado = ?1 and (eliminada is null or eliminada = false) order by id desc",
                        EstadoPublicacion.APROBADA)
                .stream()
                .map(PublicacionMapaDTO::desde)
                .toList();
    }

    public String fotoDe(Long id) {
        Publicacion pub = publicacionRepository.findById(id);
        if (pub == null
                || pub.estado != EstadoPublicacion.APROBADA
                || Boolean.TRUE.equals(pub.eliminada)
                || pub.foto == null
                || pub.foto.isBlank()) {
            throw new WebApplicationException("Foto no disponible", 404);
        }
        return pub.foto;
    }
        @Transactional
    public void retirar(Long id, PublicacionModeracionDTO dto) {
        if (dto.motivo == null || dto.motivo.isBlank()) {
            throw new WebApplicationException("El motivo del retiro es obligatorio", 400);
        }
        Publicacion pub = publicacionRepository.findById(id);
        if (pub == null || Boolean.TRUE.equals(pub.eliminada)) {
            throw new WebApplicationException("Publicacion no encontrada", 404);
        }
        if (pub.estado != EstadoPublicacion.APROBADA) {
            throw new WebApplicationException("Solo se pueden retirar publicaciones aprobadas", 409);
        }
        pub.eliminada = true;
        pub.motivoEliminacion = "Retirada por un administrador: " + dto.motivo;
        pub.fechaEliminacion = LocalDateTime.now();
    }

    public java.util.Map<String, Long> resumen() {
        String activa = "(eliminada is null or eliminada = false)";
        java.util.Map<String, Long> r = new java.util.LinkedHashMap<>();
        r.put("pendientes", publicacionRepository.count(
                "estado = ?1 and " + activa, EstadoPublicacion.PENDIENTE));
        r.put("aprobadas", publicacionRepository.count(
                "estado = ?1 and " + activa, EstadoPublicacion.APROBADA));
        r.put("perdidas", publicacionRepository.count(
                "estado = ?1 and tipo = ?2 and " + activa,
                EstadoPublicacion.APROBADA, TipoPublicacion.PERDIDA));
        r.put("encontradas", publicacionRepository.count(
                "estado = ?1 and tipo = ?2 and " + activa,
                EstadoPublicacion.APROBADA, TipoPublicacion.ENCONTRADA));
        r.put("rechazadas", publicacionRepository.count(
                "estado = ?1 and " + activa, EstadoPublicacion.RECHAZADA));
        r.put("resueltas", publicacionRepository.count(
                "(estado = ?1 or estado = ?2) and " + activa,
                EstadoPublicacion.RESUELTA, EstadoPublicacion.CERRADA));
        return r;
    }

        public java.util.Map<String, String> contactoDe(Long id) {
        Publicacion pub = publicacionRepository.findById(id);
        if (pub == null
                || pub.estado != EstadoPublicacion.APROBADA
                || Boolean.TRUE.equals(pub.eliminada)
                || pub.autor == null) {
            throw new WebApplicationException("Contacto no disponible", 404);
        }
        java.util.Map<String, String> contacto = new java.util.LinkedHashMap<>();
        contacto.put("nombre", pub.autor.nombre);
        contacto.put("telefono", pub.autor.telefono);
        contacto.put("correo", pub.autor.correo);
        return contacto;
    }
    
}