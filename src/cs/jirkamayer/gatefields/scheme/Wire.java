package cs.jirkamayer.gatefields.scheme;

import com.sun.istack.internal.NotNull;
import cs.jirkamayer.gatefields.Camera;
import cs.jirkamayer.gatefields.editor.Selection;
import cs.jirkamayer.gatefields.math.Vector2D;

import java.awt.*;

public class Wire {
    public Vertex start, end;

    public Wire(@NotNull Vertex start, @NotNull Vertex end) {
        this.start = start;
        this.end = end;
    }

    public void draw(Camera c, Selection s, Simulator sim) {
        c.setTransform(null);
        c.getRenderer().drawWire(
            start.transform.getPosition(),
            end.transform.getPosition(),
            sim.hasSignal(this),
            s.isSelected(this)
        );
    }
}
