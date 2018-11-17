package cs.jirkamayer.gatefields.scheme;

import com.sun.istack.internal.NotNull;
import cs.jirkamayer.gatefields.Camera;

import java.awt.*;

public class Wire {
    public Vertex start, end;

    public Wire(@NotNull Vertex start, @NotNull Vertex end) {
        this.start = start;
        this.end = end;
    }

    public void draw(Camera c) {
        // TODO: draw more sophisticatedly
        c.setTransform(null);
        c.drawLine(start.transform.getPosition(), end.transform.getPosition(), 0.1f, Color.CYAN);
    }
}
