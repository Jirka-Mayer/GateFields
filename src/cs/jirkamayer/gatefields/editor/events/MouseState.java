package cs.jirkamayer.gatefields.editor.events;

import cs.jirkamayer.gatefields.math.Vector2D;

public class MouseState {
    public static final int LMB = 1;
    public static final int RMB = 3;

    public Vector2D position = Vector2D.ZERO;
    public boolean[] buttonPressed = new boolean[10];

    /**
     * Button that caused this event to occur
     */
    public int causeButton = -1;
}
