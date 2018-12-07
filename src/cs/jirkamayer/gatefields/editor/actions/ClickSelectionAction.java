package cs.jirkamayer.gatefields.editor.actions;

import cs.jirkamayer.gatefields.Camera;
import cs.jirkamayer.gatefields.editor.Action;
import cs.jirkamayer.gatefields.editor.Selection;
import cs.jirkamayer.gatefields.editor.events.Event;
import cs.jirkamayer.gatefields.editor.events.EventType;
import cs.jirkamayer.gatefields.editor.events.KeyState;
import cs.jirkamayer.gatefields.editor.events.MouseState;
import cs.jirkamayer.gatefields.scheme.Scheme;
import cs.jirkamayer.gatefields.scheme.Vertex;

import java.util.List;

public class ClickSelectionAction extends Action {

    /**
     * Selection radius around the cursor in screen space (pixels)
     */
    public static final float SELECTION_DISTANCE_PX = 25f;

    private Camera camera;
    private Scheme scheme;
    private Selection selection;

    public ClickSelectionAction(Camera camera, Scheme scheme, Selection selection) {
        this.camera = camera;
        this.scheme = scheme;
        this.selection = selection;
    }

    @Override
    public boolean filterActivatingEvent(Event e) {
        if (e.getEventType() != EventType.MOUSE_DOWN)
            return false;

        return e.mouseState.causeButton == MouseState.RMB;
    }

    @Override
    public void actionActivated(Event e) {
        super.actionActivated(e);

        List<Vertex> vertices = scheme.getVertices();

        if (vertices.size() <= 0) {
            this.deactivate();
            return;
        }

        // get the closest vertex to mouse
        Vertex closest = vertices.get(0);
        float closestDistance = camera
            .worldToScreen(closest.transform.getPosition())
            .minus(e.mouseState.position).length();
        float distance;

        for (Vertex v : vertices) {
            distance = camera
                .worldToScreen(v.transform.getPosition())
                .minus(e.mouseState.position).length();
            if (distance <= closestDistance) {
                closest = v;
                closestDistance = distance;
            }
        }

        // invert vertex selection if it's close enough
        if (closestDistance <= SELECTION_DISTANCE_PX) {
            this.clickedOnVertex(closest, e.keyState.keyPressed[KeyState.SHIFT]);
        } else {
            this.clickedInSpace(e.keyState.keyPressed[KeyState.SHIFT]);
        }

        this.repaint();

        this.deactivate();
    }

    private void clickedOnVertex(Vertex vertex, boolean shift) {
        if (!shift)
            selection.deselectAll();

        if (selection.isSelected(vertex)) {
            selection.deselect(vertex);
        } else {
            selection.select(vertex);
        }
    }

    private void clickedInSpace(boolean shift) {
        if (!shift)
            selection.deselectAll();
    }

    @Override
    protected void repaint() {}
}
