package cs.jirkamayer.gatefields.scheme;

import cs.jirkamayer.gatefields.Camera;
import cs.jirkamayer.gatefields.editor.Selection;

import java.util.*;

public class Scheme {
    private List<Vertex> vertices = new ArrayList<>();
    private List<Element> elements = new ArrayList<>();
    private List<Wire> wires = new ArrayList<>();

    /**
     * Helps traversing the scheme
     */
    private Dictionary<Vertex, List<Wire>> wiresAtVertex = new Hashtable<>();

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
        elements.add(e);

        for (Vertex v : e.vertices)
            this.addBoundVertex(v);
    }

    public void add(Vertex v) {
        if (v.isBound())
            throw new IllegalArgumentException("Bound vertex cannot be added manually.");

        vertices.add(v);
        wiresAtVertex.put(v, new ArrayList<>());
    }

    private void addBoundVertex(Vertex v) {
        vertices.add(v);
        wiresAtVertex.put(v, new ArrayList<>());
    }

    public void add(Wire w) {
        wires.add(w);
        wiresAtVertex.get(w.start).add(w);
        wiresAtVertex.get(w.end).add(w);
    }

    public void remove(Vertex v) {
        if (v.isBound())
            throw new IllegalArgumentException("Cannot remove bound vertex.");

        for (Wire w : wiresAtVertex.get(v))
            this.remove(w);

        vertices.remove(v);
        wiresAtVertex.remove(v);
    }

    private void removeBoundVertex(Vertex v) {
        for (Wire w : wiresAtVertex.get(v))
            this.remove(w);

        vertices.remove(v);
        wiresAtVertex.remove(v);
    }

    public void remove(Wire w) {
        wiresAtVertex.get(w.start).remove(w);
        wiresAtVertex.get(w.end).remove(w);
        wires.remove(w);
    }

    public void remove(Element e) {
        for (Vertex v : e.vertices)
            this.removeBoundVertex(v);

        elements.remove(e);
    }

    public void draw(Camera c, Selection s) {
        for (Wire w : wires)
            w.draw(c, s);

        for (Element e : elements)
            e.draw(c, s);

        for (Vertex v : vertices)
            v.draw(c, s);
    }
}
