package co.edu.ucentral.petmap.resource;

import co.edu.ucentral.petmap.dto.publicacion.PublicacionEncontradaDTO;
import co.edu.ucentral.petmap.entity.Publicacion;
import co.edu.ucentral.petmap.service.PublicacionService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/publicaciones")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PublicacionResource {

    @Inject
    PublicacionService publicacionService;

    @POST
    @Path("/encontrada")
    public Response crearEncontrada(@Valid PublicacionEncontradaDTO dto) {
        Publicacion pub = publicacionService.crearEncontrada(dto);
        return Response.status(Response.Status.CREATED).entity(pub).build();
    }
}