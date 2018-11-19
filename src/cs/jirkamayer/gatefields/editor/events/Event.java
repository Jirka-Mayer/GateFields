package cs.jirkamayer.gatefields.editor.events;

public class Event {

    /*
        Each event holds state of the entire system,
        the eventType property only tells us, how this
        event was fired
     */

    private EventType eventType;
    public MouseState mouseState = null;
    public KeyState keyState = null;

    public Event(EventType eventType) {
        this.eventType = eventType;
    }

    public EventType getEventType() {
        return eventType;
    }
}
