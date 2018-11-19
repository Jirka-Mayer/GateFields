package cs.jirkamayer.gatefields.editor.actions;

import cs.jirkamayer.gatefields.Camera;
import cs.jirkamayer.gatefields.editor.Selection;
import cs.jirkamayer.gatefields.editor.events.Event;
import cs.jirkamayer.gatefields.editor.events.EventType;
import cs.jirkamayer.gatefields.editor.events.KeyState;
import cs.jirkamayer.gatefields.editor.events.MouseState;
import cs.jirkamayer.gatefields.math.Vector2D;
import cs.jirkamayer.gatefields.scheme.Scheme;
import cs.jirkamayer.gatefields.scheme.Vertex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.Key;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClickSelectionActionTest {
    private ClickSelectionAction action;
    private Selection selection;
    private Scheme scheme;
    private Vertex vertex;
    private Camera camera;

    @BeforeEach
    void setUp() {
        camera = new Camera();
        camera.scale = 100;
        camera.setDisplayDimensions(1000, 1000);
        camera.position = new Vector2D(1, 1);

        scheme = new Scheme();
        vertex = new Vertex(new Vector2D(3, 3));
        scheme.add(vertex);

        selection = new Selection();
        action = new ClickSelectionAction(camera, scheme, selection);
    }

    @Test
    void itSelectsAndDeselectsSingleVertex() {
        Event e = new Event(EventType.MOUSE_DOWN);
        e.keyState = new KeyState();
        e.keyState.buttonPressed[KeyState.SHIFT] = true;

        e.mouseState = new MouseState();
        e.mouseState.buttonPressed[2] = true;
        e.mouseState.position = new Vector2D(700, 700);

        action.actionActivated(e);
        assertTrue(vertex.selected);

        action.actionActivated(e);
        assertFalse(vertex.selected);
    }
}