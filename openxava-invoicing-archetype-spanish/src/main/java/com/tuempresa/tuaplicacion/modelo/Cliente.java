package com.tuempresa.tuaplicacion.modelo;
 
import javax.persistence.*;

import org.openxava.annotations.*;

import lombok.*;
 
@Entity  // Esto marca la clase Cliente como una entidad
@Getter @Setter // Esto hace los campos a continuaci�n p�blicamente accesibles
@View(name="Simple", // Esta vista solo se usar� cuando se especifique "Simple"
members="numero, nombre" // Muestra �nicamente numero y nombre en la misma l��nea
)
public class Cliente {
 
    @Id  // La propiedad numero es la clave.  Las claves son obligatorias (required) por defecto
    @Column(length=6)  // La longitud de columna se usa a nivel UI y a nivel DB
    int numero;
 
    @Column(length=50) // La longitud de columna se usa a nivel UI y a nivel DB
    @Required  // Se mostra� un error de validaci�n si la propiedad nombre se deja en blanco
    String nombre;
    
    @Embedded // As� para referenciar a una clase incrustable
    Direccion direccion; // Una referencia Java convencional
 
}