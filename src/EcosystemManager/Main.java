package EcosystemManager;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.util.Objects;

public class Main extends Component implements ActionListener{

    public SimulationManager sManager;
    Thread GUIThread;

    private JFrame window;
    private JPanel panel;

    private JButton runButton;
    private JButton stopButton;
    private JTextField inputField;

    public static void main(String[] args) {
        Main manager = new Main();
        manager.createGUI();
    }

    public void loadSimulationManager(int wolfLimit) {
        sManager = new SimulationManager();
        sManager.initiateSimulation(wolfLimit);
    }

    public void createGUI() {
        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(true);
        window.setTitle("Ecosystem");

        panel = new JPanel();

        JButton loadButton = new JButton("Load");
        loadButton.setBounds(100, 400, 165, 25);
        loadButton.addActionListener(this);

        JButton confirmButton = new JButton("Confirm");
        confirmButton.setBounds(300, 400, 165, 25);
        confirmButton.addActionListener(this);

        inputField = new JTextField("Max wolf count");
        inputField.setBounds(200, 200, 165, 25);

        panel.add(loadButton);
        panel.add(confirmButton);
        panel.add(inputField);

        window.add(panel, BorderLayout.CENTER);
        window.pack();
        window.setSize(500, 500);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    public void simulationGUI() {

        panel.removeAll();

        JButton saveButton = new JButton("Save");
        saveButton.setBounds((sManager.getEnvironmentSize() * SimulationManager.tileSize) / 2,
                sManager.getEnvironmentSize() * SimulationManager.tileSize + SimulationManager.tileSize,
                165, 25);
        saveButton.addActionListener(this);

        runButton = new JButton("Start");
        runButton.setBounds((sManager.getEnvironmentSize() * SimulationManager.tileSize) / 2 + 200,
                sManager.getEnvironmentSize() * SimulationManager.tileSize + SimulationManager.tileSize,
                165, 25);
        runButton.addActionListener(this);
        runButton.setVisible(false);

        stopButton = new JButton("Stop");
        stopButton.setBounds((sManager.getEnvironmentSize() * SimulationManager.tileSize) / 2 + 200,
                sManager.getEnvironmentSize() * SimulationManager.tileSize + SimulationManager.tileSize,
                165, 25);
        stopButton.addActionListener(this);

        panel.add(sManager);
        panel.add(saveButton);
        panel.add(runButton);
        panel.add(stopButton);
//        panel.setLayout(null);

        window.add(panel, BorderLayout.CENTER);
        window.pack();
        window.setSize(sManager.getEnvironmentSize() * SimulationManager.tileSize + 100,
                sManager.getEnvironmentSize() * SimulationManager.tileSize + 100);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(Objects.equals(e.getActionCommand(), "Save")) {
            Thread t = new Thread() {
                public void run() {
                    sManager.saveState();
                }
            };
            t.start();
        }
        else if (Objects.equals(e.getActionCommand(), "Stop")) {
            sManager.pause();
            stopButton.setVisible(false);
            runButton.setVisible(true);
        }
        else if (Objects.equals(e.getActionCommand(), "Start")) {
            sManager.resume();
            stopButton.setVisible(true);
            runButton.setVisible(false);
        }
        else if (Objects.equals(e.getActionCommand(), "Confirm")) {
            try{
                String input = inputField.getText();
                loadSimulationManager(Integer.parseInt(input));
                simulationGUI();
                sManager.startSimulationThread();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        else if (Objects.equals(e.getActionCommand(), "Load")) {
            loadState();
        }
        else
            System.out.println("Else");
    }

    private void loadState() {
        try {
            JFileChooser fileChooser = new JFileChooser("C:\\Users\\Titas\\Desktop\\folder\\VU\\1k. 2sem\\Objektinis\\Ecosystem\\saves\\");
            int result = fileChooser.showOpenDialog(window);
            if (result == JFileChooser.APPROVE_OPTION) {
                java.io.File file = fileChooser.getSelectedFile();
                ObjectInputStream in = new ObjectInputStream(Files.newInputStream(file.toPath()));
                this.sManager = (SimulationManager) in.readObject();
                in.close();
                JOptionPane.showMessageDialog(window, "State loaded successfully!");
                simulationGUI();
                sManager.startSimulationThread();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading state: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}