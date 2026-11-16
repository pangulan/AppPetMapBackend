package co.edu.ucentral.petmap.resource;

import co.edu.ucentral.petmap.dto.PerfilEditarDTO;
import co.edu.ucentral.petmap.entity.Usuario;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.LinkedHashMap;
import java.util.Map;

@Path("/api/perfil")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"PUBLICADOR", "ADMINISTRADOR"})
public class PerfilResource {

    @Inject
    SecurityIdentity identity;

    @PUT
    @Transactional
    public Map<String, Object> actualizar(@Valid PerfilEditarDTO dto) {
        Usuario u = Usuario.find("correo", identity.getPrincipal().getName()).firstResult();
        if (u == null) {
            throw new WebApplicationException("Usuario no encontrado", 404);
        }
        u.nombre = dto.nombre.trim();
        String tel = dto.telefono == null ? "" : dto.telefono.trim();
        u.telefono = tel.isEmpty() ? null : tel;

        Map<String, Object> r = new LinkedHashMap<>();
        r.put("id", u.id);
        r.put("nombre", u.nombre);
        r.put("correo", u.correo);
        r.put("telefono", u.telefono);
        r.put("rol", u.rol.name());
        return r;
    }
}