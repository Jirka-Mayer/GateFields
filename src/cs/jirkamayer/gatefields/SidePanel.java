package cs.jirkamayer.gatefields;

import javax.swing.*;
import java.awt.*;

public class SidePanel extends JPanel {
    private JSlider simulationSpeed;
    private JLabel fpsLabel;
    private JButton simulationButton;
    private JLabel animationSpeedLabel;

    private MainWindow mainWindow;

    private static final double MAX_TIME_PER_FRAME = 1.0 / MainWindow.TARGET_FPS;
    private static final double MIN_TIME_PER_FRAME = 0.001 / MainWindow.TARGET_FPS;

    public SidePanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;

        this.setPreferredSize(new Dimension(200, 700));

        animationSpeedLabel = new JLabel();
        this.add(animationSpeedLabel);

        simulationSpeed = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        simulationSpeed.addChangeListener((e) -> this.simulationSpeedChanged());
        this.add(simulationSpeed);

        fpsLabel = new JLabel();
        this.add(fpsLabel);

        simulationButton = new JButton();
        simulationButton.setText("Stop");
        simulationButton.addActionListener((e) -> this.toggleSimulation());
        this.add(simulationButton);

        this.setFpsInfo(0, false);
        this.simulationSpeedChanged();
    }

    private void simulationSpeedChanged() {
        double sliderPosition = simulationSpeed.getValue() / 100.0;
        double slowDown = Math.pow(10, (1 - sliderPosition) * 3);

        mainWindow.simulationTimePerFrame = (1.0 / MainWindow.TARGET_FPS) / slowDown;

        animationSpeedLabel.setText("Speed 1:" + (int)Math.round(slowDown));
    }

    public void setFpsInfo(int fps, boolean maximum) {
        fpsLabel.setText("Simulation FPS: " + fps + (maximum ? " !!!" : ""));
    }

    private void toggleSimulation() {
        if (mainWindow.isSimulationRunning) {
            mainWindow.stopSimulation();
            simulationButton.setText("Start");
        } else {
            mainWindow.startSimulation();
            simulationButton.setText("Stop");
        }
    }
}
