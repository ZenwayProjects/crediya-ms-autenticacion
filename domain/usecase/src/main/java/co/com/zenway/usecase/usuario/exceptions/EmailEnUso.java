package co.com.zenway.usecase.usuario.exceptions;

public class EmailEnUso extends RuntimeException{

    public EmailEnUso(String mensaje){
        super (mensaje);
    }
}
