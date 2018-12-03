package cs.jirkamayer.gatefields.math;

public class Vector2D {
    public static Vector2D ZERO = new Vector2D(0.0f, 0.0f);

    public float x;
    public float y;

    public Vector2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D plus(Vector2D v) {
        return new Vector2D(this.x + v.x, this.y + v.y);
    }

    public Vector2D minus(Vector2D v) {
        return new Vector2D(this.x - v.x, this.y - v.y);
    }

    public Vector2D times(float scalar) {
        return new Vector2D(this.x * scalar, this.y * scalar);
    }

    public Vector2D divide(float scalar) {
        return new Vector2D(this.x / scalar, this.y / scalar);
    }

    public Vector2D rotate(double angle) {
        float sina = (float)Math.sin(angle);
        float cosa = (float)Math.cos(angle);
        return new Vector2D(
            x * cosa - y * sina,
            x * sina + y * cosa
        );
    }

    public Vector2D round() {
        return new Vector2D(
            Math.round(x),
            Math.round(y)
        );
    }

    public float length() {
        return (float)Math.sqrt(x*x + y*y);
    }

    public boolean equals(Vector2D v) {
        return this.x == v.x && this.y == v.y;
    }

    public float angle() {
        return (float)Math.atan2(this.y, this.x);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vector2D) {
            return this.equals((Vector2D)obj);
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
