package co.edu.ucentral.petmap.dto.usuario;

public class LoginResponseDTO {
    public String token;
    public UsuarioResponseDTO usuario;

    public LoginResponseDTO(String token, UsuarioResponseDTO usuario) {
        this.token = token;
        this.usuario = usuario;
    }
}