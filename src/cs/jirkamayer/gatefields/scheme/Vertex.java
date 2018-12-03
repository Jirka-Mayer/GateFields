package cs.jirkamayer.gatefields.scheme;

import cs.jirkamayer.gatefields.Camera;
import cs.jirkamayer.gatefields.Renderer;
import cs.jirkamayer.gatefields.editor.Selection;
import cs.jirkamayer.gatefields.math.Size2D;
import cs.jirkamayer.gatefields.math.Transform;
import cs.jirkamayer.gatefields.math.Vector2D;

import java.awt.*;

public class Vertex {
    public Transform transform;
    private Element boundElement;

    public Vertex(Vector2D position, Element boundElement) {
        transform = new Transform();
        transform.setLocalPosition(position);

        this.boundElement = boundElement;
        if (boundElement != null)
            transform.setParent(boundElement.transform);
    }

    public Vertex(Vector2D position) {
        this(position, null);
    }

    public boolean isBound() {
        return boundElement != null;
    }

    public Element getBoundElement() {
        return this.boundElement;
    }

    public void draw(Camera c, Selection s) {
        c.setTransform(transform);
        c.getRenderer().drawVertex(Vector2D.ZERO, s.isSelected(this));
    }
}
