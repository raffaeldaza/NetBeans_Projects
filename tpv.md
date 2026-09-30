# GUÍA DE DESARROLLO: TPV CAFETERÍA (UD3 - Desarrollo de Interfaces)

## 1. PLANIFICACIÓN DE PANTALLAS (WIREFRAMES)

### PANTALLA 1: Principal (JFrame)
Tamaño recomendado: 1200 x 700 px (Cumple el requisito de <1500x800).

┌─────────────────────────────────────────────────────────────────┐
│  ☕ CAFETERÍA SAN VIATOR                    [—] [□] [X]         │
├─────────────────────────────────────────────────────────────────┤
│  [🥤 Bebidas]  [🥪 Comida]  [🧾 Ticket]  [ Factura]      │
├──────────────────────────────┬──────────────────────────────────┤
│                              │  ┌──────────────────────────┐    │
│   PRODUCTO    CANT  IMPORTE  │  │  DISPLAY CALCULADORA     │    │
│  ┌────────────────────────┐  │  │         0.00 €           │    │
│  │Café solo    2    3.00€ │  │  └──────────────────────────┘    │
│  │Coca-Cola    1    1.50€ │  │                                  │
│  │Bocadillo    1    4.50€ │  │  [C]  [±]  [%]  [÷]             │
│  │                        │  │  [7]  [8]  [9]  [×]             │
│  │                        │  │  [4]  [5]  [6]  [-]             │
│  │                        │  │  [1]  [2]  [3]  [+]             │
│  │                        │  │  [0]  [.]  [=]  [MODO]          │
│  └────────────────────────┘  │                                  │
│                              │  MODO: [🧮 Calculadora]          │
│  SUBTOTAL:      9.00 €      │                                  │
│  IVA (21%):     1.89 €      │  CANTIDAD: [  1  ]               │
│  ─────────────────────       │                                  │
│  TOTAL:        10.89 €      │  [➕ AÑADIR PRODUCTO]            │
│                              │  [️ BORRAR SELECCIÓN]          │
──────────────────────────────┴──────────────────────────────────┘

### PANTALLA 2 y 3: Bebidas / Comida (JDialog Modal)
┌─────────────────────────────────────┐
│  Selecciona Bebida              [X] │
├─────────────────────────────────────┤
│  [☕ Café solo    - 1.50€]          │
│  [☕ Café con leche - 1.80€]        │
│  [🥤 Coca-Cola    - 1.50€]         │
│  ...                               │
│         [❌ Cancelar]               │
└─────────────────────────────────────┘

### PANTALLA 4: Ticket / Factura (JDialog)
┌──────────────────────────────────────────┐
│  TICKET                              [X] │
├──────────────────────────────────────────
│  ════════════════════════════════════    │
│       CAFETERÍA SAN VIATOR               │
│    C/ Ejemplo 123, Valladolid            │
│    CIF: B12345678                        │
│  ════════════════════════════════════    │
│  Café solo       2 x 1.50€ =  3.00€     │
│  Coca-Cola       1 x 1.50€ =  1.50€     │
│  ────────────────────────────────        │
│  SUBTOTAL:                  9.00€       │
│  IVA 21%:                   1.89€       │
│  TOTAL:                    10.89€       │
│  Forma de pago: [ Efectivo] [💳 Tarjeta]│
│         [🖨️ Imprimir]  [✖ Cerrar]       │
──────────────────────────────────────────┘


## 2. ESTRUCTURA DEL PROYECTO EN NETBEANS

proyecto-tpv/
── src/
│   ├── tpv/                    (Paquete principal - Interfaces)
│   │   ├── Principal.java      (JFrame)
│   │   ├── Bebidas.java        (JDialog)
│   │   ├── Comida.java         (JDialog)
│   │   └── Ticket.java         (JDialog)
│   ├── dto/                    (Paquete de Datos)
│   │   └── Producto.java       (Clase con nombre, precio, categoría)
│   └── util/                   (Paquete de Utilidades)
│       └── GestorPrecios.java  (Lectura del fichero)
├── recursos/
│   └── precios.txt             (Fichero de texto con los precios)


