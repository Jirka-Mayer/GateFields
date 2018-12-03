package cs.jirkamayer.gatefields.editor.actions;

import cs.jirkamayer.gatefields.Camera;
import cs.jirkamayer.gatefields.editor.Action;
import cs.jirkamayer.gatefields.editor.Selection;
import cs.jirkamayer.gatefields.editor.events.Event;
import cs.jirkamayer.gatefields.editor.events.EventType;
import cs.jirkamayer.gatefields.editor.events.KeyState;
import cs.jirkamayer.gatefields.editor.events.MouseState;
import cs.jirkamayer.gatefields.math.Vector2D;
import cs.jirkamayer.gatefields.scheme.Element;
import cs.jirkamayer.gatefields.scheme.Vertex;

import java.util.List;

public class MoveAction extends Action {
    private Camera camera;
    private Selection selection;

    // mouse position when activated
    private Vector2D mouseOnDown = null;

    // vertices and elements we work with
    private List<Vertex> freeVertices = null;
    private List<Element> elements = null;

    // vertex and element positions when activated
    private Vector2D[] originalFreeVertexPositions = null;
    private Vector2D[] originalElementPositions = null;

    // result, when action launched manually
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

        if (selection.isEmpty()) {
            this.deactivate();
            return;
        }

        // to avoid code duplication
        this.actionActivatedManually(e.mouseState.position);
    }

    // not an override since this action can be activated non-manually as well
    public void actionActivatedManually(Vector2D mousePosition) {
        // store mouse position
        mouseOnDown = mousePosition;

        // store vertices and elements
        freeVertices = selection.getFreeVertices();
        elements = selection.getElements();

        // store vertex positions
        originalFreeVertexPositions = new Vector2D[freeVertices.size()];
        for (int i = 0; i < originalFreeVertexPositions.length; i++)
            originalFreeVertexPositions[i] = freeVertices.get(i).transform.getPosition();

        // store element positions
        originalElementPositions = new Vector2D[elements.size()];
        for (int i = 0; i < originalElementPositions.length; i++)
            originalElementPositions[i] = elements.get(i).transform.getPosition();
    }

    @Override
    public void eventOccurred(Event e) {
        super.eventOccurred(e);

        if (e.getEventType() == EventType.MOUSE_MOVE) {
            Vector2D delta = e.mouseState.position.minus(mouseOnDown).divide(camera.scale);
            this.moveItemsByDelta(delta);
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
        this.moveItemsByDelta(Vector2D.ZERO);

        result = false;
        super.actionCancelled();
    }

    private void moveItemsByDelta(Vector2D delta) {
        for (int i = 0; i < originalFreeVertexPositions.length; i++)
            freeVertices.get(i).transform.setPosition(originalFreeVertexPositions[i].plus(delta));

        for (int i = 0; i < originalElementPositions.length; i++)
            elements.get(i).transform.setPosition(originalElementPositions[i].plus(delta));
    }
}
