/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.util.List;



/**
 *
 * @author DAM_202
 */
public class Alumno {
        
    private String nombre;
    private String apellido;
    private String dni;
    private String curso;
    private String turno;
    private List<String> asignaturas;

    public Alumno(String nombre, String apellido, String dni, String curso, String turno, List<String> asignaturas) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.curso = curso;
        this.turno = turno;
        this.asignaturas = asignaturas;
    }
    
    

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public List<String> getAsignaturas() {
        return asignaturas;
    }

    public void setAsignaturas(List<String> asignaturas) {
        this.asignaturas = asignaturas;
    }

   

    @Override
    public String toString() {
        return "Nombre: " + nombre + "\n" +
               "DNI: " + dni + "\n" +
               "Curso: " + curso + "\n" +
               "Turno: " + turno + "\n" +
               "Asignaturas: " + asignaturas;
    }
    
    
    
}
