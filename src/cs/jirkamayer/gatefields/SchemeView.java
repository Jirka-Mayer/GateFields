package cs.jirkamayer.gatefields;

import cs.jirkamayer.gatefields.editor.events.EventDispatcher;
import cs.jirkamayer.gatefields.math.Size2D;
import cs.jirkamayer.gatefields.math.Vector2D;
import cs.jirkamayer.gatefields.scheme.NotGate;
import cs.jirkamayer.gatefields.scheme.Scheme;

import java.awt.*;
import java.awt.event.*;

public class SchemeView extends Canvas {
    private Camera camera;
    private Scheme scheme;
    private EventDispatcher eventDispatcher;

    private Vector2D cameraOnDown = null;
    private Vector2D mouseDown = null;
    private boolean mousePressed = false;

    public SchemeView(Scheme scheme) {
        super();

        this.camera = new Camera();
        this.scheme = scheme;
        this.eventDispatcher = new EventDispatcher();

        /*eventDispatcher.addEventListener((e) -> {
            for (boolean b : e.mouseState.buttonPressed)
                System.out.print(b + ", ");
            System.out.println();
        });*/

        this.addMouseListener(new SchemeViewMouseListener());
        this.addMouseMotionListener(new SchemeViewMouseMotionListener());
        this.addMouseWheelListener(new SchemeViewMouseWheelListener());

        this.addMouseListener(eventDispatcher);
        /*this.addMouseMotionListener(eventDispatcher);
        this.addMouseWheelListener(eventDispatcher);*/
    }

    public void paint(Graphics g) {
        camera.setGraphics(g);
        camera.setDisplayDimensions(this.getWidth(), this.getHeight());

        camera.clear(Color.BLACK);

        scheme.draw(camera);

        /*
        g.setColor(Color.BLACK);
        g.fillOval(
            100,
            100,
            50,
            50
        );
        */
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
                camera.scale *= 0.9f;
                SchemeView.this.repaint();
            } else if (e.getWheelRotation() < 0) {
                camera.scale /= 0.9f;
                SchemeView.this.repaint();
            }
        }
    }
}
