package cs.jirkamayer.gatefields.math;

public class Vector2D {
    public static Vector2D ZERO = new Vector2D(0.0f, 0.0f);

    public float x;
    public float y;

    public Vector2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D minus(Vector2D v) {
        return new Vector2D(this.x - v.x, this.y - v.y);
    }

    public Vector2D times(float scalar) {
        return new Vector2D(this.x * scalar, this.y * scalar);
    }
}
