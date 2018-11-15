package cs.jirkamayer.gatefields;

import cs.jirkamayer.gatefields.math.Size2D;
import cs.jirkamayer.gatefields.math.Vector2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

class MainWindow extends JFrame {

    private Camera camera;
    private SceneView sceneView;

    MainWindow() {
        super("Gate fields");

        sceneView = new SceneView();
        sceneView.setSize(400, 400);
        this.add(sceneView);

        camera = new Camera();

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

    private class SceneView extends Canvas {
        public SceneView() {
            super();

            this.addMouseListener(new SceneViewMouseListener());
            this.addMouseMotionListener(new SceneViewMouseMotionListener());
        }

        public void paint(Graphics g) {
            camera.setGraphics(g);
            camera.setDisplayDimensions(this.getWidth(), this.getHeight());

            camera.fillRect(new Vector2D(1f, 1f), new Size2D(1.0f, 0.5f), Color.RED);

            g.setColor(Color.BLACK);
            g.fillOval(
                100,
                100,
                50,
                50
            );
        }

        private class SceneViewMouseMotionListener implements MouseMotionListener {
            @Override
            public void mouseDragged(MouseEvent e) {

            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        }

        private class SceneViewMouseListener implements MouseListener {
            @Override
            public void mouseClicked(MouseEvent e) {}

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
        }
    }
}
