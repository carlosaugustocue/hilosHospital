package com.hospital.principal;

import com.hospital.modelo.Medico;
import com.hospital.modelo.Paciente;
import com.hospital.simulacion.AsignadorPacientes;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Hospital {
    private static List<Thread> hilosMedicos = new ArrayList<>();
    private static List<Medico> medicos = new ArrayList<>();
    private static Thread hiloAsignador;
    private static BlockingQueue<Paciente> colaPacientes;
    private static boolean simulacionIniciada = false;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int numeroMedicos = 3; // Puedes cambiar el número de médicos
        int totalPacientes = 10; // Número total de pacientes en la simulación
        colaPacientes = new ArrayBlockingQueue<>(totalPacientes);

        while (true) {
            System.out.println("----- Área de Atención Médica -----");
            System.out.println("1. Iniciar simulación");
            System.out.println("2. Ver resultados");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");

            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    if (!simulacionIniciada) {
                        iniciarSimulacion(numeroMedicos, totalPacientes);
                        simulacionIniciada = true;
                    } else {
                        System.out.println("La simulación ya está en marcha.");
                    }
                    break;
                case "2":
                    if (simulacionIniciada) {
                        mostrarResultados();
                    } else {
                        System.out.println("Debe iniciar la simulación primero.");
                    }
                    break;
                case "3":
                    detenerSimulacion();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }

    private static void iniciarSimulacion(int numeroMedicos, int totalPacientes) {
        // Crear y arrancar los hilos de los médicos
        for (int i = 1; i <= numeroMedicos; i++) {
            Medico medico = new Medico(i, colaPacientes);
            medicos.add(medico);
            Thread hiloMedico = new Thread(medico);
            hilosMedicos.add(hiloMedico);
            hiloMedico.start();
        }

        // Crear y arrancar el hilo que asigna pacientes
        AsignadorPacientes asignador = new AsignadorPacientes(colaPacientes, totalPacientes);
        hiloAsignador = new Thread(asignador);
        hiloAsignador.start();

        System.out.println("Simulación iniciada.");
    }

    private static void mostrarResultados() {
        // Esperar a que todos los pacientes sean atendidos
        try {
            hiloAsignador.join();
            for (Thread hiloMedico : hilosMedicos) {
                hiloMedico.interrupt();
            }
            for (Thread hiloMedico : hilosMedicos) {
                hiloMedico.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Mostrar el número de pacientes atendidos por cada médico
        for (Medico medico : medicos) {
            System.out.println("Médico " + medico.getIdMedico() + " atendió a " + medico.getPacientesAtendidos() + " pacientes.");
        }

        simulacionIniciada = false; // Permitir reiniciar la simulación si se desea
    }

    private static void detenerSimulacion() {
        if (simulacionIniciada) {
            hiloAsignador.interrupt();
            for (Thread hiloMedico : hilosMedicos) {
                hiloMedico.interrupt();
            }
            System.out.println("Simulación detenida.");
        }
    }
}
