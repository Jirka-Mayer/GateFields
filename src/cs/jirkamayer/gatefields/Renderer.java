package cs.jirkamayer.gatefields;

import cs.jirkamayer.gatefields.math.Vector2D;

public interface Renderer {
    void clear();
    void drawVertex(Vector2D pos, boolean selected);
    void drawWire(Vector2D a, Vector2D b, boolean signal, boolean selected);
    void drawElementLine(Vector2D a, Vector2D b, boolean selected);
    void drawElementCircle(Vector2D center, boolean selected);
    void drawElementOrigin(Vector2D center, boolean selected);
}
