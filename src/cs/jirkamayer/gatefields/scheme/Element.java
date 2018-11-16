package cs.jirkamayer.gatefields.scheme;

import cs.jirkamayer.gatefields.Camera;
import cs.jirkamayer.gatefields.math.Vector2D;

public abstract class Element {
    public Vector2D position = Vector2D.ZERO;

    public abstract void draw(Camera c);
}
