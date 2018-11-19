package cs.jirkamayer.gatefields.scheme;

import cs.jirkamayer.gatefields.Camera;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Scheme {
    private List<Vertex> vertices = new ArrayList<>();
    private List<Element> elements = new ArrayList<>();
    private List<Wire> wires = new ArrayList<>();

    public List<Vertex> getVertices() {
        return Collections.unmodifiableList(vertices);
    }

    public List<Element> getElements() {
        return Collections.unmodifiableList(elements);
    }

    public List<Wire> getWires() {
        return Collections.unmodifiableList(wires);
    }

    public void add(Element e) {
        this.elements.add(e);
        vertices.addAll(e.vertices);
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
