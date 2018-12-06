package cs.jirkamayer.gatefields.editor.actions;

import cs.jirkamayer.gatefields.Camera;
import cs.jirkamayer.gatefields.editor.Action;
import cs.jirkamayer.gatefields.editor.events.Event;
import cs.jirkamayer.gatefields.editor.events.EventType;
import cs.jirkamayer.gatefields.editor.events.KeyState;
import cs.jirkamayer.gatefields.editor.events.MouseState;
import cs.jirkamayer.gatefields.math.Vector2D;
import cs.jirkamayer.gatefields.scheme.Element;
import cs.jirkamayer.gatefields.scheme.Scheme;

public class AddElementAction extends Action {
    private Scheme scheme;
    private Element element;
    private Camera camera;

    public AddElementAction(Scheme scheme, Element element, Camera camera) {
        this.scheme = scheme;
        this.element = element;
        this.camera = camera;
    }

    @Override
    public void actionActivatedManually() {
        super.actionActivatedManually();

        scheme.add(element);
        this.repaint();
    }

    @Override
    public boolean filterActivatingEvent(Event e) {
        return false;
    }

    @Override
    public void eventOccurred(Event e) {
        super.eventOccurred(e);

        if (e.getEventType() == EventType.MOUSE_MOVE) {

            Vector2D pos = camera.screenToWorld(e.mouseState.position);

            // clamp delta to units halves
            if (!e.keyState.keyPressed[KeyState.SHIFT]) {
                pos = pos.times(2).round().divide(2);
            }

            element.transform.setPosition(pos);

            this.repaint();
        }

        if (e.getEventType() == EventType.MOUSE_DOWN && e.mouseState.causeButton == MouseState.LMB) {
            this.actionSubmitted();
        }
    }

    @Override
    public void actionCancelled() {
        scheme.remove(element);

        super.actionCancelled();
    }
}
