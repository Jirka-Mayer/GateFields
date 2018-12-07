package cs.jirkamayer.gatefields.scheme;

import cs.jirkamayer.gatefields.Camera;
import cs.jirkamayer.gatefields.Renderer;
import cs.jirkamayer.gatefields.editor.Selection;
import cs.jirkamayer.gatefields.math.Vector2D;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class JKLatch extends Element {
    public Vertex j, k, clk, q, nq;
    private boolean state = false; // = Q

    // to detect rising edges
    private boolean prevClkSignal = false;

    public JKLatch() {
        vertices.add(j = new Vertex(new Vector2D(-1.5f, -1), this));
        vertices.add(k = new Vertex(new Vector2D(-1.5f, 1), this));
        vertices.add(clk = new Vertex(new Vector2D(-1.5f, 0), this));
        vertices.add(q = new Vertex(new Vector2D(1.5f, -1), this));
        vertices.add(nq = new Vertex(new Vector2D(1.5f, 1), this));
    }

    @Override
    public void initializeSignals(Simulator sim) {
        sim.activateVertex(nq);
    }

    @Override
    public void updateSignals(Simulator sim) {
        // catch CLK rising edge
        if (sim.hasSignal(clk) && !prevClkSignal) {
            if (sim.hasSignal(j) && !sim.hasSignal(k)) // set
                state = true;

            if (!sim.hasSignal(j) && sim.hasSignal(k)) // reset
                state = false;

            if (sim.hasSignal(j) && sim.hasSignal(k)) // toggle
                state = !state;
        }

        if (state) {
            sim.activateVertex(q);
            sim.deactivateVertex(nq);
        } else {
            sim.deactivateVertex(q);
            sim.activateVertex(nq);
        }

        prevClkSignal = sim.hasSignal(clk);
    }

    @Override
    public void draw(Camera c, Selection s, Simulator sim) {
        Renderer r = c.getRenderer();
        c.setTransform(transform);
        boolean selected = s.isSelected(this);

        // wires
        r.drawWire(
            new Vector2D(-1.5f, -1), new Vector2D(-1, -1),
            sim.hasSignal(this.j), selected
        );
        r.drawWire(
            new Vector2D(-1.5f, 1), new Vector2D(-1, 1),
            sim.hasSignal(this.k), selected
        );
        r.drawWire(
            new Vector2D(-1.5f, 0), new Vector2D(-1, 0),
            sim.hasSignal(this.clk), selected
        );
        r.drawWire(
            new Vector2D(1.5f, -1), new Vector2D(1, -1),
            sim.hasSignal(this.q), selected
        );
        r.drawWire(
            new Vector2D(1.5f, 1), new Vector2D(1, 1),
            sim.hasSignal(this.nq), selected
        );

        // body
        r.drawElementLine(new Vector2D(-1, -1.5f), new Vector2D(1, -1.5f), selected);
        r.drawElementLine(new Vector2D(-1, 1.5f), new Vector2D(1, 1.5f), selected);
        r.drawElementLine(new Vector2D(-1, -1.5f), new Vector2D(-1, 1.5f), selected);
        r.drawElementLine(new Vector2D(1, -1.5f), new Vector2D(1, 1.5f), selected);

        // clk symbol
        r.drawElementLine(new Vector2D(-1, -0.3f), new Vector2D(-0.7f, 0), selected);
        r.drawElementLine(new Vector2D(-1, 0.3f), new Vector2D(-0.7f, 0), selected);

        // labels
        r.drawLabel("J", new Vector2D(-0.6f, -1f));
        r.drawLabel("K", new Vector2D(-0.6f, 1f));
        r.drawLabel("Q", new Vector2D(0.6f, -1f));
        r.drawBarLabel("Q", new Vector2D(0.6f, 1f));

        // origin
        r.drawElementOrigin(Vector2D.ZERO, selected);
    }

    @Override
    protected void writeCustomPayload(DataOutputStream stream) throws IOException {
        stream.writeInt(state ? 1 : 0);
        stream.writeInt(prevClkSignal ? 1 : 0);
    }

    @Override
    protected void readCustomPayload(DataInputStream stream) throws IOException {
        state = stream.readInt() == 1;
        prevClkSignal = stream.readInt() == 1;
    }
}
