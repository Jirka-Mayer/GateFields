package cs.jirkamayer.gatefields;

import cs.jirkamayer.gatefields.editor.Selection;
import cs.jirkamayer.gatefields.math.Size2D;
import cs.jirkamayer.gatefields.math.Vector2D;
import cs.jirkamayer.gatefields.scheme.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

class MainWindow extends JFrame {

    private SchemeView sceneView;

    private Scheme scheme;
    private Selection selection;

    MainWindow() {
        super("Gate fields");

        scheme = new Scheme();
        selection = new Selection();

        // fake scheme
        Element notGate = new NotGate();
        Vertex freeVertex = new Vertex(new Vector2D(5, 5));
        Wire wire = new Wire(notGate.vertices.get(1), freeVertex);
        scheme.add(notGate);
        scheme.add(freeVertex);
        scheme.add(wire);

        // fake selection
        selection.select(freeVertex);

        sceneView = new SchemeView(scheme);
        sceneView.setSize(1200, 700);
        this.add(sceneView);

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.pack();

        /*
        Timer timer = new Timer((int)(1000.0 / 1.0), (ActionEvent e) -> {
            //this.update();
            ((SceneView) canvas).angle += 0.1;

            //Graphics g = canvas.getGraphics();
            //g.clearRect(0, 0, 500, 500);
            //canvas.paint(g);
            canvas.repaint();

            //System.out.println(((SceneView) canvas).angle);
        });
        timer.start();
        */
    }
}
