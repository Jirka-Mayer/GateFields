package cs.jirkamayer.gatefields.editor.actions;

import cs.jirkamayer.gatefields.editor.Selection;
import cs.jirkamayer.gatefields.editor.events.Event;
import cs.jirkamayer.gatefields.editor.events.EventType;
import cs.jirkamayer.gatefields.math.Vector2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SelectVertexActionTest {
    private SelectVertexAction action;
    private Selection selection;

    @BeforeEach
    void setUp() {
        selection = new Selection();
        action = new SelectVertexAction();
    }

    @Test
    void itSelectsSingleVertex() {
        /*Event e = new Event(EventType.MOUSE_DOWN);
        e.mouseState.buttonPressed[2] = true;
        e.mouseState.position = new Vector2D(0, 0);

        action.actionInitiated();*/
    }
}