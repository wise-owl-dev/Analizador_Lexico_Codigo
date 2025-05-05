import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Analizador léxico específico para código Java
 */
public class AnalizadorJava {

    // Lista de palabras clave de Java
    private static final Set<String> PALABRAS_CLAVE_JAVA = new HashSet<>(Arrays.asList(
            "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const",
            "continue", "default", "do", "double", "else", "enum", "extends", "final", "finally", "float",
            "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long", "native",
            "new", "package", "private", "protected", "public", "return", "short", "static", "strictfp", "super",
            "switch", "synchronized", "this", "throw", "throws", "transient", "try", "void", "volatile", "while",
            "true", "false", "null"));

    /**
     * Enumeración de los tipos de tokens para Java
     */
    public enum TipoToken {
        PALABRA_CLAVE("Palabra_Clave"),
        IDENTIFICADOR("Identificador"),
        OPERADOR("Operador"),
        DELIMITADOR("Delimitador"),
        LITERAL_STRING("String"),
        LITERAL_CHAR("Char"),
        LITERAL_NUM("Numero"),
        COMENTARIO("Comentario"),
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
     * Clase que representa un token de Java
     */
    public static class Token {
        private TipoToken tipo;
        private String lexema;
        private int linea;
        private int columna;

        public Token(TipoToken tipo, String lexema, int linea, int columna) {
            this.tipo = tipo;
            this.lexema = lexema;
            this.linea = linea;
            this.columna = columna;
        }

        public TipoToken getTipo() {
            return tipo;
        }

        public String getLexema() {
            return lexema;
        }

        public int getLinea() {
            return linea;
        }

        public int getColumna() {
            return columna;
        }

        @Override
        public String toString() {
            return String.format("%-20s %-15s %-5d %-5d",
                    lexema, tipo.getDescripcion(), linea, columna);
        }
    }

