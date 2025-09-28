package co.com.zenway.usecase.usuario.exception;

public class DocumentoDeIdentidadEnUso extends RuntimeException{

    public DocumentoDeIdentidadEnUso(String mensaje){
        super(mensaje);
    }
}
