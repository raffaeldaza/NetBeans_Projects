/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

/**
 *
 * @author DAM_204
 */
public class LineaPedido {
    private Producto producto;
    private int cantidad;

    public LineaPedido(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }
    
    public void setCantidad(int cantidad){
        this.cantidad=cantidad;
    }
    
    public double getImporte(){
        return producto.getPrecio()*cantidad;
    }
    
}
