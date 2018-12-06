package cs.jirkamayer.gatefields;

import cs.jirkamayer.gatefields.editor.Selection;
import cs.jirkamayer.gatefields.math.Vector2D;
import cs.jirkamayer.gatefields.scheme.*;

import javax.swing.*;
import java.awt.event.ActionEvent;

class MainWindow extends JFrame {

    private SchemeView schemeView;

    private Scheme scheme;
    private Selection selection;

    private Timer timer;

    MainWindow() {
        super("Gate fields");

        scheme = new Scheme();
        selection = new Selection();

        this.setupUI();

        this.openDefaultScheme();

        timer = new Timer((int)(1000.0 / 10.0), (ActionEvent e) -> {
            scheme.getSimulator().simulationTick(0.1);
            schemeView.draw();

            timer.start();
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void setupUI() {
        schemeView = new SchemeView(scheme, selection);
        schemeView.setSize(1200, 700);
        this.add(schemeView);

        this.setJMenuBar(
            new MainMenu(
                scheme,
                schemeView.getActionController(),
                schemeView.getCamera()
            )
        );

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
