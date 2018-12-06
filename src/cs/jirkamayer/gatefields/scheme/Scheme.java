package cs.jirkamayer.gatefields.scheme;

import cs.jirkamayer.gatefields.Camera;
import cs.jirkamayer.gatefields.editor.Selection;
import cs.jirkamayer.gatefields.math.Vector2D;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;

public class Scheme {
    private List<Vertex> vertices = new ArrayList<>();
    private List<Element> elements = new ArrayList<>();
    private List<Wire> wires = new ArrayList<>();

    private Simulator simulator = new Simulator(this);

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

    public Simulator getSimulator() {
        return simulator;
    }

    public void add(Element e) {
        elements.add(e);

        for (Vertex v : e.vertices)
            this.addBoundVertex(v);

        simulator.elementAdded(e);
    }

    public void add(Vertex v) {
        if (v.isBound())
            throw new IllegalArgumentException("Bound vertex cannot be added manually.");

        vertices.add(v);
        wiresAtVertex.put(v, new ArrayList<>());
        simulator.vertexAdded(v);
    }

    private void addBoundVertex(Vertex v) {
        vertices.add(v);
        wiresAtVertex.put(v, new ArrayList<>());
        simulator.vertexAdded(v);
    }

    public void add(Wire w) {
        wires.add(w);
        wiresAtVertex.get(w.start).add(w);
        wiresAtVertex.get(w.end).add(w);

        simulator.wireAdded(w);
    }

    public void remove(Vertex v) {
        if (v.isBound())
            throw new IllegalArgumentException("Cannot remove bound vertex.");

        // copy, to prevent ConcurrentModificationException
        List<Wire> wiresToRemove = new ArrayList<>(wiresAtVertex.get(v));
        for (Wire w : wiresToRemove)
            this.remove(w);

        vertices.remove(v);
        wiresAtVertex.remove(v);
        simulator.vertexRemoved(v);
    }

    private void removeBoundVertex(Vertex v) {
        // copy, to prevent ConcurrentModificationException
        List<Wire> wiresToRemove = new ArrayList<>(wiresAtVertex.get(v));
        for (Wire w : wiresToRemove)
            this.remove(w);

        vertices.remove(v);
        wiresAtVertex.remove(v);
        simulator.vertexRemoved(v);
    }

    public void remove(Wire w) {
        wiresAtVertex.get(w.start).remove(w);
        wiresAtVertex.get(w.end).remove(w);
        wires.remove(w);

        simulator.wireRemoved(w);
    }

    public void remove(Element e) {
        for (Vertex v : e.vertices)
            this.removeBoundVertex(v);

        elements.remove(e);
    }

    public Wire getWire(Vertex a, Vertex b) {
        for (Wire w : wiresAtVertex.get(a)) {
            if ((w.start == a && w.end == b) || (w.end == a && w.start == b)) {
                return w;
            }
        }
        return null;
    }

    public List<Vertex> getVertexNeighbors(Vertex v) {
        List<Vertex> neighbors = new ArrayList<>();

        for (Wire w : wiresAtVertex.get(v)) {
            if (w.start == v)
                neighbors.add(w.end);
            else
                neighbors.add(w.start);
        }

        return neighbors;
    }

    public void draw(Camera c, Selection s) {
        for (Wire w : wires)
            w.draw(c, s, simulator);

        for (Element e : elements)
            e.draw(c, s, simulator);

        for (Vertex v : vertices)
            v.draw(c, s, simulator);
    }

    public void clear() {
        vertices.clear();
        elements.clear();
        wires.clear();
        wiresAtVertex = new Hashtable<>();
        simulator.clear();
    }

    ///////////////////
    // Serialization //
    ///////////////////

    public void writeTo(DataOutputStream stream) throws IOException {
        this.writeFreeVertices(stream);
    }

    public void readFrom(DataInputStream stream) throws IOException {
        this.readFreeVertices(stream);
    }

    // Implementation //

    private void writeFreeVertices(DataOutputStream stream) throws IOException {
        stream.writeInt(vertices.size());
        for (Vertex v : vertices)
            v.transform.writeTo(stream);
    }

    private void readFreeVertices(DataInputStream stream) throws IOException {
        int count = stream.readInt();
        for (int i = 0; i < count; i++) {
            Vertex v = new Vertex(Vector2D.ZERO);
            v.transform.readFrom(stream);
            vertices.add(v);
        }
    }
}
