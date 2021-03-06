package cs.jirkamayer.gatefields.scheme;

import cs.jirkamayer.gatefields.Camera;
import cs.jirkamayer.gatefields.Renderer;
import cs.jirkamayer.gatefields.editor.Selection;
import cs.jirkamayer.gatefields.math.Vector2D;

public class BufferGate extends Element {
    public Vertex inputVertex, outputVertex;

    public BufferGate() {
        vertices.add(inputVertex = new Vertex(new Vector2D(-0.5f, 0), this));
        vertices.add(outputVertex = new Vertex(new Vector2D(1.5f, 0), this));
    }

    @Override
    public void initializeSignals(Simulator sim) {
        // nothing
    }

    @Override
    public void updateSignals(Simulator sim) {
        if (sim.hasSignal(inputVertex))
            sim.activateVertex(outputVertex);
        else
            sim.deactivateVertex(outputVertex);
    }

    @Override
    public void draw(Camera c, Selection s, Simulator sim) {
        Renderer r = c.getRenderer();
        c.setTransform(transform);
        boolean selected = s.isSelected(this);

        // stem
        r.drawWire(
            new Vector2D(-0.5f, 0),
            new Vector2D(0, 0),
            sim.hasSignal(inputVertex),
            selected
        );

        // peak
        r.drawWire(
            new Vector2D(0.8f, 0),
            new Vector2D(1.5f, 0),
            sim.hasSignal(outputVertex),
            selected
        );

        // triangle
        r.drawElementLine(new Vector2D(0.0f, -0.6f), new Vector2D(0.0f, 0.6f), selected);
        r.drawElementLine(new Vector2D(0.0f, -0.6f), new Vector2D(0.8f, 0), selected);
        r.drawElementLine(new Vector2D(0.0f, 0.6f), new Vector2D(0.8f, 0), selected);

        // origin
        r.drawElementOrigin(Vector2D.ZERO, selected);
    }
}
