package cs.jirkamayer.gatefields.editor.actions;

import cs.jirkamayer.gatefields.Camera;
import cs.jirkamayer.gatefields.editor.Action;
import cs.jirkamayer.gatefields.editor.events.Event;
import cs.jirkamayer.gatefields.editor.events.EventType;

public class ZoomCameraAction extends Action {
    public static final float SCALING_FACTOR = 0.9f;

    private Camera camera;

    public ZoomCameraAction(Camera camera) {
        this.camera = camera;
    }

    @Override
    public boolean filterActivatingEvent(Event e) {
        return e.getEventType() == EventType.SCROLL;
    }

    @Override
    public void actionActivated(Event e) {
        super.actionActivated(e);

        if (e.getEventType() != EventType.SCROLL)
            return;

        if (e.mouseState.scroll > 0) {
            camera.scale *= SCALING_FACTOR;
        } else if (e.mouseState.scroll < 0) {
            camera.scale /= SCALING_FACTOR;
        }

        this.repaint();

        this.deactivate();
    }

    @Override
    public void drawAction(Camera camera) {}
}
