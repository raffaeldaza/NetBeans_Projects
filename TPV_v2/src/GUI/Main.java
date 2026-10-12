/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package GUI;


import dto.GestorProductos;
import dto.Mesa;
import dto.Producto;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author DAM_204
 */


public class Main extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Main.class.getName());
    
   private boolean modoCalculadoraActivo = false;
    private String operacionActual = "";
    private double resultadoCalculadora = 0;
    
    private enum EstadoFlujo { NORMAL, ESPERANDO_CANTIDAD, ESPERANDO_PRECIO, ESPERANDO_EFECTIVO }
    private EstadoFlujo estadoFlujo = EstadoFlujo.NORMAL;
    private int cantidadTemporal = 0;
    private String nombreProductoTemporal = "";
    
    private Mesa mesaSeleccionada;
    private List<Mesa> listaMesas;
    private List<Producto> todosProductos;
    
    private final Color COLOR_FONDO = new Color(224, 225, 221);
    private final Color COLOR_PRIMARIO = new Color(142, 202, 230);
    private final Color COLOR_ACTIVO = new Color(33, 158, 188);
    private final Color COLOR_TEXTO = new Color(2, 48, 71);
    private final Color COLOR_DISPLAY = new Color(255, 183, 3);
    private final Color COLOR_ERROR = new Color(251, 133, 0);
    private final Color COLOR_OPACO = new Color(200, 200, 200);

    public Main() {
        initComponents();
        
        listaMesas = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            listaMesas.add(new Mesa(i));
        }
        todosProductos = GestorProductos.cargarProductos("recursos/productos.txt");
        
        actualizarEstadosVisuales();
        actualizarVisualMesas();
        mensajeBienvenida();
    }
    
    // ============================================================
    // SISTEMA DE MENSAJES INTERACTIVOS (SIN EMOJIS)
    // ============================================================
    private void mostrarMensaje(String mensaje) {
        jTextMensaje.setText(mensaje);
        jTextMensaje.setCaretPosition(0); 
    }

    private void mensajeBienvenida() {
        mostrarMensaje("¡Bienvenido a Cafetería PanRin! Selecciona una mesa para comenzar.");
    }

    private void mensajeProceso(String proceso) {
        mostrarMensaje("PROCESO: " + proceso);
    }

    private void mensajeExito(String mensaje) {
        mostrarMensaje("EXITO: " + mensaje);
    }

    private void mensajeError(String error) {
        mostrarMensaje("ERROR: " + error);
    }

    private void mensajeAyuda(String ayuda) {
        mostrarMensaje("AYUDA: " + ayuda);
    }

    // ============================================================
    // CONFIGURACIÓN VISUAL
    // ============================================================
    private void actualizarEstadosVisuales() {
        if (modoCalculadoraActivo) {
            btnModoCalculadora.setBackground(COLOR_ACTIVO);
            btnModoCalculadora.setForeground(Color.WHITE);
            btnAñadir.setBackground(COLOR_ACTIVO);
            btnAñadir.setForeground(Color.WHITE);
            btnAñadir.setEnabled(true);
            activarBotonesCalculadora(true);
        } else {
            btnModoCalculadora.setBackground(COLOR_OPACO);
            btnModoCalculadora.setForeground(Color.GRAY);
            btnAñadir.setBackground(COLOR_OPACO);
            btnAñadir.setForeground(Color.GRAY);
            btnAñadir.setEnabled(false);
            activarBotonesCalculadora(false);
        }
    }

    private void activarBotonesCalculadora(boolean activo) {
        JButton[] botones = {
            btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9,
            btnPunto, btnC, btnIgual, btnSuma, btnResta, btnMult, btnDividir
        };
        for (JButton b : botones) {
            b.setEnabled(activo);
            if (!activo) {
                b.setBackground(new Color(230, 230, 230));
                b.setForeground(Color.GRAY);
            } else {
                if (b == btnC) b.setBackground(COLOR_ERROR);
                else if (b == btnSuma || b == btnResta || b == btnMult || b == btnDividir) b.setBackground(COLOR_PRIMARIO);
                else b.setBackground(Color.WHITE);
                b.setForeground(COLOR_TEXTO);
            }
        }
        displayCalculadora.setBackground(activo ? COLOR_DISPLAY : new Color(230, 230, 230));
    }

    private void actualizarVisualMesas() {
        JButton[] botones = {btnMesa1, btnMesa2, btnMesa3, btnMesa4,
                             btnMesa5, btnMesa6, btnMesa7, btnMesa8};
        for (int i = 0; i < botones.length; i++) {
            Mesa mesa = listaMesas.get(i);
            if (mesa == mesaSeleccionada) {
                botones[i].setBackground(COLOR_ACTIVO);
                botones[i].setForeground(Color.WHITE);
            } else if (mesa.isOcupada()) {
                botones[i].setBackground(COLOR_ERROR);
                botones[i].setForeground(Color.WHITE);
            } else {
                botones[i].setBackground(COLOR_OPACO);
                botones[i].setForeground(COLOR_TEXTO);
            }
        }
    }

    // ============================================================
    // UTILIDADES
    // ============================================================
    private double parsearNumero(String s) {
        if (s == null || s.trim().isEmpty()) return -1;
        try {
            return Double.parseDouble(s.trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private String formatoEuros(double valor) {
        return String.format(Locale.US, "%.2f €", valor);
    }

    private void recargarTabla() {
        DefaultTableModel modelo = (DefaultTableModel) tablaProductos.getModel();
        modelo.setRowCount(0);
        
        double subtotal = 0;
        if (mesaSeleccionada != null) {
            for (String[] fila : mesaSeleccionada.getPedido()) {
                modelo.addRow(fila);
                subtotal += Double.parseDouble(fila[3]);
            }
        }
        
        double iva = subtotal * 0.10;
        double total = subtotal + iva;
        
        lblSubtotal.setText("SUBTOTAL: " + formatoEuros(subtotal));
        lblIva.setText("IVA 10%:    " + formatoEuros(iva));
        lblTotal.setText("TOTAL:      " + formatoEuros(total));
    }

    public void agregarProducto(Producto p) {
        if (mesaSeleccionada == null) {
            mensajeError("Primero selecciona una MESA");
            return;
        }
        
        int cantidad = 1;
        if (!modoCalculadoraActivo && !displayCalculadora.getText().equals("0")) {
            double cantidadParseada = parsearNumero(displayCalculadora.getText());
            if (cantidadParseada > 0) {
                cantidad = (int) cantidadParseada;
            }
            displayCalculadora.setText("0");
        }
        
        mesaSeleccionada.agregarProducto(p, cantidad);
        recargarTabla();
        actualizarVisualMesas();
        mostrarMensaje("AÑADIDO: " + p.getNombre() + " (Cant: " + cantidad + ")");
    }
    
    private void seleccionarMesa(int numero) {
        mesaSeleccionada = listaMesas.get(numero - 1);
        actualizarVisualMesas();

        if (mesaSeleccionada.isOcupada()) {
            recargarTabla();
            mostrarMensaje("MESA " + numero + " seleccionada (OCUPADA) | Productos en cuenta: " + mesaSeleccionada.getPedido().size());
        } else {
            recargarTabla();
            mostrarMensaje("MESA " + numero + " seleccionada (LIBRE) | Lista para añadir productos.");
        }
    }

    // ============================================================
    // LÓGICA DE LA CALCULADORA
    // ============================================================
    private void ingresarNumero(String numero) {
        if (!modoCalculadoraActivo) return;
        operacionActual += numero;
        displayCalculadora.setText(operacionActual);
    }

    private double evaluarExpresion(String expr) {
        if (expr == null || expr.trim().isEmpty()) return 0;
        expr = expr.replace("x", "*").replace(",", ".");
        
        String[] sumas = expr.split("(?=[+-])|(?<=[+-])");
        double total = 0;
        String operadorActual = "+";
        
        for (String parte : sumas) {
            if (parte.equals("+") || parte.equals("-")) {
                operadorActual = parte;
                continue;
            }
            double valor = evaluarMultiplicacionDivision(parte);
            if (operadorActual.equals("+")) total += valor;
            else if (operadorActual.equals("-")) total -= valor;
        }
        return total;
    }

    private double evaluarMultiplicacionDivision(String expr) {
        String[] factores = expr.split("(?=[*/])|(?<=[*/])");
        double resultado = 1;
        String op = "*";
        for (String f : factores) {
            if (f.equals("*") || f.equals("/")) {
                op = f;
                continue;
            }
            double val = Double.parseDouble(f);
            if (op.equals("*")) resultado *= val;
            else if (op.equals("/")) resultado /= val;
        }
        return resultado;
    }
      
    // ============================================================
    // PROCESOS DE PAGO
    // ============================================================
    private void procesarPago(Mesa mesa, String tipoDocumento, String idCliente) {
        String[] opciones = {"Efectivo", "Tarjeta"};
        int eleccion = JOptionPane.showOptionDialog(this, 
            "Total a pagar: " + formatoEuros(mesa.getTotal() * 1.10), 
            "Forma de Pago", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, 
            null, opciones, opciones[0]);
            
        if (eleccion == 0) {
            procesarPagoEfectivoModal(mesa, tipoDocumento, idCliente);
        } else if (eleccion == 1) {
            procesarPagoTarjeta(mesa, tipoDocumento, idCliente);
        }
    }

    private void procesarPagoEfectivoModal(Mesa mesa, String tipoDoc, String idCliente) {
        double total = mesa.getTotal() * 1.10;
        while (true) {
            String recibidoStr = JOptionPane.showInputDialog(this, 
                "Total: " + formatoEuros(total) + "\n¿Cuánto dinero entrega el cliente?");
            if (recibidoStr == null) {
                mostrarMensaje("CANCELADO: Pago en efectivo cancelado.");
                return;
            }
            
            double recibido = parsearNumero(recibidoStr);
            if (recibido >= total) {
                double cambio = recibido - total;
                mostrarMensaje("PAGO RECIBIDO: " + formatoEuros(recibido) + " | Cambio: " + formatoEuros(cambio));
                
                imprimirDocumento(tipoDoc, idCliente);
                mesa.limpiar();
                limpiarInterfaz();
                return;
            }
            JOptionPane.showMessageDialog(this, "Importe insuficiente.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void procesarPagoTarjeta(Mesa mesa, String tipoDoc, String idCliente) {
        JOptionPane.showMessageDialog(this, "Procesando pago...\nAcerque o inserte la tarjeta.", "Pago con Tarjeta", JOptionPane.INFORMATION_MESSAGE);
        try { Thread.sleep(1500); } catch (InterruptedException e) {}
        
        mostrarMensaje("PAGO CON TARJETA APROBADO.");
        imprimirDocumento(tipoDoc, idCliente);
        mesa.limpiar();
        limpiarInterfaz();
    }

    private void limpiarInterfaz() {
        mesaSeleccionada = null;
        estadoFlujo = EstadoFlujo.NORMAL;
        recargarTabla();
        actualizarVisualMesas();
        mensajeBienvenida();
    }

    private void imprimirDocumento(String tipo, String idCliente) {
        StringBuilder doc = new StringBuilder();
        doc.append("════════════════════════════════\n");
        doc.append("       CAFETERÍA PANRIN\n");
        doc.append("    C/ Ejemplo 123, Valladolid\n");
        doc.append("    CIF: B12345678\n");
        doc.append("════════════════════════════════\n\n");
        doc.append(tipo).append(" - Mesa: ").append(mesaSeleccionada.getNumero()).append("\n");
        if (idCliente != null) doc.append("Cliente: ").append(idCliente).append("\n");
        doc.append("Fecha: ").append(new java.util.Date()).append("\n\n");
        
        for (String[] fila : mesaSeleccionada.getPedido()) {
            doc.append(String.format("%-18s %2s x %5s€ = %6s€\n", fila[0], fila[1], fila[2], fila[3]));
        }
        
        double sub = mesaSeleccionada.getTotal();
        doc.append("\n────────────────────────────────\n");
        doc.append(String.format(Locale.US, "SUBTOTAL: %25.2f€\n", sub));
        doc.append(String.format(Locale.US, "IVA 10%%:  %25.2f€\n", sub * 0.10));
        doc.append(String.format(Locale.US, "TOTAL:    %25.2f€\n", sub * 1.10));
        doc.append("════════════════════════════════\n");
        doc.append("     ¡Gracias por su visita!\n");
        doc.append("════════════════════════════════");

        javax.swing.JTextArea area = new javax.swing.JTextArea(doc.toString());
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 13));
        
        Object[] botones = {"Imprimir", "Cerrar"};
        int opcion = JOptionPane.showOptionDialog(this, new javax.swing.JScrollPane(area), 
            tipo, JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, botones, botones[1]);
            
        if (opcion == 0) {
            try { area.print(); } 
            catch (java.awt.print.PrinterException ex) {
                JOptionPane.showMessageDialog(this, "Error al imprimir: " + ex.getMessage());
            }
        }
    }

    

 

/********************----------------------------------------------------*****************************/
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabelEmpresa = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btnComidas = new javax.swing.JButton();
        btnBebidas = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        displayCalculadora = new javax.swing.JTextField();
        jPanelBotonesCalculadora = new javax.swing.JPanel();
        btn7 = new javax.swing.JButton();
        btn8 = new javax.swing.JButton();
        btn9 = new javax.swing.JButton();
        btnDividir = new javax.swing.JButton();
        btn4 = new javax.swing.JButton();
        btn5 = new javax.swing.JButton();
        btn6 = new javax.swing.JButton();
        btnMult = new javax.swing.JButton();
        btn1 = new javax.swing.JButton();
        btn2 = new javax.swing.JButton();
        btn3 = new javax.swing.JButton();
        btnResta = new javax.swing.JButton();
        btnC = new javax.swing.JButton();
        btn0 = new javax.swing.JButton();
        btnPunto = new javax.swing.JButton();
        btnSuma = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        btnModoCalculadora = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        btnAñadir = new javax.swing.JButton();
        btnBorrar = new javax.swing.JButton();
        btn10 = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        btnIgual = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        btnEfectivo = new javax.swing.JButton();
        btnTarjeta = new javax.swing.JButton();
        btnTicket = new javax.swing.JButton();
        btnFactura = new javax.swing.JButton();
        jPanel15 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaProductos = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        lblSubtotal = new javax.swing.JTextField();
        lblIva = new javax.swing.JTextField();
        lblTotal = new javax.swing.JTextField();
        jPanel14 = new javax.swing.JPanel();
        btnMesa3 = new javax.swing.JButton();
        btnMesa1 = new javax.swing.JButton();
        btnMesa2 = new javax.swing.JButton();
        btnMesa5 = new javax.swing.JButton();
        btnMesa6 = new javax.swing.JButton();
        btnMesa7 = new javax.swing.JButton();
        btnMesa8 = new javax.swing.JButton();
        btnMesa4 = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jTextMensaje = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setForeground(new java.awt.Color(224, 225, 221));

        jPanel2.setForeground(new java.awt.Color(224, 225, 221));

        jLabelEmpresa.setBackground(new java.awt.Color(142, 202, 230));
        jLabelEmpresa.setFont(new java.awt.Font("Palatino Linotype", 1, 36)); // NOI18N
        jLabelEmpresa.setForeground(new java.awt.Color(2, 48, 71));
        jLabelEmpresa.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelEmpresa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/pan-de-bono.png"))); // NOI18N
        jLabelEmpresa.setText("Cafeteria PanRin");
        jLabelEmpresa.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabelEmpresa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabelEmpresa, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
        );

        jPanel3.setForeground(new java.awt.Color(224, 225, 221));
        jPanel3.setLayout(new java.awt.GridLayout(1, 4, 40, 0));

        btnComidas.setBackground(new java.awt.Color(142, 202, 230));
        btnComidas.setForeground(new java.awt.Color(2, 48, 71));
        btnComidas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/comidas.png"))); // NOI18N
        btnComidas.setMaximumSize(new java.awt.Dimension(120, 50));
        btnComidas.setMinimumSize(new java.awt.Dimension(120, 50));
        btnComidas.setPreferredSize(new java.awt.Dimension(120, 50));
        btnComidas.addActionListener(this::btnComidasActionPerformed);
        jPanel3.add(btnComidas);

        btnBebidas.setBackground(new java.awt.Color(142, 202, 230));
        btnBebidas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/bebidas.png"))); // NOI18N
        btnBebidas.addActionListener(this::btnBebidasActionPerformed);
        jPanel3.add(btnBebidas);

        jPanel4.setForeground(new java.awt.Color(224, 225, 221));

        jPanel6.setForeground(new java.awt.Color(224, 225, 221));

        displayCalculadora.setFont(new java.awt.Font("Monospaced", 0, 24)); // NOI18N
        displayCalculadora.setForeground(new java.awt.Color(0, 102, 204));
        displayCalculadora.addActionListener(this::displayCalculadoraActionPerformed);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(displayCalculadora)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(displayCalculadora, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanelBotonesCalculadora.setForeground(new java.awt.Color(224, 225, 221));
        jPanelBotonesCalculadora.setLayout(new java.awt.GridLayout(4, 4));

        btn7.setFont(new java.awt.Font("Alef", 1, 20)); // NOI18N
        btn7.setText("7");
        btn7.setMargin(new java.awt.Insets(1, 1, 1, 1));
        btn7.addActionListener(this::btn7ActionPerformed);
        jPanelBotonesCalculadora.add(btn7);

        btn8.setFont(new java.awt.Font("Alef", 1, 20)); // NOI18N
        btn8.setText("8");
        btn8.setMargin(new java.awt.Insets(1, 1, 1, 1));
        btn8.addActionListener(this::btn8ActionPerformed);
        jPanelBotonesCalculadora.add(btn8);

        btn9.setFont(new java.awt.Font("Alef", 1, 20)); // NOI18N
        btn9.setText("9");
        btn9.setMargin(new java.awt.Insets(1, 1, 1, 1));
        btn9.addActionListener(this::btn9ActionPerformed);
        jPanelBotonesCalculadora.add(btn9);

        btnDividir.setBackground(new java.awt.Color(142, 202, 230));
        btnDividir.setFont(new java.awt.Font("Alef", 1, 20)); // NOI18N
        btnDividir.setText("/");
        btnDividir.setMargin(new java.awt.Insets(1, 1, 1, 1));
        btnDividir.addActionListener(this::btnDividirActionPerformed);
        jPanelBotonesCalculadora.add(btnDividir);

        btn4.setFont(new java.awt.Font("Alef", 1, 20)); // NOI18N
        btn4.setText("4");
        btn4.setMargin(new java.awt.Insets(1, 1, 1, 1));
        btn4.addActionListener(this::btn4ActionPerformed);
        jPanelBotonesCalculadora.add(btn4);

        btn5.setFont(new java.awt.Font("Alef", 1, 20)); // NOI18N
        btn5.setText("5");
        btn5.setMargin(new java.awt.Insets(1, 1, 1, 1));
        btn5.addActionListener(this::btn5ActionPerformed);
        jPanelBotonesCalculadora.add(btn5);

        btn6.setFont(new java.awt.Font("Alef", 1, 20)); // NOI18N
        btn6.setText("6");
        btn6.setMargin(new java.awt.Insets(1, 1, 1, 1));
        btn6.addActionListener(this::btn6ActionPerformed);
        jPanelBotonesCalculadora.add(btn6);

        btnMult.setBackground(new java.awt.Color(142, 202, 230));
        btnMult.setFont(new java.awt.Font("Alef", 1, 20)); // NOI18N
        btnMult.setText("x");
        btnMult.setMargin(new java.awt.Insets(1, 1, 1, 1));
        btnMult.addActionListener(this::btnMultActionPerformed);
        jPanelBotonesCalculadora.add(btnMult);

        btn1.setFont(new java.awt.Font("Alef", 1, 20)); // NOI18N
        btn1.setText("1");
        btn1.setMargin(new java.awt.Insets(1, 1, 1, 1));
        btn1.addActionListener(this::btn1ActionPerformed);
        jPanelBotonesCalculadora.add(btn1);

        btn2.setFont(new java.awt.Font("Alef", 1, 20)); // NOI18N
        btn2.setText("2");
        btn2.setMargin(new java.awt.Insets(1, 1, 1, 1));
        btn2.addActionListener(this::btn2ActionPerformed);
        jPanelBotonesCalculadora.add(btn2);

        btn3.setFont(new java.awt.Font("Alef", 1, 20)); // NOI18N
        btn3.setText("3");
        btn3.setMargin(new java.awt.Insets(1, 1, 1, 1));
        btn3.addActionListener(this::btn3ActionPerformed);
        jPanelBotonesCalculadora.add(btn3);

        btnResta.setBackground(new java.awt.Color(142, 202, 230));
        btnResta.setFont(new java.awt.Font("Alef", 1, 20)); // NOI18N
        btnResta.setText("-");
        btnResta.setMargin(new java.awt.Insets(1, 1, 1, 1));
        btnResta.addActionListener(this::btnRestaActionPerformed);
        jPanelBotonesCalculadora.add(btnResta);

        btnC.setBackground(new java.awt.Color(251, 133, 0));
        btnC.setFont(new java.awt.Font("Alef", 1, 20)); // NOI18N
        btnC.setForeground(new java.awt.Color(2, 48, 71));
        btnC.setText("C");
        btnC.setMargin(new java.awt.Insets(1, 1, 1, 1));
        btnC.addActionListener(this::btnCActionPerformed);
        jPanelBotonesCalculadora.add(btnC);

        btn0.setFont(new java.awt.Font("Alef", 1, 20)); // NOI18N
        btn0.setText("0");
        btn0.setMargin(new java.awt.Insets(1, 1, 1, 1));
        btn0.addActionListener(this::btn0ActionPerformed);
        jPanelBotonesCalculadora.add(btn0);

        btnPunto.setFont(new java.awt.Font("Alef", 1, 20)); // NOI18N
        btnPunto.setText(".");
        btnPunto.setMargin(new java.awt.Insets(1, 1, 1, 1));
        btnPunto.addActionListener(this::btnPuntoActionPerformed);
        jPanelBotonesCalculadora.add(btnPunto);

        btnSuma.setBackground(new java.awt.Color(142, 202, 230));
        btnSuma.setFont(new java.awt.Font("Alef", 1, 20)); // NOI18N
        btnSuma.setText("+");
        btnSuma.setMargin(new java.awt.Insets(1, 1, 1, 1));
        btnSuma.addActionListener(this::btnSumaActionPerformed);
        jPanelBotonesCalculadora.add(btnSuma);

        jPanel12.setForeground(new java.awt.Color(224, 225, 221));
        jPanel12.setLayout(new java.awt.GridLayout(4, 0));

        jPanel7.setForeground(new java.awt.Color(224, 225, 221));
        jPanel7.setLayout(new java.awt.GridLayout(2, 0, 10, 10));

        btnModoCalculadora.setBackground(new java.awt.Color(33, 158, 188));
        btnModoCalculadora.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnModoCalculadora.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/calculadora.png"))); // NOI18N
        btnModoCalculadora.addActionListener(this::btnModoCalculadoraActionPerformed);
        jPanel7.add(btnModoCalculadora);

        jPanel13.setLayout(new java.awt.GridLayout(1, 2, 30, 0));

        btnAñadir.setBackground(new java.awt.Color(33, 197, 79));
        btnAñadir.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnAñadir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/comercio-electronico.png"))); // NOI18N
        btnAñadir.setMargin(new java.awt.Insets(1, 1, 1, 1));
        btnAñadir.addActionListener(this::btnAñadirActionPerformed);
        jPanel13.add(btnAñadir);

        btnBorrar.setBackground(new java.awt.Color(251, 170, 69));
        btnBorrar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBorrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/borrar.png"))); // NOI18N
        btnBorrar.addActionListener(this::btnBorrarActionPerformed);
        jPanel13.add(btnBorrar);

        jPanel7.add(jPanel13);

        btn10.setFont(new java.awt.Font("Alef", 1, 20)); // NOI18N
        btn10.setText("0");
        btn10.setMargin(new java.awt.Insets(1, 1, 1, 1));

        jPanel10.setLayout(new java.awt.GridLayout(1, 0));

        btnIgual.setFont(new java.awt.Font("Segoe UI", 0, 48)); // NOI18N
        btnIgual.setText("=");
        btnIgual.addActionListener(this::btnIgualActionPerformed);
        jPanel10.add(btnIgual);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(314, 314, 314)
                .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanelBotonesCalculadora, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(btn10, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(jPanelBotonesCalculadora, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(btn10, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jPanel5.setForeground(new java.awt.Color(224, 225, 221));

        jPanel11.setForeground(new java.awt.Color(224, 225, 221));
        jPanel11.setLayout(new java.awt.GridLayout(1, 4, 20, 0));

        btnEfectivo.setBackground(new java.awt.Color(33, 158, 188));
        btnEfectivo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/flujo-de-efectivo.png"))); // NOI18N
        btnEfectivo.addActionListener(this::btnEfectivoActionPerformed);
        jPanel11.add(btnEfectivo);

        btnTarjeta.setBackground(new java.awt.Color(33, 158, 188));
        btnTarjeta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/tarjeta-de-credito.png"))); // NOI18N
        btnTarjeta.addActionListener(this::btnTarjetaActionPerformed);
        jPanel11.add(btnTarjeta);

        btnTicket.setBackground(new java.awt.Color(142, 202, 230));
        btnTicket.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cuenta.png"))); // NOI18N
        btnTicket.addActionListener(this::btnTicketActionPerformed);
        jPanel11.add(btnTicket);

        btnFactura.setBackground(new java.awt.Color(142, 202, 230));
        btnFactura.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/factura.png"))); // NOI18N
        btnFactura.addActionListener(this::btnFacturaActionPerformed);
        jPanel11.add(btnFactura);

        jPanel15.setForeground(new java.awt.Color(224, 225, 221));

        tablaProductos.setBackground(new java.awt.Color(2, 48, 71));
        tablaProductos.setForeground(new java.awt.Color(255, 255, 255));
        tablaProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Producto", "Cantidad", "Precio", "Importe"
            }
        ));
        tablaProductos.setSelectionBackground(new java.awt.Color(2, 48, 71));
        jScrollPane2.setViewportView(tablaProductos);

        jPanel8.setForeground(new java.awt.Color(224, 225, 221));
        jPanel8.setLayout(new java.awt.GridLayout(3, 0));

        lblSubtotal.setEditable(false);
        lblSubtotal.setBackground(new java.awt.Color(240, 248, 255));
        lblSubtotal.setFont(new java.awt.Font("Monospaced", 0, 14)); // NOI18N
        lblSubtotal.setForeground(new java.awt.Color(0, 0, 51));
        lblSubtotal.addActionListener(this::lblSubtotalActionPerformed);
        jPanel8.add(lblSubtotal);

        lblIva.setEditable(false);
        lblIva.setBackground(new java.awt.Color(240, 248, 255));
        jPanel8.add(lblIva);

        lblTotal.setEditable(false);
        lblTotal.setBackground(new java.awt.Color(240, 248, 255));
        jPanel8.add(lblTotal);

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 490, Short.MAX_VALUE)))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 403, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jPanel14.setForeground(new java.awt.Color(224, 225, 221));

        btnMesa3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/mesas/mesa3.png"))); // NOI18N
        btnMesa3.setBorder(null);
        btnMesa3.setBorderPainted(false);
        btnMesa3.setContentAreaFilled(false);
        btnMesa3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnMesa3.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnMesa3.setOpaque(true);
        btnMesa3.setPreferredSize(new java.awt.Dimension(60, 60));
        btnMesa3.addActionListener(this::btnMesa3ActionPerformed);

        btnMesa1.setBackground(new java.awt.Color(227, 234, 242));
        btnMesa1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/mesas/mesa1.png"))); // NOI18N
        btnMesa1.setBorder(null);
        btnMesa1.setBorderPainted(false);
        btnMesa1.setContentAreaFilled(false);
        btnMesa1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnMesa1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnMesa1.setOpaque(true);
        btnMesa1.addActionListener(this::btnMesa1ActionPerformed);

        btnMesa2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/mesas/mesa2.png"))); // NOI18N
        btnMesa2.setBorder(null);
        btnMesa2.setBorderPainted(false);
        btnMesa2.setContentAreaFilled(false);
        btnMesa2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnMesa2.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnMesa2.setOpaque(true);
        btnMesa2.setPreferredSize(new java.awt.Dimension(60, 60));
        btnMesa2.addActionListener(this::btnMesa2ActionPerformed);

        btnMesa5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/mesas/mesa5.png"))); // NOI18N
        btnMesa5.setBorder(null);
        btnMesa5.setBorderPainted(false);
        btnMesa5.setContentAreaFilled(false);
        btnMesa5.setCursor(new java.awt.Cursor(java.awt.Cursor.S_RESIZE_CURSOR));
        btnMesa5.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnMesa5.setOpaque(true);
        btnMesa5.setPreferredSize(new java.awt.Dimension(60, 60));
        btnMesa5.addActionListener(this::btnMesa5ActionPerformed);

        btnMesa6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/mesas/mesa6.png"))); // NOI18N
        btnMesa6.setBorder(null);
        btnMesa6.setBorderPainted(false);
        btnMesa6.setContentAreaFilled(false);
        btnMesa6.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnMesa6.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnMesa6.setOpaque(true);
        btnMesa6.setPreferredSize(new java.awt.Dimension(60, 60));
        btnMesa6.addActionListener(this::btnMesa6ActionPerformed);

        btnMesa7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/mesas/mesa7.png"))); // NOI18N
        btnMesa7.setBorder(null);
        btnMesa7.setBorderPainted(false);
        btnMesa7.setContentAreaFilled(false);
        btnMesa7.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnMesa7.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnMesa7.setOpaque(true);
        btnMesa7.setPreferredSize(new java.awt.Dimension(60, 60));
        btnMesa7.addActionListener(this::btnMesa7ActionPerformed);

        btnMesa8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/mesas/mesa8.png"))); // NOI18N
        btnMesa8.setBorder(null);
        btnMesa8.setBorderPainted(false);
        btnMesa8.setContentAreaFilled(false);
        btnMesa8.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnMesa8.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnMesa8.setOpaque(true);
        btnMesa8.setPreferredSize(new java.awt.Dimension(60, 60));
        btnMesa8.addActionListener(this::btnMesa8ActionPerformed);

        btnMesa4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/mesas/mesa4.png"))); // NOI18N
        btnMesa4.setBorder(null);
        btnMesa4.setBorderPainted(false);
        btnMesa4.setContentAreaFilled(false);
        btnMesa4.setCursor(new java.awt.Cursor(java.awt.Cursor.S_RESIZE_CURSOR));
        btnMesa4.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnMesa4.setOpaque(true);
        btnMesa4.setPreferredSize(new java.awt.Dimension(60, 60));
        btnMesa4.addActionListener(this::btnMesa4ActionPerformed);

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap(27, Short.MAX_VALUE)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnMesa5, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnMesa1, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnMesa6, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnMesa2, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnMesa3, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnMesa7, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnMesa4, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnMesa8, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(25, 25, 25))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnMesa2, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMesa1, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnMesa3, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMesa4, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnMesa5, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMesa6, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnMesa7, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMesa8, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 761, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(74, 74, 74))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(102, Short.MAX_VALUE))
        );

        jTextMensaje.setEditable(false);
        jTextMensaje.setBackground(new java.awt.Color(240, 248, 255));
        jTextMensaje.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextMensaje.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jTextMensaje, javax.swing.GroupLayout.PREFERRED_SIZE, 471, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 6, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextMensaje, javax.swing.GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(70, 70, 70)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 609, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(60, 60, 60))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 851, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 6, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnTicketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTicketActionPerformed
       if (mesaSeleccionada == null || !mesaSeleccionada.isOcupada()) {
            mensajeError("Selecciona una mesa con productos primero");
            return;
        }
        mostrarMensaje("PREPARANDO TICKET de la Mesa " + mesaSeleccionada.getNumero());
        imprimirDocumento("TICKET", null);
    }//GEN-LAST:event_btnTicketActionPerformed

    private void btnEfectivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEfectivoActionPerformed
      if (mesaSeleccionada == null || !mesaSeleccionada.isOcupada()) {
            mensajeError("Selecciona una mesa con cuenta activa.");
            return;
        }
        
        double total = mesaSeleccionada.getTotal() * 1.10;
        estadoFlujo = EstadoFlujo.ESPERANDO_EFECTIVO;
        operacionActual = "";
        displayCalculadora.setText("0");
        
        mostrarMensaje("PAGO EN EFECTIVO | Total: " + formatoEuros(total) + " | Ingresa el dinero recibido en la calculadora y pulsa '='");
    
    }//GEN-LAST:event_btnEfectivoActionPerformed

    private void btnMesa6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMesa6ActionPerformed
        seleccionarMesa(6);
       
    }//GEN-LAST:event_btnMesa6ActionPerformed

    private void btnMesa8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMesa8ActionPerformed
        seleccionarMesa(8);
        
    }//GEN-LAST:event_btnMesa8ActionPerformed

    private void btnBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarActionPerformed
        int fila = tablaProductos.getSelectedRow();
        if (mesaSeleccionada == null || fila < 0) {
            mensajeError("Selecciona una línea de la tabla primero.");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar este producto?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            mesaSeleccionada.eliminarLinea(fila);
            recargarTabla();
            actualizarVisualMesas();
            mostrarMensaje("ELIMINADO: Producto eliminado de la cuenta.");
        }
    }//GEN-LAST:event_btnBorrarActionPerformed

    private void btn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn2ActionPerformed
        ingresarNumero("2");
    }//GEN-LAST:event_btn2ActionPerformed

    private void btnCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCActionPerformed
       if (!modoCalculadoraActivo) return;
        
        if (estadoFlujo != EstadoFlujo.NORMAL) {
            estadoFlujo = EstadoFlujo.NORMAL;
            cantidadTemporal = 0;
            nombreProductoTemporal = "";
            mostrarMensaje("CANCELADO: Operación cancelada. ");
        }
        
        operacionActual = "";
        resultadoCalculadora = 0;
        displayCalculadora.setText("0");
    }//GEN-LAST:event_btnCActionPerformed

    private void btnBebidasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBebidasActionPerformed
       if (mesaSeleccionada == null) {
            mensajeError("Primero selecciona una MESA");
            return;
        }
        Bebidas dialogoBebidas = new Bebidas(this, true, todosProductos, this);
        dialogoBebidas.setVisible(true);
        
    }//GEN-LAST:event_btnBebidasActionPerformed

    private void btnMesa1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMesa1ActionPerformed
        seleccionarMesa(1);
    }//GEN-LAST:event_btnMesa1ActionPerformed

    private void displayCalculadoraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_displayCalculadoraActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_displayCalculadoraActionPerformed

    private void btn5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn5ActionPerformed
       ingresarNumero("5");
    }//GEN-LAST:event_btn5ActionPerformed

    private void lblSubtotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lblSubtotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lblSubtotalActionPerformed

    private void btnComidasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnComidasActionPerformed
        if (mesaSeleccionada == null) {
            mensajeError("Primero selecciona una MESA");
            return;
        }
        Comidas dialogoComidas = new Comidas(this, true, todosProductos, this);
        dialogoComidas.setVisible(true);
    }//GEN-LAST:event_btnComidasActionPerformed

    private void btnModoCalculadoraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModoCalculadoraActionPerformed
         modoCalculadoraActivo = !modoCalculadoraActivo;
        if (modoCalculadoraActivo) {
            operacionActual = "";
            resultadoCalculadora = 0;
            displayCalculadora.setText("0");
        }
        actualizarEstadosVisuales();
        mostrarMensaje(modoCalculadoraActivo ? "MODO CALCULADORA ACTIVADO" : "MODO PRODUCTO ACTIVADO");
    }//GEN-LAST:event_btnModoCalculadoraActionPerformed
 
    private void btn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn1ActionPerformed
        ingresarNumero("1"); 
    }//GEN-LAST:event_btn1ActionPerformed

    private void btn3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn3ActionPerformed
         ingresarNumero("3"); 
    }//GEN-LAST:event_btn3ActionPerformed

    private void btn4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn4ActionPerformed
         ingresarNumero("4"); 
    }//GEN-LAST:event_btn4ActionPerformed

    private void btnIgualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIgualActionPerformed
         if (!modoCalculadoraActivo) return;

        if (estadoFlujo == EstadoFlujo.ESPERANDO_CANTIDAD) {
            try {
                cantidadTemporal = Integer.parseInt(displayCalculadora.getText());
                if (cantidadTemporal <= 0) {
                    mensajeError("La cantidad debe ser mayor que 0. Intenta de nuevo.");
                    return;
                }
                estadoFlujo = EstadoFlujo.ESPERANDO_PRECIO;
                operacionActual = "";
                displayCalculadora.setText("0");
                mostrarMensaje("PASO 2/2: Ingresa el PRECIO de '" + nombreProductoTemporal + "' y pulsa '='");
            } catch (NumberFormatException e) {
                mensajeError("Cantidad no válida. Usa solo números y pulsa '=' de nuevo.");
            }
            return;
        }

        if (estadoFlujo == EstadoFlujo.ESPERANDO_PRECIO) {
            try {
                double precio = parsearNumero(displayCalculadora.getText());
                if (precio <= 0) {
                    mensajeError("El precio debe ser mayor que 0. Intenta de nuevo.");
                    return;
                }
                
                Producto productoNuevo = new Producto(nombreProductoTemporal, precio, "Manual");
                mesaSeleccionada.agregarProducto(productoNuevo, cantidadTemporal);
                recargarTabla();
                actualizarVisualMesas();
                
                double importe = precio * cantidadTemporal;
                mostrarMensaje("AÑADIDO: '" + nombreProductoTemporal + "' | Cant: " + cantidadTemporal + " | Precio: " + formatoEuros(precio) + " | Total: " + formatoEuros(importe));
                
                estadoFlujo = EstadoFlujo.NORMAL;
                nombreProductoTemporal = "";
                cantidadTemporal = 0;
                operacionActual = "";
                displayCalculadora.setText("0");
                
            } catch (NumberFormatException e) {
                mensajeError("Precio no válido. Usa números y punto decimal (ej: 2.50)");
            }
            return;
        }

        if (estadoFlujo == EstadoFlujo.ESPERANDO_EFECTIVO) {
            try {
                double recibido = parsearNumero(displayCalculadora.getText());
                double total = mesaSeleccionada.getTotal() * 1.10;
                
                if (recibido >= total) {
                    double cambio = recibido - total;
                    mostrarMensaje("PAGO RECIBIDO: " + formatoEuros(recibido) + " | Cambio: " + formatoEuros(cambio) + " | ¡Completado!");
                    
                    imprimirDocumento("TICKET", null);
                    mesaSeleccionada.limpiar();
                    limpiarInterfaz();
                } else {
                    double falta = total - recibido;
                    mensajeError("Importe insuficiente. Faltan: " + formatoEuros(falta));
                }
            } catch (NumberFormatException e) {
                mensajeError("Importe no válido. Ingresa el número y pulsa '='");
            }
            return;
        }

        try {
            double resultado = evaluarExpresion(operacionActual);
            displayCalculadora.setText(String.valueOf(resultado));
            mostrarMensaje("CALCULADORA: " + operacionActual + " = " + resultado);
        } catch (Exception e) {
            displayCalculadora.setText("Error");
            operacionActual = "";
            mensajeError("Error en la operación. Pulsa 'C' para limpiar.");
        }
    }//GEN-LAST:event_btnIgualActionPerformed

    private void btn6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn6ActionPerformed
       ingresarNumero("6");
    }//GEN-LAST:event_btn6ActionPerformed

    private void btn7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn7ActionPerformed
        ingresarNumero("7");
    }//GEN-LAST:event_btn7ActionPerformed

    private void btn8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn8ActionPerformed
        ingresarNumero("8");
    }//GEN-LAST:event_btn8ActionPerformed

    private void btn9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn9ActionPerformed
        ingresarNumero("9");
    }//GEN-LAST:event_btn9ActionPerformed

    private void btnSumaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSumaActionPerformed
       if (!modoCalculadoraActivo) return;
        operacionActual += "+";
        displayCalculadora.setText(operacionActual);
    }//GEN-LAST:event_btnSumaActionPerformed

    private void btnRestaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRestaActionPerformed
        if (!modoCalculadoraActivo) return;
        operacionActual += "-";
        displayCalculadora.setText(operacionActual);
    }//GEN-LAST:event_btnRestaActionPerformed

    private void btnMultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMultActionPerformed
        if (!modoCalculadoraActivo) return;
        operacionActual += "x";
        displayCalculadora.setText(operacionActual);
    }//GEN-LAST:event_btnMultActionPerformed

    private void btnDividirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDividirActionPerformed
         if (!modoCalculadoraActivo) return;
        operacionActual += "/";
        displayCalculadora.setText(operacionActual);
    }//GEN-LAST:event_btnDividirActionPerformed

    private void btnPuntoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPuntoActionPerformed
        if (!modoCalculadoraActivo) return;
        operacionActual += ".";
        displayCalculadora.setText(operacionActual);
    }//GEN-LAST:event_btnPuntoActionPerformed

    private void btnAñadirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAñadirActionPerformed
        if (!modoCalculadoraActivo) {
            mensajeError("Activa el 'Modo Calculadora' primero (botón azul)");
            return;
        }
        if (mesaSeleccionada == null) {
            mensajeError("Primero selecciona una mesa haciendo clic en ella");
            return;
        }
        
        String nombre = JOptionPane.showInputDialog(this, "¿Cómo se llama el producto?", "Nuevo Producto", JOptionPane.QUESTION_MESSAGE);
        if (nombre == null || nombre.trim().isEmpty()) {
            mostrarMensaje("CANCELADO: Operación cancelada.");
            return;
        }
        
        nombreProductoTemporal = nombre.trim();
        estadoFlujo = EstadoFlujo.ESPERANDO_CANTIDAD;
        cantidadTemporal = 0;
        operacionActual = "";
        displayCalculadora.setText("0");
        
        mostrarMensaje("PASO 1/2: Ingresa la CANTIDAD para '" + nombreProductoTemporal + "' en la calculadora y pulsa '='");
    }//GEN-LAST:event_btnAñadirActionPerformed

    private void btnMesa2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMesa2ActionPerformed
        seleccionarMesa(2);
        
    }//GEN-LAST:event_btnMesa2ActionPerformed

    private void btnMesa3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMesa3ActionPerformed
        seleccionarMesa(3);
        
    }//GEN-LAST:event_btnMesa3ActionPerformed

    private void btnMesa4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMesa4ActionPerformed
        seleccionarMesa(4);
    }//GEN-LAST:event_btnMesa4ActionPerformed

    private void btnMesa5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMesa5ActionPerformed
        seleccionarMesa(5);
    }//GEN-LAST:event_btnMesa5ActionPerformed

    private void btnMesa7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMesa7ActionPerformed
        seleccionarMesa(7);
        
    }//GEN-LAST:event_btnMesa7ActionPerformed

    private void btnFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFacturaActionPerformed
        if (mesaSeleccionada == null || !mesaSeleccionada.isOcupada()) {
            mensajeError("Selecciona una mesa con productos primero");
            return;
        }
        
        mostrarMensaje("PREPARANDO FACTURA para Mesa " + mesaSeleccionada.getNumero());
        
        String idCliente = JOptionPane.showInputDialog(this, "Introduce DNI, NIE o CIF del cliente:");
        if (idCliente == null || idCliente.trim().isEmpty()) {
            mostrarMensaje("CANCELADO: Operación de factura cancelada.");
            return;
        }
        
        procesarPago(mesaSeleccionada, "FACTURA", idCliente.trim().toUpperCase());
    }//GEN-LAST:event_btnFacturaActionPerformed

    private void btn0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn0ActionPerformed
        ingresarNumero("0");
    }//GEN-LAST:event_btn0ActionPerformed

    private void btnTarjetaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTarjetaActionPerformed
        if (mesaSeleccionada == null || !mesaSeleccionada.isOcupada()) {
            mensajeError("Selecciona una mesa con cuenta activa.");
            return;
        }
        mostrarMensaje("PROCESANDO PAGO CON TARJETA...");
        procesarPagoTarjeta(mesaSeleccionada, "TICKET", null);
    }//GEN-LAST:event_btnTarjetaActionPerformed
    
   
    
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new Main().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn0;
    private javax.swing.JButton btn1;
    private javax.swing.JButton btn10;
    private javax.swing.JButton btn2;
    private javax.swing.JButton btn3;
    private javax.swing.JButton btn4;
    private javax.swing.JButton btn5;
    private javax.swing.JButton btn6;
    private javax.swing.JButton btn7;
    private javax.swing.JButton btn8;
    private javax.swing.JButton btn9;
    private javax.swing.JButton btnAñadir;
    private javax.swing.JButton btnBebidas;
    private javax.swing.JButton btnBorrar;
    private javax.swing.JButton btnC;
    private javax.swing.JButton btnComidas;
    private javax.swing.JButton btnDividir;
    private javax.swing.JButton btnEfectivo;
    private javax.swing.JButton btnFactura;
    private javax.swing.JButton btnIgual;
    private javax.swing.JButton btnMesa1;
    private javax.swing.JButton btnMesa2;
    private javax.swing.JButton btnMesa3;
    private javax.swing.JButton btnMesa4;
    private javax.swing.JButton btnMesa5;
    private javax.swing.JButton btnMesa6;
    private javax.swing.JButton btnMesa7;
    private javax.swing.JButton btnMesa8;
    private javax.swing.JButton btnModoCalculadora;
    private javax.swing.JButton btnMult;
    private javax.swing.JButton btnPunto;
    private javax.swing.JButton btnResta;
    private javax.swing.JButton btnSuma;
    private javax.swing.JButton btnTarjeta;
    private javax.swing.JButton btnTicket;
    private javax.swing.JTextField displayCalculadora;
    private javax.swing.JLabel jLabelEmpresa;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanelBotonesCalculadora;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextMensaje;
    private javax.swing.JTextField lblIva;
    private javax.swing.JTextField lblSubtotal;
    private javax.swing.JTextField lblTotal;
    private javax.swing.JTable tablaProductos;
    // End of variables declaration//GEN-END:variables
}
