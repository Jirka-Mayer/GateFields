package cs.jirkamayer.gatefields.scheme;

import cs.jirkamayer.gatefields.Camera;
import cs.jirkamayer.gatefields.Renderer;
import cs.jirkamayer.gatefields.editor.Selection;
import cs.jirkamayer.gatefields.math.Vector2D;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SRLatch extends Element {
    public Vertex s, r, q, nq;
    private boolean state = false; // = Q

    public SRLatch() {
        vertices.add(s = new Vertex(new Vector2D(-1.5f, -1), this));
        vertices.add(r = new Vertex(new Vector2D(-1.5f, 1), this));
        vertices.add(q = new Vertex(new Vector2D(1.5f, -1), this));
        vertices.add(nq = new Vertex(new Vector2D(1.5f, 1), this));
    }

    @Override
    public void initializeSignals(Simulator sim) {
        sim.activateVertex(nq);
    }

    @Override
    public void updateSignals(Simulator sim) {
        // write state
        // if S and at the same time R, ignore R
        if (sim.hasSignal(s))
            state = true;
        else if (sim.hasSignal(r))
            state = false;

        if (state) {
            sim.activateVertex(q);
            sim.deactivateVertex(nq);
        } else {
            sim.deactivateVertex(q);
            sim.activateVertex(nq);
        }
    }

    @Override
    public void draw(Camera c, Selection s, Simulator sim) {
        Renderer r = c.getRenderer();
        c.setTransform(transform);
        boolean selected = s.isSelected(this);

        // wires
        r.drawWire(
            new Vector2D(-1.5f, -1), new Vector2D(-1, -1),
            sim.hasSignal(this.s), selected
        );
        r.drawWire(
            new Vector2D(-1.5f, 1), new Vector2D(-1, 1),
            sim.hasSignal(this.r), selected
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

        // labels
        r.drawLabel("S", new Vector2D(-0.6f, -1f));
        r.drawLabel("R", new Vector2D(-0.6f, 1f));
        r.drawLabel("Q", new Vector2D(0.6f, -1f));
        r.drawBarLabel("Q", new Vector2D(0.6f, 1f));

        // origin
        r.drawElementOrigin(Vector2D.ZERO, selected);
    }

    @Override
    protected void writeCustomPayload(DataOutputStream stream) throws IOException {
        stream.writeInt(state ? 1 : 0);
    }

    @Override
    protected void readCustomPayload(DataInputStream stream) throws IOException {
        state = stream.readInt() == 1;
    }
}
