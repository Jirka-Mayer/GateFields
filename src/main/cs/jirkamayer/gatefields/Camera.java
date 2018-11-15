package cs.jirkamayer.gatefields;

import cs.jirkamayer.gatefields.math.Size2D;
import cs.jirkamayer.gatefields.math.Vector2D;

import java.awt.*;

/**
 * Adapter between the app drawing logic and the underlying framework
 * Handles camera motion and scaling
 */
public class Camera {
    private Graphics g;

    // position in the scene in scene units
    private Vector2D position = Vector2D.ZERO;

    // how many pixels is one scene unit
    private float scale = 100.0f;

    // size of the camera display
    private int displayWidth = 100;
    private int displayHeight = 100;

    public void setGraphics(Graphics g) {
        this.g = g;
    }

    public void setDisplayDimensions(int width, int height) {
        displayHeight = height;
        displayWidth = width;
    }

    public Vector2D worldToScreen(Vector2D v) {
        return v.minus(position).times(scale);
    }

    public Size2D worldToScreen(Size2D sz) {
        return sz.times(scale);
    }

    public void clear(Color color) {
        g.setColor(color);
        g.fillRect(0, 0, displayWidth, displayHeight);
    }

    public void fillRect(Vector2D pos, Size2D sz, Color color) {
        pos = this.worldToScreen(pos);
        sz = this.worldToScreen(sz);
        g.setColor(color);
        g.fillRect((int)pos.x, (int)pos.y, (int)sz.w, (int)sz.h);
    }
}
