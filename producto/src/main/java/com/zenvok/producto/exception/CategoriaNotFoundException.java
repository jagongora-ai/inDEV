package com.zenvok.producto.exception;

public class CategoriaNotFoundException extends RuntimeException{

    public CategoriaNotFoundException(Long id){
        super("Categoria no encontrada con id:" + id);
    }
}
