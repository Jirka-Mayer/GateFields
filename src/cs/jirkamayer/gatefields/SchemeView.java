package cs.jirkamayer.gatefields;

import cs.jirkamayer.gatefields.editor.Selection;
import cs.jirkamayer.gatefields.editor.actions.*;
import cs.jirkamayer.gatefields.editor.events.EventDispatcher;
import cs.jirkamayer.gatefields.scheme.Scheme;

import java.awt.*;

public class SchemeView extends Canvas {
    private Camera camera;
    private Scheme scheme;
    private Selection selection;
    private EventDispatcher eventDispatcher;
    private ActionController actionController;

    public SchemeView(Scheme scheme, Selection selection) {
        super();

        this.camera = new Camera();
        this.camera.setRenderer(new DefaultRenderer(camera));
        this.scheme = scheme;
        this.selection = selection;
        this.eventDispatcher = new EventDispatcher();
        this.actionController = new ActionController(eventDispatcher, this::repaint);

        this.registerAllActions();

        // register event handling
        this.addMouseListener(eventDispatcher);
        this.addMouseMotionListener(eventDispatcher);
        this.addMouseWheelListener(eventDispatcher);
        this.addKeyListener(eventDispatcher);
    }

    public ActionController getActionController() {
        return actionController;
    }

    public Camera getCamera() {
        return camera;
    }

    private void registerAllActions()
    {
        actionController.registerAction(new MoveCameraAction(camera));
        actionController.registerAction(new ZoomCameraAction(camera));
        actionController.registerAction(new ClickSelectionAction(camera, scheme, selection));
        actionController.registerAction(new MoveAction(camera, selection));
        actionController.registerAction(new RotateAction(camera, selection));
        actionController.registerAction(new ExtrudeWireAction(camera, scheme, selection));
        actionController.registerAction(new AddWireAction(selection, scheme));
        actionController.registerAction(new ToggleAction(camera, scheme, selection));
    }

    public void paint(Graphics g) {
        camera.setGraphics(g);
        camera.setDisplayDimensions(this.getWidth(), this.getHeight());

        camera.getRenderer().clear();
        scheme.draw(camera, selection);
        actionController.drawActions(camera);
    }
}
