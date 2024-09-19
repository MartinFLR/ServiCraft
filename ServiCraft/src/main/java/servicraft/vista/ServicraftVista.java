package servicraft.vista;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ServicraftVista extends JFrame {

    private static final long serialVersionUID = 1L;
    private JButton iniciarButton;
    private JButton cerrarButton;
    private JTextArea logArea;
    private JButton executeCommandButton;
    private JTextArea commandLogArea;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                ServicraftVista window = new ServicraftVista();
                window.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the application.
     */
    public ServicraftVista() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
    	setTitle("ServiCraft");
        setBounds(100, 100, 450, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        getContentPane().add(tabbedPane, BorderLayout.CENTER);
        
        // Panel de controles
        JPanel controlPanel = new JPanel(new BorderLayout());
        tabbedPane.addTab("Controles", controlPanel);
        
        logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(logArea);
        logScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        controlPanel.add(logScrollPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel();
        iniciarButton = new JButton("Iniciar Servidor");
        buttonPanel.add(iniciarButton);
        
        cerrarButton = new JButton("Cerrar Servidor");
        buttonPanel.add(cerrarButton);
        
        controlPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Pestaña para ejecutar comandos personalizados
        JPanel commandPanel = new JPanel(new BorderLayout());
        tabbedPane.addTab("Comandos", commandPanel);
        
        commandLogArea = new JTextArea();
        JScrollPane commandLogScrollPane = new JScrollPane(commandLogArea);
        commandLogScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        commandPanel.add(commandLogScrollPane, BorderLayout.CENTER);
        
        executeCommandButton = new JButton("Ejecutar Comando");
        
        JPanel commandButtonPanel = new JPanel();
        commandButtonPanel.add(executeCommandButton);
        commandPanel.add(commandButtonPanel, BorderLayout.SOUTH);
    }


    public JButton getIniciarButton() {
        return iniciarButton;
    }

    public JButton getCerrarButton() {
        return cerrarButton;
    }

    public JTextArea getLogArea() {
        return logArea;
    }
    


	public JTextArea getCommandLogArea() {
		return commandLogArea;
	}

	public JButton getExecuteCommandButton() {
        return executeCommandButton;
    }
}
