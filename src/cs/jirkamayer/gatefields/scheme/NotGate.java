package cs.jirkamayer.gatefields.scheme;

import cs.jirkamayer.gatefields.Camera;
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
        c.setTransform(transform);

        Color color = s.isSelected(this) ? Color.BLUE : Color.CYAN;

        // origin
        c.fillRect(
            new Vector2D(-0.1f, -0.1f),
            new Size2D(0.2f, 0.2f),
            Color.ORANGE
        );

        // stem
        c.drawLine(new Vector2D(-1, 0), new Vector2D(0, 0), 0.1f, color);

        // triangle
        c.drawLine(new Vector2D(0, -0.8f), new Vector2D(0, 0.8f), 0.1f, color);
        c.drawLine(new Vector2D(0, -0.8f), new Vector2D(1, 0), 0.1f, color);
        c.drawLine(new Vector2D(0, 0.8f), new Vector2D(1, 0), 0.1f, color);

        // peak
        c.drawLine(new Vector2D(1, 0), new Vector2D(2, 0), 0.1f, color);
    }
}
