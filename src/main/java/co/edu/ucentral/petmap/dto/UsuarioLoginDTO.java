package co.edu.ucentral.petmap.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UsuarioLoginDTO {
    @NotBlank
    @Email
    public String correo;

    @NotBlank
    public String contrasena;
}