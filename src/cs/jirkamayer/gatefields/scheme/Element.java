package cs.jirkamayer.gatefields.scheme;

import cs.jirkamayer.gatefields.Camera;
import cs.jirkamayer.gatefields.editor.Selection;
import cs.jirkamayer.gatefields.math.Transform;

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
}
