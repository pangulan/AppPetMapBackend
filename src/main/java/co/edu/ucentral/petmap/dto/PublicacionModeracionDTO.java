package co.edu.ucentral.petmap.dto;
import jakarta.validation.constraints.NotNull;
public class PublicacionModeracionDTO {


    @NotNull(message = "El moderador es obligatorio")
    public Long moderadorId;

    public String motivo;
}
