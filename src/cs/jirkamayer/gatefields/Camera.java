package cs.jirkamayer.gatefields;

import cs.jirkamayer.gatefields.math.Size2D;
import cs.jirkamayer.gatefields.math.Transform;
import cs.jirkamayer.gatefields.math.Vector2D;

import java.awt.*;

/**
 * Adapter between the app drawing logic and the underlying framework
 * Handles camera motion and scaling
 */
public class Camera {
    private Graphics2D g;

    // position in the scene in scene units
    public Vector2D position = Vector2D.ZERO;

    // how many pixels is one scene unit
    public float scale = 100.0f;

    // size of the camera display
    private int displayWidth = 100;
    private int displayHeight = 100;

    /**
     * Transform in which we are rendering
     */
    private Transform transform = null;

    public void setGraphics(Graphics g) {
        this.g = (Graphics2D)g;
    }

    public void setDisplayDimensions(int width, int height) {
        displayHeight = height;
        displayWidth = width;
    }

    public void setTransform(Transform t) {
        transform = t;
    }

    /////////////////////
    // Transformations //
    /////////////////////

    public Vector2D worldToScreen(Vector2D v) {
        return v.minus(position)
            .times(scale)
            .plus(new Vector2D(displayWidth / 2f, displayHeight / 2f));
    }

    public Size2D worldToScreen(Size2D sz) {
        return sz.times(scale);
    }

    public float worldToScreen(float sz) {
        return sz * scale;
    }

    public Vector2D screenToWorld(Vector2D v) {
        return v.minus(new Vector2D(displayWidth / 2f, displayHeight / 2f))
            .plus(position)
            .divide(scale);
    }

    public Vector2D localToScreen(Vector2D v) {
        if (transform != null)
            v = transform.transformLocalToGlobal(v);

        return this.worldToScreen(v);
    }

    public Size2D localToScreen(Size2D sz) {
        // scale is not affected by transforms
        return this.worldToScreen(sz);
    }

    public float localToScreen(float sz) {
        // scale is not affected by transforms
        return this.worldToScreen(sz);
    }

    /////////////
    // Drawing //
    /////////////

    public void clear(Color color) {
        g.setColor(color);
        g.fillRect(0, 0, displayWidth, displayHeight);
    }

    public void fillRect(Vector2D pos, Size2D sz, Color color) {
        pos = this.localToScreen(pos);
        sz = this.localToScreen(sz);

        g.setColor(color);

        g.fillRect((int)pos.x, (int)pos.y, (int)sz.w, (int)sz.h);
    }

    public void drawLine(Vector2D a, Vector2D b, float width, Color color) {
        this.drawScreenLine(
            this.localToScreen(a),
            this.localToScreen(b),
            this.localToScreen(width),
            color
        );
    }

    public void drawScreenLine(Vector2D a, Vector2D b, float width, Color color) {
        g.setStroke(new BasicStroke(width));
        g.setColor(color);

        g.drawLine((int)a.x, (int)a.y, (int)b.x, (int)b.y);
    }
}
