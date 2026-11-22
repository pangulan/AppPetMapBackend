package co.edu.ucentral.petmap.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PerfilEditarDTO {
    @NotBlank
    @Size(max = 100)
    public String nombre;

    @Size(max = 30)
    public String telefono;
}