package co.com.zenway.usecase.usuario.utils;

public final class Constantes {

    private Constantes() {
        throw new IllegalStateException("Clase de utilidades");
    }

    public static final String EMAIL_EN_USO = "El email que ingresa ya esta en uso";
    public static final String DOCUMENTO_IDENTIDAD_EN_USO = "El documento de identidad que ingresa ya esta en uso";
    public static final String DOCUMENTO_IDENTIDAD_NO_REGISTRADO = "El documento de identidad de la solicitud no esta registrado";
}
