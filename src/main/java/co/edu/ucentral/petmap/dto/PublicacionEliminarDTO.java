package co.edu.ucentral.petmap.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PublicacionEliminarDTO {
    @NotBlank(message = "El motivo de eliminación es obligatorio")
    public String motivo;

    @NotNull
    public Long autorId;
}