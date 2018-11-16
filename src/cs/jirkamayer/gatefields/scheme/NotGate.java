package cs.jirkamayer.gatefields.scheme;

import cs.jirkamayer.gatefields.Camera;
import cs.jirkamayer.gatefields.math.Size2D;
import cs.jirkamayer.gatefields.math.Vector2D;

import java.awt.*;

public class NotGate extends Element {
    public void draw(Camera c) {
        c.fillRect(
            this.position.minus(new Vector2D(0.1f, 0.1f)),
            new Size2D(0.2f, 0.2f),
            Color.ORANGE
        );

        // stem
        c.drawLine(new Vector2D(-1, 0), new Vector2D(0, 0), 0.1f, Color.CYAN);

        // triangle
        c.drawLine(new Vector2D(0, -0.8f), new Vector2D(0, 0.8f), 0.1f, Color.CYAN);
        c.drawLine(new Vector2D(0, -0.8f), new Vector2D(1, 0), 0.1f, Color.CYAN);
        c.drawLine(new Vector2D(0, 0.8f), new Vector2D(1, 0), 0.1f, Color.CYAN);

        // peak
        c.drawLine(new Vector2D(1, 0), new Vector2D(2, 0), 0.1f, Color.CYAN);
    }
}
