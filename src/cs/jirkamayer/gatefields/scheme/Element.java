package cs.jirkamayer.gatefields.scheme;

import cs.jirkamayer.gatefields.Camera;
import cs.jirkamayer.gatefields.editor.Selection;
import cs.jirkamayer.gatefields.math.Transform;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class Element {
    public Transform transform = new Transform();
    public List<Vertex> vertices = new ArrayList<>();

    /**
     * Initialize vertex activations
     */
    public void initializeSignals(Simulator sim) {
        // override this
    }

    /**
     * Recalculate output vertex activations. Immediately.
     */
    public void updateSignals(Simulator sim) {
        // override this
    }

    /**
     * Returns the element delay time
     * (delay between signal change and call to the updateSignals method)
     */
    public double getDelay() {
        return 0.001; // 1ms (not real, but at least manageable, real are ~10ns and less)
    }

    public abstract void draw(Camera c, Selection s, Simulator sim);

    ///////////////////
    // Serialization //
    ///////////////////

    // Note: local vertices are not serialized, their transform is known

    public static int getElementId(Element e) {
        if (e instanceof NotGate) return 1;
        if (e instanceof LogicalInput) return 2;
        if (e instanceof AndGate) return 3;
        if (e instanceof OrGate) return 4;
        if (e instanceof XorGate) return 5;
        if (e instanceof NandGate) return 6;
        if (e instanceof NorGate) return 7;
        if (e instanceof SRLatch) return 8;
        if (e instanceof DLatch) return 9;
        if (e instanceof BufferGate) return 10;
        if (e instanceof JKLatch) return 11;
        throw new IllegalArgumentException("Provided element is not known.");
    }

    public static Element createElementFromId(int id) {
        if (id == 1) return new NotGate();
        if (id == 2) return new LogicalInput();
        if (id == 3) return new AndGate();
        if (id == 4) return new OrGate();
        if (id == 5) return new XorGate();
        if (id == 6) return new NandGate();
        if (id == 7) return new NorGate();
        if (id == 8) return new SRLatch();
        if (id == 9) return new DLatch();
        if (id == 10) return new BufferGate();
        if (id == 11) return new JKLatch();
        throw new IllegalArgumentException("Provided id is not known.");
    }

    public void writeTo(DataOutputStream stream) throws IOException {
        stream.writeInt(Element.getElementId(this));
        transform.writeTo(stream);
        this.writeCustomPayload(stream);
    }

    public static Element readFrom(DataInputStream stream) throws IOException {
        int id = stream.readInt();
        Element element = Element.createElementFromId(id);
        element.transform.readFrom(stream);
        element.readCustomPayload(stream);
        return element;
    }

    protected void writeCustomPayload(DataOutputStream stream) throws IOException {
        // override this
    }

    protected void readCustomPayload(DataInputStream stream) throws IOException {
        // override this
    }
}
