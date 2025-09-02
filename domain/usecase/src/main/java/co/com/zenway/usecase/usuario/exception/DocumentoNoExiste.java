package co.com.zenway.usecase.usuario.exception;

public class DocumentoNoExiste extends RuntimeException{

    public DocumentoNoExiste(String mensaje){
        super(mensaje);
    }
}
