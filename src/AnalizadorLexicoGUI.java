import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Interfaz gráfica para el analizador léxico con soporte para Robot y Java
 */
public class AnalizadorLexicoGUI extends JFrame {

    // Componentes de la GUI
    private JTextArea txtCodigo;
    private JTable tblTokens;
    private DefaultTableModel modeloTabla;
    private JTable tblSimbolos;
    private DefaultTableModel modeloSimbolos;
    private JButton btnRobot;
    private JButton btnJava;
    private JButton btnAnalizar;
    private JButton btnLimpiar;
    private JComboBox<String> cboTipoAnalisis;
    private JLabel lblEstado;

    /**
     * Constructor de la interfaz gráfica
     */
    public AnalizadorLexicoGUI() {
        // Configuración básica de la ventana
        setTitle("Analizador Léxico");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout());

        // Panel superior para botones y título
        JPanel panelSuperior = new JPanel(new BorderLayout());

        // Panel para los botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));

        cboTipoAnalisis = new JComboBox<>(new String[] { "Robot", "Java" });
        cboTipoAnalisis.addActionListener(e -> actualizarColumnasTabla());

        btnRobot = new JButton("Ejemplo Robot");
        btnRobot.addActionListener(e -> cargarEjemploRobot());

        btnJava = new JButton("Ejemplo Java");
        btnJava.addActionListener(e -> cargarEjemploJava());

        btnAnalizar = new JButton("Analizar");
        btnAnalizar.addActionListener(e -> analizarCodigo());

        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.addActionListener(e -> limpiarTodo());

        panelBotones.add(new JLabel("Tipo: "));
        panelBotones.add(cboTipoAnalisis);
        panelBotones.add(btnRobot);
        panelBotones.add(btnJava);
        panelBotones.add(btnAnalizar);
        panelBotones.add(btnLimpiar);

