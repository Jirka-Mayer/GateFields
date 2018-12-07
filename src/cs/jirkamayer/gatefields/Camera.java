package cs.jirkamayer.gatefields;

import cs.jirkamayer.gatefields.math.Size2D;
import cs.jirkamayer.gatefields.math.Transform;
import cs.jirkamayer.gatefields.math.Vector2D;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

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

    private Renderer renderer = null;

    public void setGraphics(Graphics g) {
        this.g = (Graphics2D)g;
    }

    public void setDisplayDimensions(int width, int height) {
        displayHeight = height;
        displayWidth = width;
    }

    public Size2D getDisplayDimensions() {
        return new Size2D(displayWidth, displayHeight);
    }

    public void setTransform(Transform t) {
        transform = t;
    }

    public Renderer getRenderer() {
        return renderer;
    }

    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;
    }

    public void reset() {
        position = Vector2D.ZERO;
        scale = 100.0f;
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
            .divide(scale)
            .plus(position);
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
        g.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(color);

        g.drawLine((int)a.x, (int)a.y, (int)b.x, (int)b.y);
    }

    public void drawCircle(Vector2D center, float radius, float width, Color color) {
        this.drawScreenCircle(
            this.localToScreen(center),
            this.localToScreen(radius),
            this.localToScreen(width),
            color
        );
    }

    public void drawScreenCircle(Vector2D center, float radius, float width, Color color) {
        Vector2D pos = center.minus(new Vector2D(radius, radius));
        int size = (int)(radius * 2);

        g.setColor(color);
        g.setStroke(new BasicStroke(width));
        g.drawOval((int)pos.x, (int)pos.y, size, size);
    }

    public void fillCircle(Vector2D center, float radius, Color color) {
        this.fillScreenCircle(
            this.localToScreen(center),
            this.localToScreen(radius),
            color
        );
    }

    public void fillScreenCircle(Vector2D center, float radius, Color color) {
        Vector2D pos = center.minus(new Vector2D(radius, radius));
        int size = (int)(radius * 2);

        g.setColor(color);
        g.fillOval((int)pos.x, (int)pos.y, size, size);
    }

    public void drawScreenText(String text, Vector2D pos, Color color, float height) {
        g.setColor(color);
        g.setFont(new Font("Monospaced", Font.PLAIN, (int)height));
        g.drawString(text, pos.x, pos.y);
    }

    public void drawScreenTextCentered(String text, Vector2D pos, Color color, float height, boolean bar) {
        g.setColor(color);
        g.setFont(new Font("Monospaced", Font.PLAIN, (int)height));
        FontMetrics metrics = g.getFontMetrics();
        g.drawString(
            text,
            pos.x - metrics.stringWidth(text) / 2.0f,
            pos.y - metrics.getHeight() / 2.0f + metrics.getAscent()
        );

        if (bar) {
            g.setStroke(new BasicStroke(height / 10f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.drawLine(
                (int)(pos.x - metrics.stringWidth(text) / 2.0f),
                (int)(pos.y + metrics.getHeight() / 2.0f - metrics.getAscent() - height * 0.3),
                (int)(pos.x + metrics.stringWidth(text) / 2.0f),
                (int)(pos.y + metrics.getHeight() / 2.0f - metrics.getAscent() - height * 0.3)
            );
        }
    }

    public void drawTextCentered(String text, Vector2D pos, Color color, float height, boolean bar) {
        this.drawScreenTextCentered(
            text,
            this.localToScreen(pos),
            color,
            this.localToScreen(height),
            bar
        );
    }

    ///////////////////
    // Serialization //
    ///////////////////

    public void writeTo(DataOutputStream stream) throws IOException {
        position.writeTo(stream);
        stream.writeFloat(scale);
    }

    public void readFrom(DataInputStream stream) throws IOException {
        position = Vector2D.readFrom(stream);
        scale = stream.readFloat();
    }
}
