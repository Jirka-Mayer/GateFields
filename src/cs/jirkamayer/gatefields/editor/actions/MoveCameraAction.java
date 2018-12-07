package cs.jirkamayer.gatefields.editor.actions;

import cs.jirkamayer.gatefields.Camera;
import cs.jirkamayer.gatefields.editor.Action;
import cs.jirkamayer.gatefields.editor.events.Event;
import cs.jirkamayer.gatefields.editor.events.EventType;
import cs.jirkamayer.gatefields.editor.events.MouseState;
import cs.jirkamayer.gatefields.math.Vector2D;

public class MoveCameraAction extends Action {
    private Camera camera;

    private Vector2D cameraOnDown = null;
    private Vector2D mouseDown = null;

    public MoveCameraAction(Camera camera) {
        this.camera = camera;
    }

    @Override
    public boolean filterActivatingEvent(Event e) {
        if (e.getEventType() != EventType.MOUSE_DOWN)
            return false;

        return e.mouseState.causeButton == MouseState.LMB;
    }

    @Override
    public void actionActivated(Event e) {
        super.actionActivated(e);

        mouseDown = e.mouseState.position;
        cameraOnDown = camera.position;
    }

    @Override
    public void eventOccurred(Event e) {
        super.eventOccurred(e);

        if (e.getEventType() == EventType.MOUSE_MOVE) {
            camera.position = cameraOnDown.plus(
                mouseDown.minus(e.mouseState.position).divide(camera.scale)
            );
            this.repaint();
        }

        if (e.getEventType() == EventType.MOUSE_UP && e.mouseState.causeButton == MouseState.LMB) {
            this.deactivate();
        }
    }

    @Override
    public String getName() {
        return "Move camera";
    }
}
