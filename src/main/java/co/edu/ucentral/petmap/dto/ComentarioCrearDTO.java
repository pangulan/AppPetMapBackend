package co.edu.ucentral.petmap.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ComentarioCrearDTO {
    @NotNull
    public Long publicacionId;

    @NotBlank
    @Size(max = 300)
    public String texto;
}