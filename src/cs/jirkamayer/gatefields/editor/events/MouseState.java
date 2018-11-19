package cs.jirkamayer.gatefields.editor.events;

import cs.jirkamayer.gatefields.math.Vector2D;

public class MouseState {
    public static final int LMB = 2;
    public static final int RMB = 4;

    public Vector2D position = Vector2D.ZERO;
    public boolean[] buttonPressed = new boolean[10];
}
