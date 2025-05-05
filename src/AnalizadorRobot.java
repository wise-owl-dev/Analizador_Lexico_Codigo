import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Analizador léxico para el control del brazo robótico Enfocado en leer,
 * limpiar, separar e identificar tokens sin reglas de escritura
 */
public class AnalizadorRobot {

    /**
     * Enumeración de los tipos de tokens
     */
    public enum TipoToken {
        PALABRA_R("Palabra_r"), // Robot
        IDENTIFICADOR("Identificador"),
        ACCION("Accion"), // iniciar, finalizar
        METODO("Metodo"), // base, cuerpo, garra, velocidad
        NUMERO("Numero"),
        IGUAL("Igual"),
        PARENTESIS_IZQ("Parentesis_Izq"),
        PARENTESIS_DER("Parentesis_Der"),
        LLAVE_IZQ("Llave_Izq"),
        LLAVE_DER("Llave_Der"),
        PUNTO("Punto"),
        DESCONOCIDO("Desconocido");

        private final String descripcion;

        TipoToken(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getDescripcion() {
            return descripcion;
        }
    }

    /**
     * Clase que representa un token
     */
    public static class Token {

        private TipoToken tipo;
        private String lexema;
        private String valor;
        private int linea;
        private int columna;

        public Token(TipoToken tipo, String lexema, int linea, int columna) {
            this.tipo = tipo;
            this.lexema = lexema;
            this.linea = linea;
            this.columna = columna;
            this.valor = "";
        }

        public Token(TipoToken tipo, String lexema, String valor, int linea, int columna) {
            this.tipo = tipo;
            this.lexema = lexema;
            this.valor = valor;
            this.linea = linea;
            this.columna = columna;
        }

        public TipoToken getTipo() {
            return tipo;
        }

        public String getLexema() {
            return lexema;
        }

        public String getValor() {
            return valor;
        }

        public int getLinea() {
            return linea;
        }

        public int getColumna() {
            return columna;
        }

        @Override
        public String toString() {
            return String.format("%-15s %-15s %-15s %-5d %-5d",
                    lexema, tipo.getDescripcion(), valor, linea, columna);
        }
    }

    /**
     * Analiza el código y devuelve una lista de tokens
     *
     * @param codigo Código fuente a analizar
     * @return Lista de tokens encontrados
     */
    public static List<Token> analizar(String codigo) {
        List<Token> tokens = new ArrayList<>();

        // Dividir el código en líneas para mantener el seguimiento de línea/columna
        String[] lineas = codigo.split("\\r?\\n");

        for (int numLinea = 0; numLinea < lineas.length; numLinea++) {
            String linea = lineas[numLinea];

            // Inicializar la posición de la columna
            int columna = 0;

            // Procesar cada token en la línea
            while (columna < linea.length()) {
                // Omitir espacios en blanco
                if (Character.isWhitespace(linea.charAt(columna))) {
                    columna++;
                    continue;
                }

                // Identificador para Robot
                if (columna == 0 && linea.length() >= 5 && linea.substring(0, 5).equalsIgnoreCase("Robot")) {
                    tokens.add(new Token(TipoToken.PALABRA_R, "Robot", numLinea + 1, columna + 1));
                    columna += 5;
                    continue;
                }

                // Identificador (r1, etc.)
                if (Character.isLetter(linea.charAt(columna)) || linea.charAt(columna) == '_') {
                    int inicio = columna;
                    while (columna < linea.length()
                            && (Character.isLetterOrDigit(linea.charAt(columna)) || linea.charAt(columna) == '_')) {
                        columna++;
                    }
                    String lexema = linea.substring(inicio, columna);
                    tokens.add(new Token(TipoToken.IDENTIFICADOR, lexema, numLinea + 1, inicio + 1));
                    continue;
                }

                // Punto (separador para métodos)
                if (linea.charAt(columna) == '.') {
                    tokens.add(new Token(TipoToken.PUNTO, ".", numLinea + 1, columna + 1));
                    columna++;

                    // Después del punto viene un método o acción
                    if (columna < linea.length() && Character.isLetter(linea.charAt(columna))) {
                        int inicio = columna;
                        while (columna < linea.length() &&
                                (Character.isLetterOrDigit(linea.charAt(columna)) || linea.charAt(columna) == '_')) {
                            columna++;
                        }
                        String metodo = linea.substring(inicio, columna);

                        // Determinar si es una acción o un método
                        if (metodo.equals("iniciar") || metodo.equals("finalizar") || metodo.equals("cerrarGarra")
                                || metodo.equals("abrirGarra")) {
                            tokens.add(new Token(TipoToken.ACCION, metodo, numLinea + 1, inicio + 1));
                        } else {
                            tokens.add(new Token(TipoToken.METODO, metodo, numLinea + 1, inicio + 1));
                        }
                    }
                    continue;
                }

                // Paréntesis
                if (linea.charAt(columna) == '(') {
                    tokens.add(new Token(TipoToken.PARENTESIS_IZQ, "(", numLinea + 1, columna + 1));
                    columna++;
                    continue;
                }

                if (linea.charAt(columna) == ')') {
                    tokens.add(new Token(TipoToken.PARENTESIS_DER, ")", numLinea + 1, columna + 1));
                    columna++;
                    continue;
                }

                // Llaves
                if (linea.charAt(columna) == '{') {
                    tokens.add(new Token(TipoToken.LLAVE_IZQ, "{", numLinea + 1, columna + 1));
                    columna++;
                    continue;
                }

                if (linea.charAt(columna) == '}') {
                    tokens.add(new Token(TipoToken.LLAVE_DER, "}", numLinea + 1, columna + 1));
                    columna++;
                    continue;
                }

                // Igual
                if (linea.charAt(columna) == '=') {
                    tokens.add(new Token(TipoToken.IGUAL, "=", numLinea + 1, columna + 1));
                    columna++;
                    continue;
                }

                // Número
                if (Character.isDigit(linea.charAt(columna))) {
                    int inicio = columna;
                    while (columna < linea.length() && Character.isDigit(linea.charAt(columna))) {
                        columna++;
                    }
                    String numero = linea.substring(inicio, columna);
                    tokens.add(new Token(TipoToken.NUMERO, numero, numero, numLinea + 1, inicio + 1));
                    continue;
                }

                // Si no coincide con ninguno de los patrones anteriores
                tokens.add(new Token(TipoToken.DESCONOCIDO, String.valueOf(linea.charAt(columna)), numLinea + 1,
                        columna + 1));
                columna++;
            }
        }

        return tokens;
    }

