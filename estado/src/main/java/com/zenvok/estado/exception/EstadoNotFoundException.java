package com.zenvok.estado.exception;

public class EstadoNotFoundException extends RuntimeException{

    public EstadoNotFoundException(Long id){
        super("Estado no encontrado con id: " + id);
    }

}
