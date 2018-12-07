package cs.jirkamayer.gatefields.editor.actions;

import cs.jirkamayer.gatefields.editor.Action;
import cs.jirkamayer.gatefields.editor.Selection;
import cs.jirkamayer.gatefields.editor.events.Event;
import cs.jirkamayer.gatefields.editor.events.EventType;
import cs.jirkamayer.gatefields.editor.events.KeyState;
import cs.jirkamayer.gatefields.scheme.Scheme;
import cs.jirkamayer.gatefields.scheme.Vertex;

import java.util.List;

public class DeleteAction extends Action {
    private Scheme scheme;
    private Selection selection;

    public DeleteAction(Scheme scheme, Selection selection) {
        this.scheme = scheme;
        this.selection = selection;
    }

    @Override
    public boolean filterActivatingEvent(Event e) {
        if (e.getEventType() != EventType.KEY_DOWN)
            return false;

        return e.keyState.causeKey == KeyState.X;
    }

    @Override
    public void actionActivated(Event e) {
        List<Vertex> vertices = selection.getVertices();

        if (vertices.size() == 0) {
            this.deactivate();
            return;
        }

        for (Vertex v : vertices) {
            if (v.isBound())
                scheme.remove(v.getBoundElement());
            else
                scheme.remove(v);
        }

        this.repaint();
        this.deactivate();
    }

    @Override
    protected void repaint() {}
}
