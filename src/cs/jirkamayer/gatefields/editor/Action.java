package cs.jirkamayer.gatefields.editor;

import cs.jirkamayer.gatefields.editor.events.Event;
import cs.jirkamayer.gatefields.editor.events.EventType;
import cs.jirkamayer.gatefields.editor.events.KeyState;

public abstract class Action {

    private DeactivationCallback deactivationCallback = null;
    private RepaintCallback repaintCallback = null;

    public interface DeactivationCallback {
        void deactivateAction(Action a);
    }

    public interface RepaintCallback {
        void repaint();
    }

    public void setDeactivationCallback(DeactivationCallback callback) {
        this.deactivationCallback = callback;
    }

    public void setRepaintCallback(RepaintCallback repaintCallback) {
        this.repaintCallback = repaintCallback;
    }

    /**
     * Listens for activating event if not active
     */
    public abstract boolean filterActivatingEvent(Event e);

    public void actionActivated(Event e) {

    }

    public final void deactivate() {
        if (deactivationCallback != null)
            deactivationCallback.deactivateAction(this);
    }

    /**
     * Handles all events when active
     */
    public void eventOccurred(Event e) {
        if (e.getEventType() == EventType.KEY_DOWN) {
            if (e.keyState.causeKey == KeyState.ENTER) {
                this.actionSubmitted();
            }

            if (e.keyState.causeKey == KeyState.ESCAPE) {
                this.actionCancelled();
            }
        }
    }

    public void actionCancelled() {
        this.deactivate();
        this.repaint();
    }

    public void actionSubmitted() {
        this.deactivate();
        this.repaint();
    }

    protected void repaint() {
        if (repaintCallback != null)
            repaintCallback.repaint();
    }
}
