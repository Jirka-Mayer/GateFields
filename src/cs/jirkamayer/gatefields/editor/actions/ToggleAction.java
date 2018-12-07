package cs.jirkamayer.gatefields.editor.actions;

import cs.jirkamayer.gatefields.Camera;
import cs.jirkamayer.gatefields.editor.Action;
import cs.jirkamayer.gatefields.editor.Selection;
import cs.jirkamayer.gatefields.editor.events.Event;
import cs.jirkamayer.gatefields.editor.events.EventType;
import cs.jirkamayer.gatefields.editor.events.KeyState;
import cs.jirkamayer.gatefields.scheme.Element;
import cs.jirkamayer.gatefields.scheme.LogicalInput;
import cs.jirkamayer.gatefields.scheme.Scheme;
import cs.jirkamayer.gatefields.scheme.Simulator;

import java.util.List;

public class ToggleAction extends Action {
    public static final float TOGGLE_DISTANCE_PX = 100f;

    private Camera camera;
    private Scheme scheme;
    private Selection selection;

    public ToggleAction(Camera camera, Scheme scheme, Selection selection) {
        this.camera = camera;
        this.scheme = scheme;
        this.selection = selection;
    }

    @Override
    public boolean filterActivatingEvent(Event e) {
        if (e.getEventType() != EventType.KEY_DOWN)
            return false;

        return e.keyState.causeKey == KeyState.T;
    }

    @Override
    public void actionActivated(Event e) {
        super.actionActivated(e);

        List<Element> elements = scheme.getElements();

        if (elements.size() <= 0) {
            this.deactivate();
            return;
        }

        // get the closest input element to mouse
        Element closest = elements.get(0);
        float closestDistance = camera
            .worldToScreen(closest.transform.getPosition())
            .minus(e.mouseState.position).length();
        float distance;

        for (Element elem : elements) {
            distance = camera
                .worldToScreen(elem.transform.getPosition())
                .minus(e.mouseState.position).length();
            if (distance <= closestDistance) {
                closest = elem;
                closestDistance = distance;
            }
        }

        if (!(closest instanceof LogicalInput)) {
            this.deactivate();
            return;
        }

        // invert vertex selection if it's close enough
        if (closestDistance > TOGGLE_DISTANCE_PX) {
            this.deactivate();
            return;
        }

        // toggle
        LogicalInput input = (LogicalInput) closest;
        input.setState(!input.getState(), scheme.getSimulator());

        this.deactivate();
        this.repaint();
    }

    @Override
    protected void repaint() {}
}
