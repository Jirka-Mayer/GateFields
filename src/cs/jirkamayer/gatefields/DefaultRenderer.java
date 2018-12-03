package cs.jirkamayer.gatefields;

import cs.jirkamayer.gatefields.math.Vector2D;

import java.awt.*;

public class DefaultRenderer implements Renderer {
    public static final float LINE_WIDTH = 0.1f;
    public static final Color BACKGROUND_COLOR = Color.BLACK;

    private Camera camera;

    public DefaultRenderer(Camera camera) {
        this.camera = camera;
    }

    @Override
    public void clear() {
        camera.clear(BACKGROUND_COLOR);
    }

    @Override
    public void drawVertex(Vector2D pos, boolean selected) {
        camera.fillCircle(
            pos, 0.1f,
            selected ? Color.CYAN : Color.DARK_GRAY
        );
    }

    @Override
    public void drawWire(Vector2D a, Vector2D b, boolean signal, boolean selected) {
        camera.drawLine(
            a, b, LINE_WIDTH,
            selected ? Color.CYAN : (signal ? Color.GREEN : Color.DARK_GRAY)
        );
    }

    @Override
    public void drawElementLine(Vector2D a, Vector2D b, boolean selected) {
        camera.drawLine(
            a, b, LINE_WIDTH,
            selected ? Color.CYAN : Color.GRAY
        );
    }

    @Override
    public void drawElementCircle(Vector2D center, boolean selected) {
        camera.fillCircle(center, 0.1f, BACKGROUND_COLOR);
        camera.drawCircle(
            center, 0.1f, LINE_WIDTH,
            selected ? Color.CYAN : Color.GRAY
        );
    }

    @Override
    public void drawElementOrigin(Vector2D center, boolean selected) {
        if (selected)
            camera.fillCircle(center, 0.1f, Color.YELLOW);
    }
}
