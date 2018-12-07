package cs.jirkamayer.gatefields.editor.actions;

import cs.jirkamayer.gatefields.editor.Action;
import cs.jirkamayer.gatefields.editor.Selection;
import cs.jirkamayer.gatefields.editor.events.Event;
import cs.jirkamayer.gatefields.editor.events.EventType;
import cs.jirkamayer.gatefields.editor.events.KeyState;
import cs.jirkamayer.gatefields.scheme.Scheme;
import cs.jirkamayer.gatefields.scheme.Vertex;
import cs.jirkamayer.gatefields.scheme.Wire;

import java.util.List;

public class AddWireAction extends Action {
    private Selection selection;
    private Scheme scheme;

    public AddWireAction(Selection selection, Scheme scheme) {
        this.selection = selection;
        this.scheme = scheme;
    }

    @Override
    public boolean filterActivatingEvent(Event e) {
        if (e.getEventType() != EventType.KEY_DOWN)
            return false;

        return e.keyState.causeKey == KeyState.W;
    }

    @Override
    public void actionActivated(Event e) {
        super.actionActivated(e);

        List<Vertex> selectedVertices = selection.getVertices();

        if (selectedVertices.size() != 2) {
            this.deactivate();
            return;
        }

        Wire existingWire = scheme.getWire(selectedVertices.get(0), selectedVertices.get(1));

        if (existingWire == null) {
            Wire wire = new Wire(selectedVertices.get(0), selectedVertices.get(1));
            scheme.add(wire);
        } else {
            scheme.remove(existingWire);
        }

        this.repaint();
        this.deactivate();
    }

    @Override
    protected void repaint() {}
}