## 3. FICHERO DE PRECIOS (recursos/precios.txt)

# Formato: categoria;nombre;precio
bebida;Café solo;1.50
bebida;Café con leche;1.80
bebida;Batido vainilla;2.50
bebida;Coca-Cola;1.50
bebida;Kas limón;1.50
bebida;Agua mineral;1.00
bebida;Cerveza;2.00
bebida;Vino tinto;2.50
comida;Bocadillo jamón;4.50
comida;Bocadillo queso;4.00
comida;Pizza individual;5.50
comida;Ensalada mixta;3.50
comida;Patatas fritas;2.50
comida;Croissant;1.80
comida;Tarta del día;3.00


## 4. CÓDIGO BASE (DTO Y UTILIDADES)

### Clase Producto.java (Paquete dto)
```java
package dto;

public class Producto {
    private String nombre;
    private double precio;
    private String categoria;

    public Producto(String nombre, double precio, String categoria) {
        this.nombre = nombre;
        this.precio = precio;
        this.categoria = categoria;
    }

    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
    public String getCategoria() { return categoria; }

    @Override
    public String toString() {
        return nombre + " - " + String.format("%.2f€", precio);
    }
}# GUÍA DE DESARROLLO: TPV CAFETERÍA (UD3 - Desarrollo de Interfaces)

## 1. PLANIFICACIÓN DE PANTALLAS (WIREFRAMES)

### PANTALLA 1: Principal (JFrame)
Tamaño recomendado: 1200 x 700 px (Cumple el requisito de <1500x800).

┌─────────────────────────────────────────────────────────────────┐
│  ☕ CAFETERÍA SAN VIATOR                    [—] [□] [X]         │
├─────────────────────────────────────────────────────────────────┤
│  [🥤 Bebidas]  [🥪 Comida]  [🧾 Ticket]  [ Factura]      │
├──────────────────────────────┬──────────────────────────────────┤
│                              │  ┌──────────────────────────┐    │
│   PRODUCTO    CANT  IMPORTE  │  │  DISPLAY CALCULADORA     │    │
│  ┌────────────────────────┐  │  │         0.00 €           │    │
│  │Café solo    2    3.00€ │  │  └──────────────────────────┘    │
│  │Coca-Cola    1    1.50€ │  │                                  │
│  │Bocadillo    1    4.50€ │  │  [C]  [±]  [%]  [÷]             │
│  │                        │  │  [7]  [8]  [9]  [×]             │
│  │                        │  │  [4]  [5]  [6]  [-]             │
│  │                        │  │  [1]  [2]  [3]  [+]             │
│  │                        │  │  [0]  [.]  [=]  [MODO]          │
│  └────────────────────────┘  │                                  │
│                              │  MODO: [🧮 Calculadora]          │
│  SUBTOTAL:      9.00 €      │                                  │
│  IVA (21%):     1.89 €      │  CANTIDAD: [  1  ]               │
│  ─────────────────────       │                                  │
│  TOTAL:        10.89 €      │  [➕ AÑADIR PRODUCTO]            │
│                              │  [️ BORRAR SELECCIÓN]          │
──────────────────────────────┴──────────────────────────────────┘

### PANTALLA 2 y 3: Bebidas / Comida (JDialog Modal)
┌─────────────────────────────────────┐
│  Selecciona Bebida              [X] │
├─────────────────────────────────────┤
│  [☕ Café solo    - 1.50€]          │
│  [☕ Café con leche - 1.80€]        │
│  [🥤 Coca-Cola    - 1.50€]         │
│  ...                               │
│         [❌ Cancelar]               │
└─────────────────────────────────────┘

### PANTALLA 4: Ticket / Factura (JDialog)
┌──────────────────────────────────────────┐
│  TICKET                              [X] │
├──────────────────────────────────────────
│  ════════════════════════════════════    │
│       CAFETERÍA SAN VIATOR               │
│    C/ Ejemplo 123, Valladolid            │
│    CIF: B12345678                        │
│  ════════════════════════════════════    │
│  Café solo       2 x 1.50€ =  3.00€     │
│  Coca-Cola       1 x 1.50€ =  1.50€     │
│  ────────────────────────────────        │
│  SUBTOTAL:                  9.00€       │
│  IVA 21%:                   1.89€       │
│  TOTAL:                    10.89€       │
│  Forma de pago: [ Efectivo] [💳 Tarjeta]│
│         [🖨️ Imprimir]  [✖ Cerrar]       │
──────────────────────────────────────────┘


## 2. ESTRUCTURA DEL PROYECTO EN NETBEANS

proyecto-tpv/
── src/
│   ├── tpv/                    (Paquete principal - Interfaces)
│   │   ├── Principal.java      (JFrame)
│   │   ├── Bebidas.java        (JDialog)
│   │   ├── Comida.java         (JDialog)
│   │   └── Ticket.java         (JDialog)
│   ├── dto/                    (Paquete de Datos)
│   │   └── Producto.java       (Clase con nombre, precio, categoría)
│   └── util/                   (Paquete de Utilidades)
│       └── GestorPrecios.java  (Lectura del fichero)
├── recursos/
│   └── precios.txt             (Fichero de texto con los precios)


## 3. FICHERO DE PRECIOS (recursos/precios.txt)

# Formato: categoria;nombre;precio
bebida;Café solo;1.50
bebida;Café con leche;1.80
bebida;Batido vainilla;2.50
bebida;Coca-Cola;1.50
bebida;Kas limón;1.50
bebida;Agua mineral;1.00
bebida;Cerveza;2.00
bebida;Vino tinto;2.50
comida;Bocadillo jamón;4.50
comida;Bocadillo queso;4.00
comida;Pizza individual;5.50
comida;Ensalada mixta;3.50
comida;Patatas fritas;2.50
comida;Croissant;1.80
comida;Tarta del día;3.00


## 4. CÓDIGO BASE (DTO Y UTILIDADES)

### Clase Producto.java (Paquete dto)
```java
package dto;

