package co.com.zenway.model.usuario.gateways;

public interface PasswordEncoder {

    String encode(String password);

    boolean matches(String password, String encodedPassword);
}
