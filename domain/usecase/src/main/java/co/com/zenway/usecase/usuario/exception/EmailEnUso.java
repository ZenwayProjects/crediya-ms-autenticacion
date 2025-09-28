package co.com.zenway.usecase.usuario.exception;

public class EmailEnUso extends RuntimeException{

    public EmailEnUso(String mensaje){
        super (mensaje);
    }
}
