package com.hospital.modelo;

public class Paciente {
    private static int contador = 1;
    private int id;
    private int tiempoAtencion; // Tiempo de atención en milisegundos

    public Paciente() {
        this.id = contador++;
        this.tiempoAtencion = (int) (Math.random() * 3000) + 1000; // Entre 1 y 4 segundos
    }

    public int getId() {
        return id;
    }

    public int getTiempoAtencion() {
        return tiempoAtencion;
    }
}
