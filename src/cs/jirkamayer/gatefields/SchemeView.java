package cs.jirkamayer.gatefields;

import cs.jirkamayer.gatefields.editor.Selection;
import cs.jirkamayer.gatefields.editor.actions.ActionController;
import cs.jirkamayer.gatefields.editor.actions.ClickSelectionAction;
import cs.jirkamayer.gatefields.editor.actions.MoveCameraAction;
import cs.jirkamayer.gatefields.editor.events.EventDispatcher;
import cs.jirkamayer.gatefields.math.Vector2D;
import cs.jirkamayer.gatefields.scheme.Scheme;

import java.awt.*;
import java.awt.event.*;

public class SchemeView extends Canvas {
    private Camera camera;
    private Scheme scheme;
    private Selection selection;
    private EventDispatcher eventDispatcher;
    private ActionController actionController;

    public SchemeView(Scheme scheme, Selection selection) {
        super();

        this.camera = new Camera();
        this.scheme = scheme;
        this.selection = selection;
        this.eventDispatcher = new EventDispatcher();
        this.actionController = new ActionController(eventDispatcher, this::repaint);

        this.registerAllActions();

        // TODO: remove this
        this.addMouseWheelListener(new SchemeViewMouseWheelListener());

        // register event handling
        this.addMouseListener(eventDispatcher);
        this.addMouseMotionListener(eventDispatcher);
        this.addMouseWheelListener(eventDispatcher);
        this.addKeyListener(eventDispatcher);
    }

    private void registerAllActions()
    {
        actionController.registerAction(new MoveCameraAction(camera));
        actionController.registerAction(new ClickSelectionAction(camera, scheme, selection));
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
