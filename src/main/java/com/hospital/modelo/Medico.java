package com.hospital.modelo;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Medico implements Runnable {
    private int idMedico;
    private AtomicInteger pacientesAtendidos;
    private BlockingQueue<Paciente> colaPacientes;

    public Medico(int idMedico, BlockingQueue<Paciente> colaPacientes) {
        this.idMedico = idMedico;
        this.pacientesAtendidos = new AtomicInteger(0);
        this.colaPacientes = colaPacientes;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Paciente paciente = colaPacientes.take(); // Espera hasta que haya un paciente disponible
                atenderPaciente(paciente);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void atenderPaciente(Paciente paciente) throws InterruptedException {
        System.out.println("Médico " + idMedico + " está atendiendo al paciente " + paciente.getId());
        Thread.sleep(paciente.getTiempoAtencion());
        pacientesAtendidos.incrementAndGet();
        System.out.println("Médico " + idMedico + " terminó de atender al paciente " + paciente.getId());
    }

    public int getIdMedico() {
        return idMedico;
    }

    public int getPacientesAtendidos() {
        return pacientesAtendidos.get();
    }
}
