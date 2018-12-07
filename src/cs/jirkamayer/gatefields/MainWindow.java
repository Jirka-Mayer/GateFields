package cs.jirkamayer.gatefields;

import cs.jirkamayer.gatefields.editor.Selection;
import cs.jirkamayer.gatefields.math.Vector2D;
import cs.jirkamayer.gatefields.scheme.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

class MainWindow extends JFrame {

    public static final int TARGET_FPS = 30;
    public static final long TARGET_FRAME_DURATION_MS = (long)(1000.0 / TARGET_FPS);
    private static final int MINIMAL_DELAY = 10;

    private SchemeView schemeView;
    private SidePanel sidePanel;

    private Scheme scheme;
    private Selection selection;

    private Timer timer;
    private long lastFrameStart = 0;
    private long frame = 0;

    /**
     * Time that elapses in the simulation world during one frame
     * at the TARGET_FPS. If FPS is lagging, then this is lagging as well.
     */
    public double simulationTimePerFrame = 1.0;

    public boolean isSimulationRunning = false;

    MainWindow() {
        super("Gate fields");

        scheme = new Scheme();
        selection = new Selection();

        this.setupUI();

        this.openDefaultScheme();

        timer = new Timer(100, (ActionEvent e) -> {
            long frameStart = System.currentTimeMillis();

            scheme.getSimulator().simulationTick(simulationTimePerFrame);
            schemeView.draw();

            long frameEnd = System.currentTimeMillis();

            long waitFor = TARGET_FRAME_DURATION_MS - (frameEnd - frameStart);

            if (waitFor < MINIMAL_DELAY)
                waitFor = MINIMAL_DELAY;

            if (frame % 10 == 0)
                sidePanel.setFpsInfo((int)(1000.0 / (frameStart - lastFrameStart)), waitFor == MINIMAL_DELAY);

            lastFrameStart = frameStart;
            frame++;

            if (!isSimulationRunning)
                return;

            timer.setInitialDelay((int)waitFor);
            timer.start();
        });
        timer.setRepeats(false);

        this.startSimulation();
    }

    public void stopSimulation() {
        if (!isSimulationRunning)
            return;

        timer.stop();
        isSimulationRunning = false;
    }

    public void startSimulation() {
        if (isSimulationRunning)
            return;

        timer.setInitialDelay(MINIMAL_DELAY);
        timer.start();
        isSimulationRunning = true;
    }

    private void setupUI() {
        schemeView = new SchemeView(scheme, selection);
        schemeView.setSize(1000, 700);
        this.add(schemeView);

        this.setJMenuBar(
            new MainMenu(
                scheme,
                schemeView.getActionController(),
                schemeView.getCamera(),
                this
            )
        );

        sidePanel = new SidePanel(this);
        this.add(sidePanel, BorderLayout.EAST);

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.pack();
    }

    private void openDefaultScheme() {
        Element input = new LogicalInput();
        input.transform.setPosition(new Vector2D(-4, 0));

        Element notGate = new NotGate();
        Vertex freeVertex = new Vertex(new Vector2D(5, 5));

        scheme.add(input);
        scheme.add(notGate);
        scheme.add(freeVertex);

        scheme.add(new Wire(input.vertices.get(0), notGate.vertices.get(0)));
        scheme.add(new Wire(notGate.vertices.get(1), freeVertex));

        scheme.getSimulator().processVertexActivations();

        schemeView.repaint();
    }
}
