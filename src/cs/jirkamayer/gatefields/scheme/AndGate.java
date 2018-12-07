package cs.jirkamayer.gatefields.scheme;

import cs.jirkamayer.gatefields.Camera;
import cs.jirkamayer.gatefields.Renderer;
import cs.jirkamayer.gatefields.editor.Selection;
import cs.jirkamayer.gatefields.math.Vector2D;

public class AndGate extends Element {
    public Vertex a, b, out;

    public AndGate() {
        vertices.add(a = new Vertex(new Vector2D(-1, -0.5f), this));
        vertices.add(b = new Vertex(new Vector2D(-1, 0.5f), this));
        vertices.add(out = new Vertex(new Vector2D(1.5f, 0), this));
    }

    @Override
    public void initializeSignals(Simulator sim) {
        // nothing
    }

    @Override
    public void updateSignals(Simulator sim) {
        if (sim.hasSignal(a) && sim.hasSignal(b))
            sim.activateVertex(out);
        else
            sim.deactivateVertex(out);
    }

    @Override
    public void draw(Camera c, Selection s, Simulator sim) {
        Renderer r = c.getRenderer();
        c.setTransform(transform);
        boolean selected = s.isSelected(this);

        // inputs
        r.drawWire(
            new Vector2D(-1, -0.5f),
            new Vector2D(-0.5f, -0.5f),
            sim.hasSignal(a),
            selected
        );
        r.drawWire(
            new Vector2D(-1, 0.5f),
            new Vector2D(-0.5f, 0.5f),
            sim.hasSignal(b),
            selected
        );

        // output
        r.drawWire(
            new Vector2D(1, 0),
            new Vector2D(1.5f, 0),
            sim.hasSignal(out),
            selected
        );

        // body
        r.drawElementLine(new Vector2D(-0.5f, -0.6f), new Vector2D(-0.5f, 0.6f), selected);

        r.drawElementLine(new Vector2D(-0.5f, 0.6f), new Vector2D(0.5f, 0.6f), selected);
        r.drawElementLine(new Vector2D(0.5f, 0.6f), new Vector2D(0.85f, 0.4f), selected);
        r.drawElementLine(new Vector2D(0.85f, 0.4f), new Vector2D(1, 0), selected);

        r.drawElementLine(new Vector2D(-0.5f, -0.6f), new Vector2D(0.5f, -0.6f), selected);
        r.drawElementLine(new Vector2D(0.5f, -0.6f), new Vector2D(0.85f, -0.4f), selected);
        r.drawElementLine(new Vector2D(0.85f, -0.4f), new Vector2D(1, -0), selected);
    }
}