        // Etiqueta para el título
        JLabel lblTitulo = new JLabel("Analizador Léxico", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));

        panelSuperior.add(panelBotones, BorderLayout.WEST);
        panelSuperior.add(lblTitulo, BorderLayout.CENTER);

        // Panel central dividido en dos partes
        JSplitPane panelCentral = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        panelCentral.setDividerLocation(400);

        // Panel izquierdo para el código fuente
        JPanel panelIzquierdo = new JPanel(new BorderLayout());
        JLabel lblInstrucciones = new JLabel("Instrucciones", JLabel.CENTER);
        txtCodigo = new JTextArea();
        txtCodigo.setFont(new Font("Monospaced", Font.PLAIN, 14));

        // Agregar numeración de líneas al JTextArea
        JScrollPane scrollCodigo = new JScrollPane(txtCodigo);
        scrollCodigo.setRowHeaderView(new LineNumberView(txtCodigo));

        panelIzquierdo.add(lblInstrucciones, BorderLayout.NORTH);
        panelIzquierdo.add(scrollCodigo, BorderLayout.CENTER);

        // Panel derecho dividido en dos partes verticalmente
        JPanel panelDerecho = new JPanel(new BorderLayout());

        // Tabla de símbolos
        JLabel lblTablaSimbolos = new JLabel("Tabla de Símbolos", JLabel.CENTER);
        String[] columnasSimbolos = { "NOMBRE", "TIPO", "VALOR" };
        modeloSimbolos = new DefaultTableModel(columnasSimbolos, 0);
        tblSimbolos = new JTable(modeloSimbolos);
        JScrollPane scrollSimbolos = new JScrollPane(tblSimbolos);

        // Tabla de tokens
        JLabel lblTablaTokens = new JLabel("Tokens", JLabel.CENTER);
        String[] columnasTokens = { "TOKEN", "TIPO", "VALOR", "PARAMETRO" };
        modeloTabla = new DefaultTableModel(columnasTokens, 0);
        tblTokens = new JTable(modeloTabla);
        JScrollPane scrollTokens = new JScrollPane(tblTokens);

        // Dividir el panel derecho en dos
        JSplitPane panelDerechoDividido = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

        JPanel panelSimbolos = new JPanel(new BorderLayout());
        panelSimbolos.add(lblTablaSimbolos, BorderLayout.NORTH);
        panelSimbolos.add(scrollSimbolos, BorderLayout.CENTER);

        JPanel panelTokens = new JPanel(new BorderLayout());
        panelTokens.add(lblTablaTokens, BorderLayout.NORTH);
        panelTokens.add(scrollTokens, BorderLayout.CENTER);

        panelDerechoDividido.setTopComponent(panelSimbolos);
        panelDerechoDividido.setBottomComponent(panelTokens);
        panelDerechoDividido.setDividerLocation(150);

        panelDerecho.add(panelDerechoDividido, BorderLayout.CENTER);

        // Agregar los paneles al panel central
        panelCentral.setLeftComponent(panelIzquierdo);
        panelCentral.setRightComponent(panelDerecho);

        // Panel inferior para mensajes de estado
        JPanel panelInferior = new JPanel(new BorderLayout());
        lblEstado = new JLabel("Listo");
        panelInferior.add(lblEstado, BorderLayout.WEST);

        // Agregar todos los paneles al panel principal
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        panelPrincipal.add(panelCentral, BorderLayout.CENTER);
        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);

        // Agregar el panel principal al frame
        setContentPane(panelPrincipal);

        // Establecer un aspecto y sentido moderno
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Inicializar columnas
        actualizarColumnasTabla();
    }

    /**
     * Actualiza las columnas de la tabla de tokens según el tipo de análisis
     * seleccionado
     */
    private void actualizarColumnasTabla() {
        String tipoAnalisis = (String) cboTipoAnalisis.getSelectedItem();

        // Limpiar las tablas
        limpiarTablas();

        // Configurar las columnas según el tipo de análisis
        if ("Robot".equals(tipoAnalisis)) {
            modeloTabla.setColumnIdentifiers(new String[] { "TOKEN", "TIPO", "VALOR", "PARAMETRO" });
            modeloSimbolos.setColumnIdentifiers(new String[] { "NOMBRE", "TIPO", "VALOR" });
        } else { // Java
            modeloTabla.setColumnIdentifiers(new String[] { "TOKEN", "TIPO", "LINEA" });
            modeloSimbolos.setColumnIdentifiers(new String[] { "IDENTIFICADOR", "TIPO", "LÍNEA" });
        }
    }

    /**
     * Cargar un ejemplo de código de robot
     */
    private void cargarEjemploRobot() {
        String codigoRobot = "Robot r1\n" +
                "r1.iniciar()\n" + // Sin paréntesis pero será identificado como acción
                "r1.velocidad=50\n" + // Con valor
                "r1.base=180\n" +
                "r1.cuerpo=45\n" +
                "r1.garra=90\n" +
                "r1.cerrarGarra()\n" + // Con paréntesis
                "r1.abrirGarra()\n" + // Sin paréntesis pero será identificado como acción
                "r1.finalizar()"; // Sin paréntesis pero será identificado como acción

        txtCodigo.setText(codigoRobot);
        cboTipoAnalisis.setSelectedItem("Robot");
        lblEstado.setText("Código de ejemplo de robot cargado. Presione 'Analizar' para procesar.");
    }

    /**
     * Cargar un ejemplo de código Java
     */
    private void cargarEjemploJava() {
        String codigoJava = "public class Ejemplo {\n" +
                "    public static void main(String[] args) {\n" +
                "        int x = 10;\n" +
                "        double y = 3.14;\n" +
                "        // Este es un comentario\n" +
                "        if (x > 5) {\n" +
                "            x++;\n" +
                "        } else {\n" +
                "            x--;\n" +
                "        }\n" +
                "        /* Comentario\n" +
                "           multilinea */\n" +
                "    }\n" +
                "}";

        txtCodigo.setText(codigoJava);
        cboTipoAnalisis.setSelectedItem("Java");
        lblEstado.setText("Código de ejemplo de Java cargado. Presione 'Analizar' para procesar.");
    }

    /**
     * Analizar el código fuente
     */
    private void analizarCodigo() {
        String codigo = txtCodigo.getText();

        if (codigo.trim().isEmpty()) {
            lblEstado.setText("Error: No hay código para analizar");
            return;
        }

        try {
            // Limpiar las tablas
            limpiarTablas();

            // Determinar el tipo de análisis
            String tipoAnalisis = (String) cboTipoAnalisis.getSelectedItem();

            if ("Robot".equals(tipoAnalisis)) {
                analizarCodigoRobot(codigo);
            } else {
                analizarCodigoJava(codigo);
            }

            lblEstado.setText("Análisis completado con éxito");
        } catch (Exception e) {
            lblEstado.setText("Error durante el análisis: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Analizar código de robot
     */
    private void analizarCodigoRobot(String codigo) {
        // Analizar el código
        List<AnalizadorRobot.Token> tokens = AnalizadorRobot.analizar(codigo);

        // Procesar los tokens para la tabla
        List<Object[]> filasTabla = AnalizadorRobot.procesarParaTabla(tokens);

        // Mostrar los tokens en la tabla
        for (Object[] fila : filasTabla) {
            modeloTabla.addRow(fila);
        }

        // Actualizar la tabla de símbolos
        // Recolectar identificadores
        for (AnalizadorRobot.Token token : tokens) {
            if (token.getTipo() == AnalizadorRobot.TipoToken.IDENTIFICADOR) {
                // Verificar si ya existe en la tabla
                boolean existe = false;
                for (int i = 0; i < modeloSimbolos.getRowCount(); i++) {
                    if (modeloSimbolos.getValueAt(i, 0).equals(token.getLexema())) {
                        existe = true;
                        break;
                    }
                }

                if (!existe) {
                    modeloSimbolos.addRow(new Object[] {
                            token.getLexema(),
                            "Variable",
                            ""
                    });
                }
            }
        }

        // Métodos con valores
        for (Object[] fila : filasTabla) {
            if (fila[1].equals("Metodo") && fila[3].equals("Si")) {
                // Verificar si ya existe en la tabla
                boolean existe = false;
                for (int i = 0; i < modeloSimbolos.getRowCount(); i++) {
                    if (modeloSimbolos.getValueAt(i, 0).equals(fila[0])) {
                        existe = true;
                        modeloSimbolos.setValueAt(fila[2], i, 2); // Actualizar valor
                        break;
                    }
                }

                if (!existe) {
                    modeloSimbolos.addRow(new Object[] {
                            fila[0], // nombre del método
                            "Método",
                            fila[2] // valor del método
                    });
                }
            }
        }
    }

    /**
     * Analizar código Java
     */
    private void analizarCodigoJava(String codigo) {
        // Analizar el código
        List<AnalizadorJava.Token> tokens = AnalizadorJava.analizar(codigo);

        // Procesar los tokens para la tabla
        List<Object[]> filasTabla = AnalizadorJava.procesarParaTabla(tokens);

        // Mostrar los tokens en la tabla
        for (Object[] fila : filasTabla) {
            modeloTabla.addRow(fila);
        }

        // Actualizar la tabla de símbolos (solo identificadores)
        for (AnalizadorJava.Token token : tokens) {
            if (token.getTipo() == AnalizadorJava.TipoToken.IDENTIFICADOR) {
                // Verificar si ya existe en la tabla
                boolean existe = false;
                for (int i = 0; i < modeloSimbolos.getRowCount(); i++) {
                    if (modeloSimbolos.getValueAt(i, 0).equals(token.getLexema())) {
                        existe = true;
                        break;
                    }
                }

                if (!existe) {
                    modeloSimbolos.addRow(new Object[] {
                            token.getLexema(),
                            "Variable",
                            token.getLinea()
                    });
                }
            }
        }
    }

    /**
     * Limpiar todas las tablas y campos
     */
    private void limpiarTodo() {
        txtCodigo.setText("");
        limpiarTablas();
        lblEstado.setText("Todo limpiado");
    }

    /**
     * Limpiar solo las tablas
     */
    private void limpiarTablas() {
        modeloTabla.setRowCount(0);
        modeloSimbolos.setRowCount(0);
    }

    /**
     * Componente para mostrar números de línea en un JTextArea
     */
    private class LineNumberView extends JComponent {
        private final JTextComponent textComponent;
        private final FontMetrics fontMetrics;
        private final int digitWidth;
        private final int insets = 5;

        public LineNumberView(JTextComponent textComponent) {
            this.textComponent = textComponent;
            setFont(new Font("monospaced", Font.PLAIN, textComponent.getFont().getSize()));
            fontMetrics = getFontMetrics(getFont());
            digitWidth = fontMetrics.stringWidth("9");
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Rectangle clip = g.getClipBounds();
            int startOffset = textComponent.viewToModel(new Point(0, clip.y));
            int endOffset = textComponent.viewToModel(new Point(0, clip.y + clip.height));

            while (startOffset <= endOffset) {
                try {
                    int lineNumber = textComponent.getDocument().getDefaultRootElement().getElementIndex(startOffset)
                            + 1;
                    String lineNumberStr = String.valueOf(lineNumber);

                    int y = textComponent.modelToView(startOffset).y + fontMetrics.getAscent();
                    int strWidth = fontMetrics.stringWidth(lineNumberStr);

                    g.setColor(Color.GRAY);
                    g.drawString(lineNumberStr, getWidth() - strWidth - insets, y);

                    startOffset = javax.swing.text.Utilities.getRowEnd(textComponent, startOffset) + 1;
                } catch (BadLocationException e) {
                    break;
                }
            }
        }

        @Override
        public Dimension getPreferredSize() {
            int lines = textComponent.getDocument().getDefaultRootElement().getElementCount();
            int digits = Math.max(2, String.valueOf(lines).length());
            return new Dimension(digits * digitWidth + 2 * insets, textComponent.getHeight());
        }
    }

    /**
     * Método principal para iniciar la aplicación
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AnalizadorLexicoGUI().setVisible(true));
    }
}