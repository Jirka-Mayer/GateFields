package cs.jirkamayer.gatefields.editor.events;

import cs.jirkamayer.gatefields.math.Vector2D;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class EventDispatcher implements
    MouseListener,
    MouseMotionListener,
    MouseWheelListener,
    KeyListener
{

    private MouseState mouseState = new MouseState();
    private KeyState keyState = new KeyState();

    private List<EventListener> listeners = new ArrayList<>();

    public interface EventListener {
        void eventOccurred(Event e);
    }

    public void addEventListener(EventListener listener) {
        listeners.add(listener);
    }

    private void dispatch(Event e) {
        e.mouseState = mouseState;
        e.keyState = keyState;

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
        mouseState.position = new Vector2D(e.getX(), e.getY());
        mouseState.buttonPressed[e.getButton()] = true;

        this.dispatch(new Event(EventType.MOUSE_DOWN));
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseState.position = new Vector2D(e.getX(), e.getY());
        mouseState.buttonPressed[e.getButton()] = false;

        this.dispatch(new Event(EventType.MOUSE_UP));
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    /////////////////////////
    // MouseMotionListener //
    /////////////////////////

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    ////////////////////////
    // MouseWheelListener //
    ////////////////////////

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

    }

    /////////////////
    // KeyListener //
    /////////////////

    @Override
    public void keyTyped(KeyEvent e) {
        // not interesting for us
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keyState.buttonPressed[e.getKeyCode()] = true;

        this.dispatch(new Event(EventType.KEY_DOWN));
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyState.buttonPressed[e.getKeyCode()] = false;

        this.dispatch(new Event(EventType.KEY_UP));
    }
}
