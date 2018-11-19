package cs.jirkamayer.gatefields.editor.actions;

import cs.jirkamayer.gatefields.editor.Action;
import cs.jirkamayer.gatefields.editor.events.Event;
import cs.jirkamayer.gatefields.editor.events.EventDispatcher;

import java.util.ArrayList;
import java.util.List;

public class ActionController implements EventDispatcher.EventListener, Action.DeactivationCallback {
    private List<Action> actions = new ArrayList<>();
    private Action activeAction = null;

    public ActionController(EventDispatcher eventDispatcher) {
        eventDispatcher.addEventListener(this);
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
        actions.add(a);
    }

    @Override
    public void deactivateAction(Action a) {
        if (a == activeAction)
            activeAction = null;
    }
}
