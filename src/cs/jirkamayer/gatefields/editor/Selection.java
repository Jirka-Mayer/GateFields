package cs.jirkamayer.gatefields.editor;

import cs.jirkamayer.gatefields.math.Vector2D;
import cs.jirkamayer.gatefields.scheme.Element;
import cs.jirkamayer.gatefields.scheme.Vertex;
import cs.jirkamayer.gatefields.scheme.Wire;

import java.util.*;

public class Selection {
    private List<Vertex> vertices = new ArrayList<>();
    private List<Vertex> freeVertices = new ArrayList<>();
    private List<Element> elements = new ArrayList<>();

    // for faster "isSelected" query
    private Set<Vertex> vertexSet = new HashSet<>();
    private Set<Element> elementSet = new HashSet<>();

    public List<Vertex> getVertices() {
        return Collections.unmodifiableList(vertices);
    }

    public List<Vertex> getFreeVertices() {
        return Collections.unmodifiableList(freeVertices);
    }

    public List<Element> getElements() {
        return Collections.unmodifiableList(elements);
    }

    public boolean isSelected(Vertex v) {
        return vertexSet.contains(v);
    }

    public boolean isSelected(Element e) {
        return elementSet.contains(e);
    }

    public boolean isSelected(Wire w) {
        return this.isSelected(w.start) && this.isSelected(w.end);
    }

    public boolean isEmpty() {
        // no need to check for elements or wires, because
        // they are conditioned by vertices
        return vertices.size() == 0;
    }

    public void select(Vertex v) {
        if (isSelected(v))
            return;

        vertices.add(v);
        if (!v.isBound())
            freeVertices.add(v);
        vertexSet.add(v);

        if (v.isBound())
            this.selectJustElement(v.getBoundElement());
    }

    public void deselect(Vertex v) {
        if (!isSelected(v))
            return;

        vertices.remove(v);
        freeVertices.remove(v);
        vertexSet.remove(v);

        if (v.isBound())
            this.checkElementSelection(v.getBoundElement());
    }

    public void deselectAll() {
        for (Element e : elements)
            elementSet.remove(e);

        for (Vertex v : vertices)
            vertexSet.remove(v);

        elements.clear();
        vertices.clear();
        freeVertices.clear();
    }

    private void selectJustElement(Element e) {
        if (isSelected(e))
            return;

        elements.add(e);
        elementSet.add(e);
    }

    private void deselectJustElement(Element e) {
        if (!isSelected(e))
            return;

        elements.remove(e);
        elementSet.remove(e);
    }

    private void checkElementSelection(Element e) {
        for (Vertex v : e.vertices) {
            if (isSelected(v)) {
                this.selectJustElement(e);
                return;
            }
        }

        this.deselectJustElement(e);
    }
}