public class Producto {
    private String nombre;
    private double precio;
    private String categoria;

    public Producto(String nombre, double precio, String categoria) {
        this.nombre = nombre;
        this.precio = precio;
        this.categoria = categoria;
    }

    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
    public String getCategoria() { return categoria; }

    @Override
    public String toString() {
        return nombre + " - " + String.format("%.2f€", precio);
    }
}# GUÍA DE DESARROLLO: TPV CAFETERÍA (UD3 - Desarrollo de Interfaces)

## 1. PLANIFICACIÓN DE PANTALLAS (WIREFRAMES)

### PANTALLA 1: Principal (JFrame)
Tamaño recomendado: 1200 x 700 px (Cumple el requisito de <1500x800).

┌─────────────────────────────────────────────────────────────────┐
│  ☕ CAFETERÍA SAN VIATOR                    [—] [□] [X]         │
├─────────────────────────────────────────────────────────────────┤
│  [🥤 Bebidas]  [🥪 Comida]  [🧾 Ticket]  [ Factura]      │
├──────────────────────────────┬──────────────────────────────────┤
│                              │  ┌──────────────────────────┐    │
│   PRODUCTO    CANT  IMPORTE  │  │  DISPLAY CALCULADORA     │    │
│  ┌────────────────────────┐  │  │         0.00 €           │    │
│  │Café solo    2    3.00€ │  │  └──────────────────────────┘    │
│  │Coca-Cola    1    1.50€ │  │                                  │
│  │Bocadillo    1    4.50€ │  │  [C]  [±]  [%]  [÷]             │
│  │                        │  │  [7]  [8]  [9]  [×]             │
│  │                        │  │  [4]  [5]  [6]  [-]             │
│  │                        │  │  [1]  [2]  [3]  [+]             │
│  │                        │  │  [0]  [.]  [=]  [MODO]          │
│  └────────────────────────┘  │                                  │
│                              │  MODO: [🧮 Calculadora]          │
│  SUBTOTAL:      9.00 €      │                                  │
│  IVA (21%):     1.89 €      │  CANTIDAD: [  1  ]               │
│  ─────────────────────       │                                  │
│  TOTAL:        10.89 €      │  [➕ AÑADIR PRODUCTO]            │
│                              │  [️ BORRAR SELECCIÓN]          │
──────────────────────────────┴──────────────────────────────────┘

