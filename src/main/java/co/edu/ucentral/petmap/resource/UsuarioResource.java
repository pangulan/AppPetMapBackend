package co.edu.ucentral.petmap.resource;

import co.edu.ucentral.petmap.dto.usuario.*;
import co.edu.ucentral.petmap.service.UsuarioService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioResource {

    @Inject
    UsuarioService usuarioService;

    @POST
    @Path("/registro")
    public Response registrar(@Valid UsuarioRegistroDTO dto) {
        UsuarioResponseDTO response = usuarioService.registrar(dto);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @POST
    @Path("/login")
    public Response login(@Valid UsuarioLoginDTO dto) {
        LoginResponseDTO response = usuarioService.login(dto);
        return Response.ok(response).build();
    }
}