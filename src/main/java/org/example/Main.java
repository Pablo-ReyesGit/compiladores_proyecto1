package org.example;

// Asegúrate de que el paquete sea correcto
import Javacc.*;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main {
    static Gramatica parser = null;

    public static void main(String[] args) throws IOException {
        int numeroLinea = 1;
        tablaSimbolos tablaSimbolos = new tablaSimbolos();

        // 1. Leer todo el archivo una sola vez
        String rutaArchivo = "Parser/src/Javacc/Txt_Prueba_AL.txt";
        String contenidoArchivo = leerArchivoComoString(rutaArchivo);

        // 2. Procesar tabla de símbolos
        try (BufferedReader br = new BufferedReader(new StringReader(contenidoArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                procesarLinea(linea, numeroLinea, tablaSimbolos);
                numeroLinea++;
            }
        }

        String rutaHTML = "Parser\\src\\main\\java\\org\\example\\tabla_simbolos.html";
        ReporteHTML.generarTablaSimbolos(tablaSimbolos.getSimbolos(), rutaHTML);

        // 3. Análisis léxico
        Reader readerLexico = new StringReader(contenidoArchivo);
        SimpleCharStream charStream = new SimpleCharStream(readerLexico);
        GramaticaTokenManager lexer = new GramaticaTokenManager(charStream);

        Token t;
        while (true) {
            try {
                t = lexer.getNextToken();
                if (t.kind == 0) break; // EOF

                System.out.println("Token: " + t.image + " - Tipo: " + t.kind);
                ReporteHTML.agregarToken(String.valueOf(t.kind), t.image, t.beginLine, t.beginColumn);

            } catch (TokenMgrError e) {
                System.err.println("Error léxico detectado:");
                System.err.println(e.getMessage());
                ReporteHTML.agregarError(e.getMessage());
                try {
                    charStream.readChar(); // avanzar
                    lexer.ReInit(charStream);
                } catch (IOException ioEx) {
                    System.err.println("No se pudo avanzar el stream");
                    break;
                }
            }
        }

        // 4. Análisis sintáctico
        Reader readerSintactico = new StringReader(contenidoArchivo); // << nuevo reader limpio
        parser = new Gramatica(readerSintactico);

        try {
            parser.Inicio();
            System.out.println("Análisis sintáctico exitoso.");
        } catch (ParseException e) {
            System.err.println("Error sintáctico detectado:");
            System.err.println(e.toString());
            ReporteHTML.agregarError(e.toString()); // << ahora sí agrega bien
        } catch (Exception e) {
            System.err.println("Otro error durante el análisis sintáctico:");
            e.printStackTrace();
        }

        // 5. Finalmente generar el HTML
        ReporteHTML.generarReporte();
        System.out.println("Análisis léxico y sintáctico terminados.");
    }

    private static String leerArchivoComoString(String ruta) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(ruta));
        StringBuilder sb = new StringBuilder();
        String linea;
        while ((linea = br.readLine()) != null) {
            sb.append(linea).append("\n");
        }
        br.close();
        return sb.toString();
    }

    private static void procesarLinea(String linea, int numeroLinea, tablaSimbolos tablaSimbolos) {
        Pattern pattern = Pattern.compile("(?:\\b(privado|publico|protegido)\\b\\s*)?(entero|flotante|doble|caracter|cadena|clase)\\s+([a-zA-Z_][a-zA-Z0-9_]*)(?:\\s*=\\s*([^;]+))?\\s*;");
        Matcher matcher = pattern.matcher(linea);

        while (matcher.find()) {
            String acceso = matcher.group(1);
            String tipo = matcher.group(2);
            String nombre = matcher.group(3);
            String valor = matcher.group(4);
            int columna = matcher.start(3) + 1;

            if (valor == null) valor = "null";
            if (acceso == null) acceso = "null";

            tablaSimbolos.agregarSimbolo(nombre, tipo, valor, numeroLinea, columna, acceso);
        }
    }
}