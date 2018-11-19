package cs.jirkamayer.gatefields.editor.events;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class EventDispatcher implements MouseListener {

    private MouseState mouseState = new MouseState();

    private List<EventListener> listeners = new ArrayList<>();

    public interface EventListener {
        void eventOccurred(Event e);
    }

    public void addEventListener(EventListener listener) {
        listeners.add(listener);
    }

    private void dispatch(Event e) {
        e.mouseState = mouseState;

        for (EventListener l : listeners)
            l.eventOccurred(e);
    }

    ///////////////////
    // MouseListener //
    ///////////////////

    @Override
    public void mouseClicked(MouseEvent e) {
        // not interesting for us
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseState.buttonPressed[e.getButton()] = true;

        this.dispatch(new Event(EventType.MOUSE_DOWN));
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseState.buttonPressed[e.getButton()] = false;

        this.dispatch(new Event(EventType.MOUSE_UP));
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
