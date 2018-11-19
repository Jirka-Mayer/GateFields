package cs.jirkamayer.gatefields.scheme;

import cs.jirkamayer.gatefields.Camera;

import java.util.ArrayList;
import java.util.List;

public class Scheme {
    public List<Vertex> vertices = new ArrayList<>();
    public List<Element> elements = new ArrayList<>();
    public List<Wire> wires = new ArrayList<>();

    public void add(Element e) {
        this.elements.add(e);

        for (Vertex v : e.vertices)
            vertices.add(v);
    }

    public void add(Vertex v) {
        if (v.isBound())
            throw new IllegalArgumentException("Bound vertex cannot be added manually.");

        vertices.add(v);
    }

    public void add(Wire w) {
        wires.add(w);
    }

    public void draw(Camera c) {
        for (Wire w : wires)
            w.draw(c);

        for (Element e : elements)
            e.draw(c);

        for (Vertex v : vertices)
            v.draw(c);
    }
}
