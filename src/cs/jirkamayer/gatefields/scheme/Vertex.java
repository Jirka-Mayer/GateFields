package cs.jirkamayer.gatefields.scheme;

import cs.jirkamayer.gatefields.Camera;
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

    public void draw(Camera c) {
        c.setTransform(transform);

        c.fillRect(
            new Vector2D(-0.1f, -0.1f),
            new Size2D(0.2f, 0.2f),
            Color.ORANGE
        );
    }
}
