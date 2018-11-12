package cs.jirkamayer.gatefields.math;

public class Size2D {
    public float w;
    public float h;

    public Size2D(float width, float height) {
        this.w = width;
        this.h = height;
    }

    public Size2D times(float scalar) {
        return new Size2D(w * scalar, h * scalar);
    }
}
