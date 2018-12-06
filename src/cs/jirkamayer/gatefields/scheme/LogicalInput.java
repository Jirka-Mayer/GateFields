package cs.jirkamayer.gatefields.scheme;

import cs.jirkamayer.gatefields.Camera;
import cs.jirkamayer.gatefields.Renderer;
import cs.jirkamayer.gatefields.editor.Selection;
import cs.jirkamayer.gatefields.math.Vector2D;

public class LogicalInput extends Element {
    private boolean state = false;

    public LogicalInput() {
        vertices.add(new Vertex(new Vector2D(0, 0), this));
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state, Simulator simulator) {
        this.state = state;

        if (state)
            simulator.activateVertex(vertices.get(0));
        else
            simulator.deactivateVertex(vertices.get(0));

        simulator.processVertexActivations();
    }

    @Override
    public void draw(Camera c, Selection s, Simulator sim) {
        Renderer r = c.getRenderer();
        c.setTransform(transform);

        boolean selected = s.isSelected(this);

        // stem
        r.drawWire(
            new Vector2D(-1, 0),
            new Vector2D(0, 0),
            sim.isActive(vertices.get(0)),
            selected
        );

        // perpendicular bar
        r.drawWire(new Vector2D(-1, 0.1f), new Vector2D(-1, -0.1f), state, selected);

        // origin
        r.drawElementOrigin(Vector2D.ZERO, selected);
    }
}
