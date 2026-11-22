package co.edu.ucentral.petmap.resource;

import co.edu.ucentral.petmap.dto.ComentarioCrearDTO;
import co.edu.ucentral.petmap.entity.Comentario;
import co.edu.ucentral.petmap.entity.EstadoPublicacion;
import co.edu.ucentral.petmap.entity.Publicacion;
import co.edu.ucentral.petmap.entity.Usuario;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Path("/api/comentarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"PUBLICADOR", "ADMINISTRADOR"})
public class ComentarioResource {

    @Inject
    SecurityIdentity identity;

    private String correoActual() {
        return identity.getPrincipal().getName();
    }

    private Usuario usuarioActual() {
        Usuario u = Usuario.find("correo", correoActual()).firstResult();
        if (u == null) {
            throw new WebApplicationException("Usuario no encontrado", 401);
        }
        return u;
    }

    private Publicacion publicacionVisible(Long id) {
        Publicacion pub = Publicacion.findById(id);
        if (pub == null || Boolean.TRUE.equals(pub.eliminada)) {
            throw new WebApplicationException("Publicacion no encontrada", 404);
        }
        if (pub.estado != EstadoPublicacion.APROBADA) {
            throw new WebApplicationException(
                    "Solo se puede comentar en publicaciones aprobadas", 409);
        }
        return pub;
    }

    private Map<String, Object> aMapa(Comentario c, String correo) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", c.id);
        m.put("autor", c.autor.nombre);
        m.put("texto", c.texto);
        m.put("fecha", c.fecha.toString());
        m.put("mio", correo.equals(c.autor.correo));
        return m;
    }

    @GET
    public List<Map<String, Object>> listar(@QueryParam("publicacionId") Long publicacionId) {
        if (publicacionId == null) {
            throw new BadRequestException("publicacionId es obligatorio");
        }
        publicacionVisible(publicacionId);
        String correo = correoActual();
        List<Comentario> lista = Comentario.list("publicacion.id = ?1 order by id", publicacionId);
        List<Map<String, Object>> resultado = new ArrayList<>();
        for (Comentario c : lista) {
            resultado.add(aMapa(c, correo));
        }
        return resultado;
    }

    @POST
    @Transactional
    public Response crear(@Valid ComentarioCrearDTO dto) {
        Publicacion pub = publicacionVisible(dto.publicacionId);
        Comentario c = new Comentario();
        c.publicacion = pub;
        c.autor = usuarioActual();
        c.texto = dto.texto.trim();
        c.persist();
        return Response.status(Response.Status.CREATED).entity(aMapa(c, correoActual())).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response borrar(@PathParam("id") Long id) {
        Comentario c = Comentario.findById(id);
        if (c == null) {
            throw new WebApplicationException("Comentario no encontrado", 404);
        }
        boolean esAutor = c.autor != null && correoActual().equals(c.autor.correo);
        if (!esAutor && !identity.hasRole("ADMINISTRADOR")) {
            throw new WebApplicationException("No puedes borrar este comentario", 403);
        }
        c.delete();
        return Response.noContent().build();
    }
}