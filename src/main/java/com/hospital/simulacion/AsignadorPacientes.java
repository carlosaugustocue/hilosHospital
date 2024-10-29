package com.hospital.simulacion;

import com.hospital.modelo.Paciente;
import java.util.concurrent.BlockingQueue;

public class AsignadorPacientes implements Runnable {
    private BlockingQueue<Paciente> colaPacientes;
    private int totalPacientes;

    public AsignadorPacientes(BlockingQueue<Paciente> colaPacientes, int totalPacientes) {
        this.colaPacientes = colaPacientes;
        this.totalPacientes = totalPacientes;
    }

    @Override
    public void run() {
        for (int i = 0; i < totalPacientes; i++) {
            Paciente paciente = new Paciente();
            try {
                colaPacientes.put(paciente);
                System.out.println("Paciente " + paciente.getId() + " ha llegado y espera ser atendido.");
                Thread.sleep((int) (Math.random() * 2000) + 500); // Tiempo entre llegada de pacientes
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
