package co.com.zenway.usecase.usuario.exceptions;

public class DocumentoNoExiste extends RuntimeException{

    public DocumentoNoExiste(String mensaje){
        super(mensaje);
    }
}
