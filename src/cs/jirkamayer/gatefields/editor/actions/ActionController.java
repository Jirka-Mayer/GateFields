package cs.jirkamayer.gatefields.editor.actions;

import cs.jirkamayer.gatefields.editor.Action;
import cs.jirkamayer.gatefields.editor.events.Event;
import cs.jirkamayer.gatefields.editor.events.EventDispatcher;

import java.util.ArrayList;
import java.util.List;

public class ActionController implements
    EventDispatcher.EventListener,
    Action.DeactivationCallback,
    Action.RepaintCallback
{
    private List<Action> actions = new ArrayList<>();
    private Action activeAction = null;
    private Action.RepaintCallback repaintCallback = null;

    public ActionController(EventDispatcher eventDispatcher, Action.RepaintCallback repaintCallback) {
        eventDispatcher.addEventListener(this);
        this.repaintCallback = repaintCallback;
    }

    /**
     * Feed events to a manually activated action
     */
    public void activateActionManually(Action a) {
        if (activeAction != null)
            return;

        a.setDeactivationCallback(this);
        a.setRepaintCallback(this);
        activeAction = a;
        a.actionActivatedManually();
    }

    @Override
    public void eventOccurred(Event e) {
        if (activeAction == null) {
            this.tryToActivateAnAction(e);
            return;
        }

        activeAction.eventOccurred(e);
    }

    private void tryToActivateAnAction(Event e) {
        for (Action a : actions) {
            if (a.filterActivatingEvent(e)) {
                activeAction = a;
                a.actionActivated(e);
                return;
            }
        }
    }

    public void registerAction(Action a) {
        a.setDeactivationCallback(this);
        a.setRepaintCallback(this);
        actions.add(a);
    }

    @Override
    public void deactivateAction(Action a) {
        if (a == activeAction)
            activeAction = null;
    }

    @Override
    public void repaint() {
        if (this.repaintCallback != null)
            this.repaintCallback.repaint();
    }
}
