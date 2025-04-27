package org.example;

import java.util.HashMap;
import java.util.Map;

public class tablaSimbolos {
    private Map<String, Simbolo> simbolos;

    public tablaSimbolos() {
        this.simbolos = new HashMap<>();
    }

    /**
     * Agrega un símbolo a la tabla de símbolos.
     *
     * @param nombre  El nombre del símbolo.
     * @param tipo    El tipo de dato del símbolo.
     * @param valor   El valor del símbolo.
     * @param linea   La línea donde se declara el símbolo.
     * @param columna La columna donde se declara el símbolo.
     * @param acceso  El tipo de acceso del símbolo.
     */
    public void agregarSimbolo(String nombre, String tipo, String valor, int linea, int columna, String acceso) {
        // Validar si el símbolo ya existe
        if (simbolos.containsKey(nombre)) {
            throw new IllegalArgumentException("El símbolo '" + nombre + "' ya está declarado.");
        }

        Simbolo simbolo = new Simbolo(nombre, tipo, valor, linea, columna, acceso);
        simbolos.put(nombre, simbolo);
    }

    /**
     * Obtiene el mapa de símbolos.
     *
     * @return Un mapa que contiene los símbolos.
     */
    public Map<String, Simbolo> getSimbolos() {
        return simbolos;
    }

    /**
     * Busca un símbolo en la tabla de símbolos.
     *
     * @param nombre El nombre del símbolo a buscar.
     * @return El símbolo correspondiente al nombre, o null si no se encuentra.
     */
    public Simbolo buscarSimbolo(String nombre) {
        return simbolos.get(nombre);
    }
}
