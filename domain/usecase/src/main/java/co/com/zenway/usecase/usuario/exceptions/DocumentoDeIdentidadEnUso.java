package co.com.zenway.usecase.usuario.exceptions;

public class DocumentoDeIdentidadEnUso extends RuntimeException{

    public DocumentoDeIdentidadEnUso(String mensaje){
        super(mensaje);
    }
}
