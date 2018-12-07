package cs.jirkamayer.gatefields.scheme;

import cs.jirkamayer.gatefields.Camera;
import cs.jirkamayer.gatefields.Renderer;
import cs.jirkamayer.gatefields.editor.Selection;
import cs.jirkamayer.gatefields.math.Vector2D;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class DLatch extends Element {
    public Vertex d, clk, q, nq;
    private boolean state = false; // = Q

    // to detect rising edges
    private boolean prevClkSignal = false;

    public DLatch() {
        vertices.add(d = new Vertex(new Vector2D(-1.5f, -1), this));
        vertices.add(clk = new Vertex(new Vector2D(-1.5f, 1), this));
        vertices.add(q = new Vertex(new Vector2D(1.5f, -1), this));
        vertices.add(nq = new Vertex(new Vector2D(1.5f, 1), this));
    }

    @Override
    public void initializeSignals(Simulator sim) {
        sim.activateVertex(nq);
    }

    @Override
    public void updateSignals(Simulator sim) {
        /*
            NOTE:
            I sadly cannot implement shift register with only D latches
            I have to delay the signal using buffers. Then it works.
         */

        // catch CLK rising edge, but not if the data just had some edge as well
        if (sim.hasSignal(clk) && !prevClkSignal) // rising edge
            state = sim.hasSignal(d);

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
            sim.hasSignal(this.d), selected
        );
        r.drawWire(
            new Vector2D(-1.5f, 1), new Vector2D(-1, 1),
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
        r.drawElementLine(new Vector2D(-1, 0.7f), new Vector2D(-0.7f, 1), selected);
        r.drawElementLine(new Vector2D(-1, 1.3f), new Vector2D(-0.7f, 1), selected);

        // labels
        r.drawLabel("D", new Vector2D(-0.6f, -1f));
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
