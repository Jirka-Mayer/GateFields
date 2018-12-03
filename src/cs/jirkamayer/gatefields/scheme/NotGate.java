package cs.jirkamayer.gatefields.scheme;

import cs.jirkamayer.gatefields.Camera;
import cs.jirkamayer.gatefields.Renderer;
import cs.jirkamayer.gatefields.editor.Selection;
import cs.jirkamayer.gatefields.math.Size2D;
import cs.jirkamayer.gatefields.math.Vector2D;

import java.awt.*;

public class NotGate extends Element {
    public NotGate() {
        vertices.add(new Vertex(new Vector2D(-1, 0), this));
        vertices.add(new Vertex(new Vector2D(2, 0), this));
    }

    public void draw(Camera c, Selection s) {
        Renderer r = c.getRenderer();
        c.setTransform(transform);

        Color color = s.isSelected(this) ? Color.BLUE : Color.CYAN;
        boolean selected = s.isSelected(this);

        // stem
        r.drawWire(new Vector2D(-1, 0), new Vector2D(0, 0), false, selected);

        // triangle
        r.drawElementLine(new Vector2D(0, -0.8f), new Vector2D(0, 0.8f), selected);
        r.drawElementLine(new Vector2D(0, -0.8f), new Vector2D(1, 0), selected);
        r.drawElementLine(new Vector2D(0, 0.8f), new Vector2D(1, 0), selected);

        // peak
        r.drawWire(new Vector2D(1, 0), new Vector2D(2, 0), false, selected);

        // circle
        r.drawElementCircle(new Vector2D(1.1f, 0), selected);

        // origin
        r.drawElementOrigin(Vector2D.ZERO, selected);
    }
}