### PANTALLA 2 y 3: Bebidas / Comida (JDialog Modal)
┌─────────────────────────────────────┐
│  Selecciona Bebida              [X] │
├─────────────────────────────────────┤
│  [☕ Café solo    - 1.50€]          │
│  [☕ Café con leche - 1.80€]        │
│  [🥤 Coca-Cola    - 1.50€]         │
│  ...                               │
│         [❌ Cancelar]               │
└─────────────────────────────────────┘

### PANTALLA 4: Ticket / Factura (JDialog)
┌──────────────────────────────────────────┐
│  TICKET                              [X] │
├──────────────────────────────────────────
│  ════════════════════════════════════    │
│       CAFETERÍA SAN VIATOR               │
│    C/ Ejemplo 123, Valladolid            │
│    CIF: B12345678                        │
│  ════════════════════════════════════    │
│  Café solo       2 x 1.50€ =  3.00€     │
│  Coca-Cola       1 x 1.50€ =  1.50€     │
│  ────────────────────────────────        │
│  SUBTOTAL:                  9.00€       │
│  IVA 21%:                   1.89€       │
│  TOTAL:                    10.89€       │
│  Forma de pago: [ Efectivo] [💳 Tarjeta]│
│         [🖨️ Imprimir]  [✖ Cerrar]       │
──────────────────────────────────────────┘


## 2. ESTRUCTURA DEL PROYECTO EN NETBEANS

proyecto-tpv/
── src/
│   ├── tpv/                    (Paquete principal - Interfaces)
│   │   ├── Principal.java      (JFrame)
│   │   ├── Bebidas.java        (JDialog)
│   │   ├── Comida.java         (JDialog)
│   │   └── Ticket.java         (JDialog)
│   ├── dto/                    (Paquete de Datos)
│   │   └── Producto.java       (Clase con nombre, precio, categoría)
│   └── util/                   (Paquete de Utilidades)
│       └── GestorPrecios.java  (Lectura del fichero)
├── recursos/
│   └── precios.txt             (Fichero de texto con los precios)


## 3. FICHERO DE PRECIOS (recursos/precios.txt)

# Formato: categoria;nombre;precio
bebida;Café solo;1.50
bebida;Café con leche;1.80
bebida;Batido vainilla;2.50
bebida;Coca-Cola;1.50
bebida;Kas limón;1.50
bebida;Agua mineral;1.00
bebida;Cerveza;2.00
bebida;Vino tinto;2.50
comida;Bocadillo jamón;4.50
comida;Bocadillo queso;4.00
comida;Pizza individual;5.50
comida;Ensalada mixta;3.50
comida;Patatas fritas;2.50
comida;Croissant;1.80
comida;Tarta del día;3.00


## 4. CÓDIGO BASE (DTO Y UTILIDADES)

### Clase Producto.java (Paquete dto)
```java
package dto;

public class Producto {
    private String nombre;
    private double precio;
    private String categoria;

    public Producto(String nombre, double precio, String categoria) {
        this.nombre = nombre;
        this.precio = precio;
        this.categoria = categoria;
    }

    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
    public String getCategoria() { return categoria; }

    @Override
    public String toString() {
        return nombre + " - " + String.format("%.2f€", precio);
    }
}