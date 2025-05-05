package org.example;

import Javacc.*;
import java.util.Hashtable;
import java.util.ArrayList;

public class TablaSimbolo {
    // Variable para validar asignaciones a caracteres
    public static int segunda = 0;

    // Tabla que almacenará los tokens declarados
    private static Hashtable<String, Integer> tabla = new Hashtable<>();

    // Listas que guardan los tipos compatibles de las variables
    private static ArrayList<Integer> intComp = new ArrayList<>();
    private static ArrayList<Integer> decComp = new ArrayList<>();
    private static ArrayList<Integer> strComp = new ArrayList<>();
    private static ArrayList<Integer> chrComp = new ArrayList<>();

    // Método que inicializa las tablas de tipos compatibles
    public static void SetTables() {
        intComp.add(39);  // INT
        decComp.add(39);  // INT
        decComp.add(26);  // DOUBLE
        decComp.add(32);  // FLOAT
        chrComp.add(20);  // CHAR
        strComp.add(57);  // STRING
    }

    /**
     * Agrega un símbolo a la tabla de tokens con su tipo correspondiente.
     *
     * @param identificador El token que representa el identificador.
     * @param tipo El tipo de dato correspondiente.
     */
    public static void agregarSimbolo(Token identificador, int tipo) {
        tabla.put(identificador.image, tipo);
    }

    /**
     * Verifica si una asignación es compatible entre dos variables o un valor.
     *
     * @param TokenIzq El token de la variable a la izquierda de la asignación.
     * @param TokenAsig El token del valor a asignar.
     * @return Un mensaje de error si no es compatible, o " " si es compatible.
     */
    public static String checkAsing(Token TokenIzq, Token TokenAsig) {
        int tipoIdent1;
        int tipoIdent2;

        // Se obtiene el tipo de dato del identificador de la izquierda
        if (TokenIzq.kind != 48 && TokenIzq.kind != 50) {
            try {
                tipoIdent1 = tabla.get(TokenIzq.image);
            } catch (Exception e) {
                return "Error: El identificador " + TokenIzq.image + " No ha sido declarado \r\nLinea: " + TokenIzq.beginLine;
            }
        } else {
            tipoIdent1 = 0;
        }

        // Se obtiene el tipo de dato del valor a la derecha
        if (TokenAsig.kind == 49) {
            try {
                tipoIdent2 = tabla.get(TokenAsig.image);
            } catch (Exception e) {
                return "Error: El identificador " + TokenAsig.image + " No ha sido declarado \r\nLinea: " + TokenIzq.beginLine;
            }
        } else if (TokenAsig.kind == 48 || TokenAsig.kind == 50 || TokenAsig.kind == 51 || TokenAsig.kind == 52) {
            tipoIdent2 = TokenAsig.kind;
        } else {
            tipoIdent2 = 0;
        }

        // Verificación de tipos según el tipo de dato de la izquierda
        if (tipoIdent1 == 44) { // INT
            if (intComp.contains(tipoIdent2))
                return " ";
            else
                return "Error: No se puede convertir " + TokenAsig.image + " a Entero \r\nLinea: " + TokenIzq.beginLine;
        } else if (tipoIdent1 == 45) { // DOUBLE
            if (decComp.contains(tipoIdent2))
                return " ";
            else
                return "Error: No se puede convertir " + TokenAsig.image + " a Decimal \r\nLinea: " + TokenIzq.beginLine;
        } else if (tipoIdent1 == 46) { // CHAR
            segunda++;
            if (segunda < 2) {
                if (chrComp.contains(tipoIdent2))
                    return " ";
                else
                    return "Error: No se puede convertir " + TokenAsig.image + " a Caracter \r\nLinea: " + TokenIzq.beginLine;
            } else {
                return "Error: No se puede asignar más de un valor a un carácter \r\nLinea: " + TokenIzq.beginLine;
            }
        } else if (tipoIdent1 == 47) { // STRING
            if (strComp.contains(tipoIdent2))
                return " ";
            else
                return "Error: No se puede convertir " + TokenAsig.image + " a Cadena \r\nLinea: " + TokenIzq.beginLine;
        } else {
            return "El Identificador " + TokenIzq.image + " no ha sido declarado \r\nLinea: " + TokenIzq.beginLine;
        }
    }

    /**
     * Verifica si una variable ha sido declarada.
     *
     * @param checkTok El token de la variable a verificar.
     * @return Un mensaje de error si la variable no ha sido declarada, o " " si es válida.
     */
    public static String checkVariable(Token checkTok) {
        try {
            // Verifica si el token de la variable está en la tabla de símbolos
            int tipoIdent1 = tabla.get(checkTok.image);
            return " ";
        } catch (Exception e) {
            return "Error: El identificador " + checkTok.image + " No ha sido declarado \r\nLinea: " + checkTok.beginLine;
        }
    }
}
