/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 *
 * @author DAM_204
 */
public class Mesa {
    
   private int numero;
    private boolean ocupada;
    private List<String[]> pedido;
    private String camarero;

    public Mesa(int numero) {
        this.numero = numero;
        this.ocupada = false;
        this.pedido = new ArrayList<>();
        this.camarero = null;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public boolean isOcupada() {
        return ocupada;
    }

    public void setOcupada(boolean ocupada) {
        this.ocupada = ocupada;
    }

    public List<String[]> getPedido() {
        return pedido;
    }

    public void setPedido(List<String[]> pedido) {
        this.pedido = pedido;
    }

    public String getCamarero() {
        return camarero;
    }

    public void setCamarero(String camarero) {
        this.camarero = camarero;
    }

    public void agregarProducto(Producto producto, int cantidad) {
        boolean existe = false;
        for (String[] fila : pedido) {
            if (fila[0].equals(producto.getNombre())) {
                int cantActual = Integer.parseInt(fila[1]);
                fila[1] = String.valueOf(cantActual + cantidad);
                double nuevoImporte = producto.getPrecio() * (cantActual + cantidad);
                fila[3] = String.format(Locale.US, "%.2f", nuevoImporte);
                existe = true;
                break;
            }
        }
        if (!existe) {
            double importe = producto.getPrecio() * cantidad;
            pedido.add(new String[]{
                producto.getNombre(),
                String.valueOf(cantidad),
                String.format(Locale.US, "%.2f", producto.getPrecio()),
                String.format(Locale.US, "%.2f", importe)
            });
        }
        ocupada = true;
    }

    public void eliminarLinea(int indice) {
        if (indice >= 0 && indice < pedido.size()) {
            pedido.remove(indice);
            if (pedido.isEmpty()) {
                ocupada = false;
            }
        }
    }

    public double getTotal() {
        double total = 0;
        for (String[] fila : pedido) {
            total += Double.parseDouble(fila[3]);
        }
        return total;
    }

    public void limpiar() {
        pedido.clear();
        ocupada = false;
        camarero = null;
    }
}