package cs.jirkamayer.gatefields.editor.actions;

import cs.jirkamayer.gatefields.editor.events.Event;
import cs.jirkamayer.gatefields.editor.events.EventType;

public class SelectVertexAction {
    public boolean filterInitiatingEvent(Event e) {
        if (e.getEventType() != EventType.MOUSE_DOWN)
            return false;

        return e.mouseState.buttonPressed[3];
    }

    public void actionInitiated(Event e) {

    }
}
