package co.edu.ucentral.petmap.dto.usuario;

public class UsuarioResponseDTO {
    public Long id;
    public String nombre;
    public String correo;
    public String telefono;
    public String rol;

    public UsuarioResponseDTO(Long id, String nombre, String correo, String telefono, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
        this.rol = rol;
    }
}