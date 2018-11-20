package cs.jirkamayer.gatefields.editor.events;

public class KeyState {
    public static final int ENTER = 10;
    public static final int SHIFT = 16;
    public static final int CTRL = 17;
    public static final int ALT = 18;
    public static final int ESCAPE = 27;

    public static final int E = 69;
    public static final int G = 71;
    public static final int W = 87;
    public static final int X = 88;

    public boolean[] keyPressed = new boolean[256];

    /**
     * Key that caused this event to occur
     */
    public int causeKey = -1;
}
