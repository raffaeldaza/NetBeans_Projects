/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DAM_204
 */



public class GestorProductos {

    public static List<Producto> cargarProductos(String rutaFichero) {
        List<Producto> productos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(rutaFichero))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                // Ignorar comentarios o líneas vacías
                if (linea.startsWith("#") || linea.trim().isEmpty()) continue;
                
                String[] partes = linea.split(";");
                if (partes.length == 3) {
                    productos.add(new Producto(partes[1], Double.parseDouble(partes[2]), partes[0]));
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el fichero de productos: " + e.getMessage());
        }
        return productos;
    }

    public static List<Producto> filtrar(List<Producto> todos, String categoria) {
        List<Producto> filtrados = new ArrayList<>();
        for (Producto p : todos) {
            if (p.getCategoria().equalsIgnoreCase(categoria)) {
                filtrados.add(p);
            }
        }
        return filtrados;
    }
}