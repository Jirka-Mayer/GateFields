package cs.jirkamayer.gatefields.editor;

import cs.jirkamayer.gatefields.editor.events.Event;

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
        // TODO: handle escape and enter, call "actionCanceled", "actionSubmitted"
    }

    public void actionCancelled() {

    }

    public void actionSubmitted() {

    }

    protected void repaint() {
        if (repaintCallback != null)
            repaintCallback.repaint();
    }
}
