package co.edu.ucentral.petmap.dto.usuario;

import jakarta.validation.constraints.*;

public class UsuarioRegistroDTO {
    @NotBlank
    public String nombre;

    @NotBlank
    @Email
    public String correo;

    @NotBlank
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    public String contrasena;

    public String telefono;
}