package cs.jirkamayer.gatefields.editor.actions;

import cs.jirkamayer.gatefields.Camera;
import cs.jirkamayer.gatefields.editor.Action;
import cs.jirkamayer.gatefields.editor.Selection;
import cs.jirkamayer.gatefields.editor.events.Event;
import cs.jirkamayer.gatefields.editor.events.EventType;
import cs.jirkamayer.gatefields.editor.events.KeyState;
import cs.jirkamayer.gatefields.editor.events.MouseState;
import cs.jirkamayer.gatefields.math.Vector2D;

public class MoveAction extends Action {
    private Camera camera;
    private Selection selection;

    private Vector2D mouseOnDown = null;
    private Vector2D[] originalFreeVertexPositions = null;
    private Vector2D[] originalElementPositions = null;

    private boolean result = false;

    public MoveAction(Camera camera, Selection selection) {
        this.camera = camera;
        this.selection = selection;
    }

    public boolean getResult() {
        return result;
    }

    @Override
    public boolean filterActivatingEvent(Event e) {
        if (e.getEventType() != EventType.KEY_DOWN)
            return false;

        return e.keyState.keyPressed[KeyState.G];
    }

    @Override
    public void actionActivated(Event e) {
        super.actionActivated(e);

        // to avoid code duplication
        this.actionActivatedManually(e.mouseState.position);
    }

    public void actionActivatedManually(Vector2D mousePosition) {
        mouseOnDown = mousePosition;
        originalFreeVertexPositions = selection.getFreeVertexPositions();
        originalElementPositions = selection.getElementPositions();
    }

    @Override
    public void eventOccurred(Event e) {
        super.eventOccurred(e);

        if (e.getEventType() == EventType.MOUSE_MOVE) {
            Vector2D delta = e.mouseState.position.minus(mouseOnDown).divide(camera.scale);
            selection.setFreeVertexPositions(originalFreeVertexPositions, delta);
            selection.setElementPositions(originalElementPositions, delta);
            this.repaint();
        }

        if (e.getEventType() == EventType.MOUSE_DOWN && e.mouseState.causeButton == MouseState.LMB) {
            this.actionSubmitted();
        }
    }

    @Override
    public void actionSubmitted() {
        result = true;
        super.actionSubmitted();
    }

    @Override
    public void actionCancelled() {
        selection.setFreeVertexPositions(originalFreeVertexPositions, Vector2D.ZERO);
        selection.setElementPositions(originalElementPositions, Vector2D.ZERO);

        result = false;
        super.actionCancelled();
    }
}
