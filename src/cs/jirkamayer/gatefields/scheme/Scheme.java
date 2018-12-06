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
        this.writeElements(stream);
        this.writeWires(stream);
        this.writeActiveVertices(stream);
    }

    public void readFrom(DataInputStream stream) throws IOException {
        List<Vertex> readFreeVertices = this.readFreeVertices(stream);
        List<Element> readElements = this.readElements(stream);
        List<Wire> readWires = this.readWires(stream, readFreeVertices, readElements);
        List<Vertex> readActiveVertices = this.readActiveVertices(stream, readFreeVertices, readElements);

        for (Vertex v : readFreeVertices)
            this.add(v);

        for (Element e : readElements)
            this.add(e);

        for (Wire w : readWires)
            this.add(w);

        for (Vertex v : readActiveVertices)
            simulator.activateVertex(v);

        simulator.processVertexActivations();

        for (Element e : readElements)
            simulator.planElementUpdate(e);
    }

    // Implementation //

    private void writeFreeVertices(DataOutputStream stream) throws IOException {
        int count = 0;
        for (Vertex v : vertices)
            if (!v.isBound())
                count++;

        stream.writeInt(count);
        for (Vertex v : vertices)
            if (!v.isBound())
                v.transform.writeTo(stream);
    }

    private List<Vertex> readFreeVertices(DataInputStream stream) throws IOException {
        List<Vertex> readFreeVertices = new ArrayList<>();
        int count = stream.readInt();
        for (int i = 0; i < count; i++) {
            Vertex v = new Vertex(Vector2D.ZERO);
            v.transform.readFrom(stream);
            readFreeVertices.add(v);
        }
        return readFreeVertices;
    }

    private void writeElements(DataOutputStream stream) throws IOException {
        stream.writeInt(elements.size());
        for (Element e : elements)
            e.writeTo(stream);
    }

    private List<Element> readElements(DataInputStream stream) throws IOException {
        List<Element> readElements = new ArrayList<>();
        int count = stream.readInt();
        for (int i = 0; i < count; i++) {
            readElements.add(Element.readFrom(stream));
        }
        return readElements;
    }

    private void writeWires(DataOutputStream stream) throws IOException {
        stream.writeInt(wires.size());
        for (Wire w : wires) {
            this.writeVertexReference(w.start, stream);
            this.writeVertexReference(w.end, stream);
        }
    }

    private void writeVertexReference(Vertex v, DataOutputStream stream) throws IOException {
        if (v.isBound()) {
            Element e = v.getBoundElement();

            stream.writeInt(1);
            stream.writeInt(elements.indexOf(e));
            stream.writeInt(e.vertices.indexOf(v));
        } else {
            stream.writeInt(0);
            stream.writeInt(vertices.indexOf(v));
        }
    }

    private List<Wire> readWires(
        DataInputStream stream,
        List<Vertex> readFreeVertices,
        List<Element> readElements
    ) throws IOException {
        List<Wire> readWires = new ArrayList<>();
        int count = stream.readInt();
        for (int i = 0; i < count; i++) {
            Vertex start = this.readVertexReference(stream, readFreeVertices, readElements);
            Vertex end = this.readVertexReference(stream, readFreeVertices, readElements);
            readWires.add(new Wire(start, end));
        }
        return readWires;
    }

    private Vertex readVertexReference(
        DataInputStream stream,
        List<Vertex> readFreeVertices,
        List<Element> readElements
    ) throws IOException {
        int type = stream.readInt();

        if (type == 0) {
            return readFreeVertices.get(stream.readInt());
        } else if (type == 1) {
            int elementIndex = stream.readInt();
            int vertexIndex = stream.readInt();
            return readElements.get(elementIndex).vertices.get(vertexIndex);
        } else {
            throw new IOException("Unknown vertex reference type.");
        }
    }

    private void writeActiveVertices(DataOutputStream stream) throws IOException {
        int count = 0;
        for (Vertex v : vertices)
            if (simulator.isActive(v))
                count++;

        stream.writeInt(count);
        for (Vertex v : vertices)
            if (simulator.isActive(v))
                this.writeVertexReference(v, stream);
    }

    private List<Vertex> readActiveVertices(
        DataInputStream stream,
        List<Vertex> readFreeVertices,
        List<Element> readElements
    ) throws IOException {
        List<Vertex> readActiveVertices = new ArrayList<>();
        int count = stream.readInt();
        for (int i = 0; i < count; i++) {
            readActiveVertices.add(
                this.readVertexReference(stream, readFreeVertices, readElements)
            );
        }
        return readActiveVertices;
    }
}
