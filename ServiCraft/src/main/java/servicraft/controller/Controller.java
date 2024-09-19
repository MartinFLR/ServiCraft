package servicraft.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import servicraft.model.DockerMinecraft;
import servicraft.vista.ServicraftVista;

public class Controller {
    ServicraftVista vista;
    DockerMinecraft modelo;
    
    public Controller(ServicraftVista vista, DockerMinecraft modelo) {
        this.vista = vista;
        this.modelo = modelo;
        inicializarListeners();
    }
    
    private void inicializarListeners() {
        vista.getIniciarButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarServidor();
            }
        });
        
        vista.getCerrarButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cerrarServidor();
            }
        });
        
        vista.getExecuteCommandButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	//vista.getCommandLogArea().setText("");
                String command = vista.getCommandLogArea().getText();
                executeDockerCommand(command);
                
            }
        });
    }
    
    private void iniciarServidor() {
    	//Comando de docker que ejecutara al iniciar el server
        String[] command = {
            "docker", "run", "-d",
            "--name", "minecraft-server",
            "-p", "25565:25565",
            "-e", "EULA=TRUE",
            "-e", "ONLINE_MODE=false",
            "-v", "./data:/data",
            "itzg/minecraft-server"
        };

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            // Leer la salida del proceso en un hilo separado
            new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String[] line = new String[1];

                    while ((line[0] = reader.readLine()) != null) {
                        SwingUtilities.invokeLater(() -> {
                            vista.getLogArea().append(line[0] + "\n");
                            vista.getLogArea().setCaretPosition(vista.getLogArea().getDocument().getLength()); // Desplaza hacia abajo
                            // Verificar si el mensaje indica que el servidor está activo
                            if (line[0].contains("Done")) {
                                JOptionPane.showMessageDialog(vista, "El servidor de Minecraft se ha iniciado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                                modelo.setServerRunning(true);
                            }
                        });
                    }
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(vista, "Error al leer la salida: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }).start();

            // Verifica el estado del contenedor
            new Thread(() -> {
                try {
                    Thread.sleep(5000);
                    String[] commandLogs = {"docker", "logs", "-f", "minecraft-server"};
                    ProcessBuilder logsBuilder = new ProcessBuilder(commandLogs);
                    Process logsProcess = logsBuilder.start();

                    try (BufferedReader logReader = new BufferedReader(new InputStreamReader(logsProcess.getInputStream()))) {
                        String[] logLine = new String[1];
                        while ((logLine[0] = logReader.readLine()) != null) {
                            SwingUtilities.invokeLater(() -> {
                                vista.getLogArea().append(logLine[0] + "\n");
                                vista.getLogArea().setCaretPosition(vista.getLogArea().getDocument().getLength());

                                // Verificar si el mensaje indica que el servidor está activo
                                if (logLine[0].contains("Done")) {
                                    JOptionPane.showMessageDialog(vista, "El servidor de Minecraft se ha iniciado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                                    modelo.setServerRunning(true);
                                }
                            });
                        }
                    }
                } catch (IOException | InterruptedException e) {
                    JOptionPane.showMessageDialog(vista, "Error al leer los logs: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }).start();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(vista, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void cerrarServidor() {
        String[] commandStop = {
            "docker", "stop", "minecraft-server"
        };

        String[] commandRm = {
            "docker", "rm", "minecraft-server"
        };

        ProcessBuilder processBuilderStop = new ProcessBuilder(commandStop);
        processBuilderStop.redirectErrorStream(true); // Combinar stdout y stderr

        try {
            Process process = processBuilderStop.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                // Solo elimina si se detuvo correctamente
                ProcessBuilder processBuilderRm = new ProcessBuilder(commandRm);
                Process processRm = processBuilderRm.start();
                
                // Leer la salida del proceso de eliminación
                String removalOutput = new BufferedReader(new InputStreamReader(processRm.getInputStream()))
                    .lines().collect(Collectors.joining("\n"));

                SwingUtilities.invokeLater(() -> {
                    vista.getLogArea().append(removalOutput + "\n");
                    JOptionPane.showMessageDialog(vista, "Servidor de Minecraft detenido y eliminado.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                });
                modelo.setServerRunning(false);
            } else {
                String errorMessage = new BufferedReader(new InputStreamReader(process.getInputStream()))
                    .lines().collect(Collectors.joining("\n"));
                JOptionPane.showMessageDialog(vista, "Error al detener el servidor: " + errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (IOException | InterruptedException e) {
            JOptionPane.showMessageDialog(vista, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    private void executeDockerCommand(String command) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("powershell.exe", "-Command", command);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                vista.getCommandLogArea().append(line + "\n");
            }
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            vista.getCommandLogArea().append("Error: " + e.getMessage() + "\n");
        }
    }
}
