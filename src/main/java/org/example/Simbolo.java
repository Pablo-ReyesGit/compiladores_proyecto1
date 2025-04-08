package org.example;

public class Simbolo {
    private String nombre;
    private String tipo;
    private String valor;
    private int linea;
    private int columna;
    private String acceso;

    public Simbolo(String nombre, String tipo, String valor, int linea, int columna, String acceso) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.valor = valor;
        this.linea = linea;
        this.columna = columna;
        this.acceso = acceso;
    }

    // Getters
    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }
    public String getValor() { return valor; }
    public int getLinea() { return linea; }
    public int getColumna() { return columna; }
    public String getAcceso() { return acceso; }
}