    /**
     * Procesa los tokens para mostrar la información en formato tabular
     *
     * @param tokens Lista de tokens a procesar
     * @return Lista de datos para la tabla (cada fila es un arreglo de objetos)
     */
    public static List<Object[]> procesarParaTabla(List<Token> tokens) {
        List<Object[]> filas = new ArrayList<>();

        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);

            // Caso especial para métodos que están seguidos de paréntesis vacíos (sin
            // parámetros)
            if (token.getTipo() == TipoToken.METODO
                    && i + 2 < tokens.size()
                    && tokens.get(i + 1).getTipo() == TipoToken.PARENTESIS_IZQ
                    && tokens.get(i + 2).getTipo() == TipoToken.PARENTESIS_DER) {

                filas.add(new Object[] {
                        token.getLexema() + "()",
                        token.getTipo().getDescripcion(),
                        "", // Sin valor
                        "No" // No tiene parámetros
                });
                i += 2; // Saltar los paréntesis
            } // Caso para métodos con valores (seguidos de igual y número)
            else if (token.getTipo() == TipoToken.METODO
                    && i + 2 < tokens.size()
                    && tokens.get(i + 1).getTipo() == TipoToken.IGUAL
                    && tokens.get(i + 2).getTipo() == TipoToken.NUMERO) {

                filas.add(new Object[] {
                        token.getLexema(),
                        token.getTipo().getDescripcion(),
                        tokens.get(i + 2).getLexema(), // El valor es el número
                        "Si" // Tiene parámetros
                });
                i += 2; // Saltar el igual y el número
            } // Caso general para otros tokens
            else if (token.getTipo() != TipoToken.PUNTO) { // Ignoramos los puntos para simplificar la salida
                filas.add(new Object[] {
                        token.getLexema(),
                        token.getTipo().getDescripcion(),
                        token.getValor(),
                        "" // No aplica parámetro
                });
            }
        }

        return filas;
    }

    /**
     * Método principal para probar el analizador léxico
     */
    public static void main(String[] args) {
        // Código de ejemplo para el brazo robótico
        String codigoEjemplo = "Robot r1\n"
                + "r1.iniciar()\n"
                + "r1.velocidad=50\n"
                + "r1.base=180\n"
                + "r1.cuerpo=45\n"
                + "r1.garra=90\n"
                + "r1.cerrarGarra()\n"
                + "r1.abrirGarra()\n"
                + "r1.finalizar()";

        // Analizar el código
        List<Token> tokens = analizar(codigoEjemplo);

        // Mostrar los tokens en formato crudo
        System.out.println("=== TOKENS CRUDOS ===");
        System.out.println(String.format("%-15s %-15s %-15s %-5s %-5s",
                "LEXEMA", "TIPO", "VALOR", "LINEA", "COL"));
        System.out.println("-------------------------------------------------------------------");
        for (Token token : tokens) {
            System.out.println(token);
        }

        // Mostrar los tokens procesados para una tabla
        System.out.println("\n=== TOKENS PROCESADOS PARA TABLA ===");
        System.out.println(String.format("%-15s %-15s %-15s %-15s",
                "TOKEN", "TIPO", "VALOR", "PARAMETRO"));
        System.out.println("-------------------------------------------------------------------");
        List<Object[]> filasTabla = procesarParaTabla(tokens);
        for (Object[] fila : filasTabla) {
            System.out.println(String.format("%-15s %-15s %-15s %-15s",
                    fila[0], fila[1], fila[2], fila[3]));
        }
    }
}
