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

import java.awt.*;
import java.util.List;

public class RotateAction extends Action {
    private Camera camera;
    private Selection selection;

    // mouse position when activated
    private Vector2D mouseOnDown = null;

    // last mouse position (for drawing purpouses)
    private Vector2D mousePosition = null;

    // rotation pivot in world space
    private Vector2D pivot = null;

    // vertices and elements we work with
    private List<Vertex> freeVertices = null;
    private List<Element> elements = null;

    // vertex and element positions when activated
    private Vector2D[] originalFreeVertexPositions = null;
    private Vector2D[] originalElementPositions = null;
    private float[] originalElementRotations = null;

    public RotateAction(Camera camera, Selection selection) {
        this.camera = camera;
        this.selection = selection;
    }

    @Override
    public boolean filterActivatingEvent(Event e) {
        if (e.getEventType() != EventType.KEY_DOWN)
            return false;

        return e.keyState.keyPressed[KeyState.R];
    }

    @Override
    public void actionActivated(Event e) {
        super.actionActivated(e);

        if (selection.isEmpty()) {
            this.deactivate();
            return;
        }

        // store mouse position
        mouseOnDown = e.mouseState.position;

        // store vertices and elements
        freeVertices = selection.getFreeVertices();
        elements = selection.getElements();

        // store vertex positions
        originalFreeVertexPositions = new Vector2D[freeVertices.size()];
        for (int i = 0; i < originalFreeVertexPositions.length; i++)
            originalFreeVertexPositions[i] = freeVertices.get(i).transform.getPosition();

        // store element positions and rotations
        originalElementPositions = new Vector2D[elements.size()];
        originalElementRotations = new float[elements.size()];
        for (int i = 0; i < originalElementPositions.length; i++) {
            originalElementPositions[i] = elements.get(i).transform.getPosition();
            originalElementRotations[i] = elements.get(i).transform.getRotation();
        }

        this.calculatePivot();
    }

    @Override
    public void eventOccurred(Event e) {
        super.eventOccurred(e);

        if (e.getEventType() == EventType.MOUSE_MOVE) {
            mousePosition = e.mouseState.position;

            Vector2D screenPivot = camera.worldToScreen(pivot);
            float currentAngle = mousePosition.minus(screenPivot).angle();
            float pivotalAngle = mouseOnDown.minus(screenPivot).angle();
            float delta = currentAngle - pivotalAngle;

            // clamp delta to 45deg rotation
            if (e.keyState.keyPressed[KeyState.SHIFT]) {
                delta = (float)(Math.round(delta / (Math.PI/4)) * (Math.PI/4));
            }

            this.rotateItemsByDelta(delta);
            this.repaint();
        }

        if (e.getEventType() == EventType.MOUSE_DOWN && e.mouseState.causeButton == MouseState.LMB) {
            this.actionSubmitted();
        }
    }

    @Override
    public void actionCancelled() {
        this.rotateItemsByDelta(0);

        super.actionCancelled();
    }

    @Override
    public void drawAction(Camera camera) {
        if (mouseOnDown == null || mousePosition == null)
            return;

        camera.drawScreenLine(
            camera.worldToScreen(pivot), mousePosition, 1, Color.GRAY
        );
    }

    private void calculatePivot() {
        pivot = Vector2D.ZERO;

        for (int i = 0; i < freeVertices.size(); i++)
            pivot = pivot.plus(freeVertices.get(i).transform.getPosition());

        for (int i = 0; i < elements.size(); i++)
            pivot = pivot.plus(elements.get(i).transform.getPosition());

        pivot = pivot.divide(freeVertices.size() + elements.size());
    }

    private void rotateItemsByDelta(float delta) {
        for (int i = 0; i < freeVertices.size(); i++)
            freeVertices.get(i).transform.setPosition(
                originalFreeVertexPositions[i].minus(pivot).rotate(delta).plus(pivot)
            );

        for (int i = 0; i < elements.size(); i++) {
            elements.get(i).transform.setPosition(
                originalElementPositions[i].minus(pivot).rotate(delta).plus(pivot)
            );

            // HACK: local rotation because we know that
            // element transform has no parents and I don't
            // want to implement is now
            elements.get(i).transform.setLocalRotation(
                originalElementRotations[i] + delta
            );
        }
    }
}