    /**
     * Analiza el código Java y devuelve una lista de tokens
     * 
     * @param codigo Código fuente a analizar
     * @return Lista de tokens encontrados
     */
    public static List<Token> analizar(String codigo) {
        List<Token> tokens = new ArrayList<>();

        // Agregar un espacio al final para facilitar el procesamiento
        codigo = codigo + " ";

        // Posición actual en el código
        int posicion = 0;
        int linea = 1;
        int columna = 1;

        // Procesamos el código carácter por carácter
        while (posicion < codigo.length()) {
            char c = codigo.charAt(posicion);

            // Salto de línea
            if (c == '\n') {
                linea++;
                columna = 1;
                posicion++;
                continue;
            }

            // Espacios en blanco
            if (Character.isWhitespace(c)) {
                posicion++;
                columna++;
                continue;
            }

            // Comentarios de una línea
            if (c == '/' && posicion + 1 < codigo.length() && codigo.charAt(posicion + 1) == '/') {
                int inicio = posicion;
                posicion += 2;
                columna += 2;

                while (posicion < codigo.length() && codigo.charAt(posicion) != '\n') {
                    posicion++;
                    columna++;
                }

                tokens.add(new Token(TipoToken.COMENTARIO,
                        codigo.substring(inicio, posicion), linea, columna - (posicion - inicio)));
                continue;
            }

            // Comentarios multilinea
            if (c == '/' && posicion + 1 < codigo.length() && codigo.charAt(posicion + 1) == '*') {
                int inicio = posicion;
                posicion += 2;
                columna += 2;

                int coloniaInicio = columna - 2;
                int lineaInicio = linea;

                boolean encontrado = false;
                while (posicion + 1 < codigo.length() && !encontrado) {
                    if (codigo.charAt(posicion) == '*' && codigo.charAt(posicion + 1) == '/') {
                        posicion += 2;
                        columna += 2;
                        encontrado = true;
                    } else {
                        if (codigo.charAt(posicion) == '\n') {
                            linea++;
                            columna = 1;
                        } else {
                            columna++;
                        }
                        posicion++;
                    }
                }

                if (encontrado) {
                    tokens.add(new Token(TipoToken.COMENTARIO,
                            codigo.substring(inicio, posicion), lineaInicio, coloniaInicio));
                } else {
                    tokens.add(new Token(TipoToken.DESCONOCIDO,
                            "Comentario multilinea sin cerrar", lineaInicio, coloniaInicio));
                }
                continue;
            }

            // Literales de String
            if (c == '"') {
                int inicio = posicion;
                posicion++;
                columna++;

                boolean escapado = false;
                boolean cerrado = false;

                while (posicion < codigo.length() && !cerrado) {
                    char actual = codigo.charAt(posicion);

                    if (actual == '\\' && !escapado) {
                        escapado = true;
                    } else if (actual == '"' && !escapado) {
                        cerrado = true;
                    } else {
                        escapado = false;
                    }

                    posicion++;
                    columna++;
                }

                tokens.add(new Token(TipoToken.LITERAL_STRING,
                        codigo.substring(inicio, posicion), linea, columna - (posicion - inicio)));
                continue;
            }

            // Literales de carácter
            if (c == '\'') {
                int inicio = posicion;
                posicion++;
                columna++;

                boolean escapado = false;
                boolean cerrado = false;

                while (posicion < codigo.length() && !cerrado) {
                    char actual = codigo.charAt(posicion);

                    if (actual == '\\' && !escapado) {
                        escapado = true;
                    } else if (actual == '\'' && !escapado) {
                        cerrado = true;
                    } else {
                        escapado = false;
                    }

                    posicion++;
                    columna++;
                }

                tokens.add(new Token(TipoToken.LITERAL_CHAR,
                        codigo.substring(inicio, posicion), linea, columna - (posicion - inicio)));
                continue;
            }

            // Números
            if (Character.isDigit(c)
                    || (c == '.' && posicion + 1 < codigo.length() && Character.isDigit(codigo.charAt(posicion + 1)))) {
                int inicio = posicion;
                boolean puntoDecimal = false;

                if (c == '.') {
                    puntoDecimal = true;
                }

                posicion++;
                columna++;

                while (posicion < codigo.length() &&
                        (Character.isDigit(codigo.charAt(posicion)) ||
                                (!puntoDecimal && codigo.charAt(posicion) == '.'))) {

                    if (codigo.charAt(posicion) == '.') {
                        puntoDecimal = true;
                    }

                    posicion++;
                    columna++;
                }

                tokens.add(new Token(TipoToken.LITERAL_NUM,
                        codigo.substring(inicio, posicion), linea, columna - (posicion - inicio)));
                continue;
            }

            // Identificadores y palabras clave
            if (Character.isLetter(c) || c == '_') {
                int inicio = posicion;
                posicion++;
                columna++;

                while (posicion < codigo.length() &&
                        (Character.isLetterOrDigit(codigo.charAt(posicion)) || codigo.charAt(posicion) == '_')) {
                    posicion++;
                    columna++;
                }

                String token = codigo.substring(inicio, posicion);

                // Verificar si es una palabra clave
                boolean esPalabraClave = PALABRAS_CLAVE_JAVA.contains(token);

                tokens.add(new Token(
                        esPalabraClave ? TipoToken.PALABRA_CLAVE : TipoToken.IDENTIFICADOR,
                        token, linea, columna - token.length()));
                continue;
            }

            // Operadores y delimitadores
            if (esOperador(c)) {
                int inicio = posicion;

                // Operadores compuestos de 2 caracteres
                if (posicion + 1 < codigo.length() && esOperadorCompuesto(c, codigo.charAt(posicion + 1))) {
                    posicion += 2;
                    columna += 2;
                    tokens.add(new Token(TipoToken.OPERADOR, codigo.substring(inicio, posicion), linea, columna - 2));
                } else {
                    posicion++;
                    columna++;
                    tokens.add(new Token(TipoToken.OPERADOR, String.valueOf(c), linea, columna - 1));
                }
                continue;
            }

            // Delimitadores
            if (esDelimitador(c)) {
                tokens.add(new Token(TipoToken.DELIMITADOR, String.valueOf(c), linea, columna));
                posicion++;
                columna++;
                continue;
            }

            // Si no reconocemos el token
            tokens.add(new Token(TipoToken.DESCONOCIDO, String.valueOf(c), linea, columna));
            posicion++;
            columna++;
        }

        return tokens;
    }

    /**
     * Verifica si un carácter es un operador
     */
    private static boolean esOperador(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '%' || c == '=' ||
                c == '>' || c == '<' || c == '!' || c == '&' || c == '|' || c == '^' ||
                c == '~' || c == '?';
    }

    /**
     * Verifica si un carácter es un delimitador
     */
    private static boolean esDelimitador(char c) {
        return c == '(' || c == ')' || c == '{' || c == '}' || c == '[' || c == ']' ||
                c == ';' || c == ',' || c == '.' || c == ':';
    }

    /**
     * Verifica si dos caracteres forman un operador compuesto
     */
    private static boolean esOperadorCompuesto(char c1, char c2) {
        String op = c1 + "" + c2;
        return op.equals("==") || op.equals("!=") || op.equals("<=") || op.equals(">=") ||
                op.equals("&&") || op.equals("||") || op.equals("++") || op.equals("--") ||
                op.equals("+=") || op.equals("-=") || op.equals("*=") || op.equals("/=") ||
                op.equals("%=") || op.equals("<<") || op.equals(">>") || op.equals("&=") ||
                op.equals("|=") || op.equals("^=");
    }

    /**
     * Procesa los tokens de Java para mostrar la información en formato tabular
     * 
     * @param tokens Lista de tokens a procesar
     * @return Lista de datos para la tabla (cada fila es un arreglo de objetos)
     */
    public static List<Object[]> procesarParaTabla(List<Token> tokens) {
        List<Object[]> filas = new ArrayList<>();

        for (Token token : tokens) {
            filas.add(new Object[] {
                    token.getLexema(),
                    token.getTipo().getDescripcion(),
                    token.getLinea()
            });
        }

        return filas;
    }
}