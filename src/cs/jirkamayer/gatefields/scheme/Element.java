package cs.jirkamayer.gatefields.scheme;

import cs.jirkamayer.gatefields.Camera;
import cs.jirkamayer.gatefields.editor.Selection;
import cs.jirkamayer.gatefields.math.Transform;

import java.util.ArrayList;
import java.util.List;

public abstract class Element {
    public Transform transform = new Transform();
    public List<Vertex> vertices = new ArrayList<>();

    public abstract void draw(Camera c, Selection s);
}
