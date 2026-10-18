package co.edu.ucentral.petmap.security;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {
    public static String hash(String contrasena) {
        return BCrypt.hashpw(contrasena, BCrypt.gensalt());
    }

    public static boolean verificar(String contrasena, String hash) {
        return BCrypt.checkpw(contrasena, hash);
    }
}