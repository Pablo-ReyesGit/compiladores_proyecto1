package org.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReporteHTML {
    private static final List<String> tokens = new ArrayList<>();
    private static final List<String> erroresLexicos = new ArrayList<>();

    public static void agregarToken(String tipo, String valor, int fila, int columna) {
        String tipotoken = obtenerDescripcionToken(Integer.parseInt(tipo));
        tokens.add("<tr><td>" + tipotoken + "</td><td>" + valor + "</td><td>" + fila + "</td><td>" + columna + "</td></tr>");
    }

    public static String obtenerDescripcionToken(int codigo) {
        if (codigo >= 13 && codigo <= 71) {
            return "Palabra clave";
        } else if (codigo >= 72 && codigo <= 81) {
            return "Palabra sensible al contexto";
        } else if (codigo >= 82 && codigo <= 84) {
            return "Valores literales reservados";
        } else if (codigo >= 85 && codigo <= 94) {
            return "Signos de puntuación";
        } else if (codigo >= 95 && codigo <= 99) {
            return "Operador aritmético";
        } else if (codigo == 100) {
            return "Signos de asignación";
        } else if (codigo >= 101 && codigo <= 103) {
            return "Signos unarios";
        } else if (codigo >= 104 && codigo <= 108) {
            return "Signos aritméticos combinados";
        } else if (codigo >= 109 && codigo <= 114) {
            return "Signos relacionales";
        } else if (codigo >= 115 && codigo <= 120) {
            return "Signos lógicos o booleanos";
        } else if (codigo >= 121 && codigo <= 122) {
            return "Desplazamientos";
        } else if (codigo == 123) {
            return "Identificadores";
        } else if (codigo == 124) {
            return "Numérico";
        } else if (codigo == 125) {
            return "Cadena de texto";
        } else if (codigo == 0) {
            return "Default";
        } else if (codigo == 1) {
            return "Comentario";
        }
        return "Error*";
    }

    public static void agregarError( String mensaje) {
        erroresLexicos.add("<tr><td>" + mensaje + "</td></tr>");
    }

    public static void generarReporte() {
        generarArchivo("Parser (1)/Parser/src/main/java/org/example/tokens.html", "Tokens Reconocidos", tokens, "Token", "Valor", "Fila", "Columna");
        generarArchivo("Parser (1)/Parser/src/main/java/org/example/errores_lexicos.html", "Errores Léxicos", erroresLexicos,  "Mensaje");
    }

    private static void generarArchivo(String nombre, String titulo, List<String> contenido, String... encabezados) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombre))) {
            writer.write("<html><head><title>" + titulo + "</title>");
            writer.write("<style>body { font-family: Arial, sans-serif; } table { width: 100%; border-collapse: collapse; } th, td { border: 1px solid black; padding: 8px; text-align: left; } th { background-color: #f2f2f2; }</style>");
            writer.write("</head><body>");
            writer.write("<h1>" + titulo + "</h1>");
            writer.write("<table><tr>");

            for (String enc : encabezados) {
                writer.write("<th>" + enc + "</th>");
            }
            writer.write("</tr>");

            for (String linea : contenido) {
                writer.write(linea);
            }

            writer.write("</table></body></html>");
            System.out.println("Archivo " + nombre + " generado correctamente.");
        } catch (IOException e) {
            System.err.println("Error al generar el archivo HTML: " + nombre);
            e.printStackTrace();
        }
    }

    public static void generarTablaSimbolos(Map<String, Simbolo> tablaSimbolos, String rutaArchivo) {
        try (FileWriter writer = new FileWriter(rutaArchivo)) {
            writer.write("<html><head><title>Tabla de Símbolos</title>");
            writer.write("<style>");
            writer.write("body { font-family: Arial, sans-serif; background-color: #f2f2f2; padding: 20px; }");
            writer.write("h2 { text-align: center; color: #333; }");
            writer.write("table { border-collapse: collapse; margin: 0 auto; width: 90%; background-color: #fff; }");
            writer.write("th, td { border: 1px solid #999; padding: 8px 12px; text-align: center; }");
            writer.write("th { background-color: #4CAF50; color: white; }");
            writer.write("tr:nth-child(even) { background-color: #f9f9f9; }");
            writer.write("</style>");
            writer.write("</head><body>");
            writer.write("<h2>Tabla de Símbolos</h2>");
            writer.write("<table>");
            writer.write("<tr><th>Nombre</th><th>Tipo</th><th>Valor</th><th>Línea</th><th>Columna</th><th>Acceso</th></tr>");

            for (Simbolo simbolo : tablaSimbolos.values()) {
                writer.write("<tr>");
                writer.write("<td>" + simbolo.getNombre() + "</td>");
                writer.write("<td>" + simbolo.getTipo() + "</td>");
                writer.write("<td>" + simbolo.getValor() + "</td>");
                writer.write("<td>" + simbolo.getLinea() + "</td>");
                writer.write("<td>" + simbolo.getColumna() + "</td>");
                writer.write("<td>" + simbolo.getAcceso() + "</td>");
                writer.write("</tr>");
            }

            writer.write("</table></body></html>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
