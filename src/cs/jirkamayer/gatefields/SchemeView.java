package cs.jirkamayer.gatefields;

import cs.jirkamayer.gatefields.math.Size2D;
import cs.jirkamayer.gatefields.math.Vector2D;
import cs.jirkamayer.gatefields.scheme.NotGate;

import java.awt.*;
import java.awt.event.*;

public class SchemeView extends Canvas {
    private Camera camera;

    private Vector2D cameraOnDown = null;
    private Vector2D mouseDown = null;
    private boolean mousePressed = false;

    public SchemeView() {
        super();

        this.camera = new Camera();

        this.addMouseListener(new SchemeViewMouseListener());
        this.addMouseMotionListener(new SchemeViewMouseMotionListener());
        this.addMouseWheelListener(new SchemeViewMouseWheelListener());
    }

    public void paint(Graphics g) {
        camera.setGraphics(g);
        camera.setDisplayDimensions(this.getWidth(), this.getHeight());

        camera.clear(Color.BLACK);

        camera.fillRect(new Vector2D(1f, 1f), new Size2D(1.0f, 0.5f), Color.RED);

        g.setColor(Color.BLACK);
        g.fillOval(
            100,
            100,
            50,
            50
        );

        new NotGate().draw(camera);
    }

    private class SchemeViewMouseMotionListener implements MouseMotionListener {
        @Override
        public void mouseDragged(MouseEvent e) {
            if (mousePressed) {
                camera.position = cameraOnDown.plus(
                    mouseDown.minus(new Vector2D(e.getX(), e.getY())).divide(camera.scale)
                );
                SchemeView.this.repaint();
            } else {
                System.out.println("ELSE!");
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }
    }

    private class SchemeViewMouseListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            mouseDown = new Vector2D(e.getX(), e.getY());
            mousePressed = true;
            cameraOnDown = camera.position;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            mousePressed = false;
        }

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {}
    }

    private class SchemeViewMouseWheelListener implements MouseWheelListener {
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            if (e.getWheelRotation() > 0) {
                camera.scale *= 0.95f;
                SchemeView.this.repaint();
            } else if (e.getWheelRotation() < 0) {
                camera.scale /= 0.95f;
                SchemeView.this.repaint();
            }
        }
    }
}
