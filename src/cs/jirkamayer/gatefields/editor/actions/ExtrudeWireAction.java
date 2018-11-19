package cs.jirkamayer.gatefields.editor.actions;

import cs.jirkamayer.gatefields.Camera;
import cs.jirkamayer.gatefields.editor.Action;
import cs.jirkamayer.gatefields.editor.Selection;
import cs.jirkamayer.gatefields.editor.events.Event;
import cs.jirkamayer.gatefields.editor.events.EventType;
import cs.jirkamayer.gatefields.editor.events.KeyState;
import cs.jirkamayer.gatefields.scheme.Scheme;
import cs.jirkamayer.gatefields.scheme.Vertex;
import cs.jirkamayer.gatefields.scheme.Wire;

import java.security.Key;
import java.util.List;

public class ExtrudeWireAction extends Action {
    private Camera camera;
    private Scheme scheme;
    private Selection selection;

    private Vertex vertex;
    private Wire wire;
    private MoveAction moveAction;

    public ExtrudeWireAction(Camera camera, Scheme scheme, Selection selection) {
        this.camera = camera;
        this.scheme = scheme;
        this.selection = selection;
    }

    @Override
    public boolean filterActivatingEvent(Event e) {
        if (e.getEventType() != EventType.KEY_DOWN)
            return false;

        return e.keyState.causeKey == KeyState.E;
    }

    @Override
    public void actionActivated(Event e) {
        super.actionActivated(e);

        List<Vertex> selectedVertices = selection.getVertices();
        if (selectedVertices.size() != 1) {
            this.deactivate();
            return;
        }
        Vertex selectedVertex = selectedVertices.get(0);

        vertex = new Vertex(selectedVertex.transform.getPosition());
        wire = new Wire(selectedVertex, vertex);
        scheme.add(vertex);
        scheme.add(wire);

        selection.deselectAll();
        selection.select(vertex);

        moveAction = new MoveAction(camera, selection);
        moveAction.setDeactivationCallback(this::moveActionDeactivated);
        moveAction.setRepaintCallback(this::repaint);
        moveAction.actionActivatedManually(e.mouseState.position);
    }

    @Override
    public void eventOccurred(Event e) {
        if (moveAction != null)
            moveAction.eventOccurred(e);
    }

    private void moveActionDeactivated(Action a) {
        if (moveAction.getResult()) {
            this.actionSubmitted();
        } else {
            this.actionCancelled();
        }
    }

    @Override
    public void actionCancelled() {
        selection.deselectAll();
        scheme.remove(wire);
        scheme.remove(vertex);

        super.actionCancelled();
    }
}
