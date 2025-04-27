package org.example;

// Asegúrate de que el paquete sea correcto

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws IOException {
        GramaticaTokenManager lexer = null;
        int numeroLinea = 1;
        tablaSimbolos tablaSimbolos = new tablaSimbolos();

        try (BufferedReader br = new BufferedReader(new FileReader("Parser/src/Javacc/Txt_Prueba_AL.txt"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                procesarLinea(linea, numeroLinea, tablaSimbolos);
                numeroLinea++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String rutaHTML = "Parser\\src\\main\\java\\org\\example\\tabla_simbolos.html";
        ReporteHTML.generarTablaSimbolos(tablaSimbolos.getSimbolos(), rutaHTML);

        // Leer el contenido del archivo como un string
        BufferedReader br = new BufferedReader(new FileReader("Parser/src/Javacc/Txt_Prueba_AL.txt"));
        StringBuilder sb = new StringBuilder();
        String linea;
        while (true) {
            try {
                if (!((linea = br.readLine()) != null)) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            sb.append(linea).append("\n");
        }
        br.close();

        Reader reader = new StringReader(sb.toString()); // ahora sí contiene el código fuente real
        SimpleCharStream charStream = new SimpleCharStream(reader);
        lexer = new GramaticaTokenManager(charStream);
        Token t = null;

        while (true) {
            try {
                t = lexer.getNextToken();

                if (t.kind == 0) break; // EOF

                System.out.println("Token: " + t.image + " - Tipo: " + t.kind);
                ReporteHTML.agregarToken(String.valueOf(t.kind), t.image, t.beginLine, t.beginColumn);

            } catch (TokenMgrError e) {
                System.err.println("Error léxico detectado:");
                System.err.println(e.getMessage());


                //  Solución: avanzar el stream manualmente si JavaCC no lo hace automáticamente
                try {
                    ReporteHTML.agregarError(String.valueOf(e.getMessage()));
                    charStream.readChar(); // avanzar 1 carácter
                    lexer.ReInit(charStream); // reinicializar el lexer
                } catch (IOException ioEx) {
                    System.err.println("Error al avanzar el stream: " + ioEx.getMessage());
                    break; // No se puede continuar
                }

                continue;

                // sigue con el siguiente token
            } catch (Exception e) {
                System.err.println("Otro tipo de error durante el análisis:");
                e.printStackTrace();
                break;
            }

            //analisis sintactico
            try {
                if (parser == null) {
                    parser = new Gramatica(reader);
                } else {
                    parser.ReInit(reader);
                }
                parser.Inicio();
                System.out.println("Análisis sintáctico exitoso.");
            } catch (ParseException e) {
                System.err.println("Error sintáctico detectado:");
                System.err.println(e.getMessage());
                ReporteHTML.agregarError(e.getMessage());
            } catch (Exception e) {
                System.err.println("Otro tipo de error durante el análisis sintáctico:");
                e.printStackTrace();
            }


// Generar el archivo HTML al final

        }
        ReporteHTML.generarReporte();

        System.out.println("Análisis léxico terminado.");


    }

    static Gramatica parser = null;
    private static void procesarLinea (String linea,int numeroLinea, tablaSimbolos tablaSimbolos){
        Pattern pattern = Pattern.compile("(?:\\b(privado|publico|protegido)\\b\\s*)?(entero|flotante|doble|caracter|cadena|clase)\\s+([a-zA-Z_][a-zA-Z0-9_]*)(?:\\s*=\\s*([^;]+))?\\s*;");
        Matcher matcher = pattern.matcher(linea);

        while (matcher.find()) {
            String acceso = matcher.group(1); // Puede ser null
            String tipo = matcher.group(2);
            String nombre = matcher.group(3);
            String valor = matcher.group(4);
            int columna = matcher.start(3) + 1;

            if (valor == null) {
                valor = "null";
            }
            if (acceso == null) {
                acceso = "null";
            }

            tablaSimbolos.agregarSimbolo(nombre, tipo, valor, numeroLinea, columna, acceso);
        }
    }
}