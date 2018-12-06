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
        return 0.2;
    }

    public abstract void draw(Camera c, Selection s, Simulator sim);

    ///////////////////
    // Serialization //
    ///////////////////

    // Note: local vertices are not serialized, their transform is known

    public static int getElementId(Element e) {
        if (e instanceof NotGate) return 1;
        if (e instanceof LogicalInput) return 2;
        throw new IllegalArgumentException("Provided element is not known.");
    }

    public static Element createElementFromId(int id) {
        if (id == 1) return new NotGate();
        if (id == 2) return new LogicalInput();
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
