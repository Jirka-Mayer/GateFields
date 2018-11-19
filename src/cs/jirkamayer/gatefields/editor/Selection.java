package cs.jirkamayer.gatefields.editor;

import cs.jirkamayer.gatefields.math.Vector2D;
import cs.jirkamayer.gatefields.scheme.Element;
import cs.jirkamayer.gatefields.scheme.Vertex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Selection {
    private List<Vertex> vertices = new ArrayList<>();
    private List<Vertex> freeVertices = new ArrayList<>();
    private List<Element> elements = new ArrayList<>();

    public List<Vertex> getVertices() {
        return Collections.unmodifiableList(vertices);
    }

    public void select(Vertex v) {
        if (v.selected)
            return;

        vertices.add(v);
        if (!v.isBound())
            freeVertices.add(v);
        v.selected = true;

        if (v.isBound())
            this.selectJustElement(v.getBoundElement());
    }

    public void deselect(Vertex v) {
        if (!v.selected)
            return;

        vertices.remove(v);
        freeVertices.remove(v);
        v.selected = false;

        if (v.isBound())
            this.checkElementSelection(v.getBoundElement());
    }

    public void deselectAll() {
        for (Element e : elements)
            e.selected = false;

        for (Vertex v : vertices)
            v.selected = false;

        elements.clear();
        vertices.clear();
        freeVertices.clear();
    }

    private void selectJustElement(Element e) {
        if (e.selected)
            return;

        elements.add(e);
        e.selected = true;
    }

    private void deselectJustElement(Element e) {
        if (!e.selected)
            return;

        elements.remove(e);
        e.selected = false;
    }

    private void checkElementSelection(Element e) {
        for (Vertex v : e.vertices) {
            if (v.selected) {
                this.selectJustElement(e);
                return;
            }
        }

        this.deselectJustElement(e);
    }

    ////////////////////////
    // MoveAction helpers //
    ////////////////////////
    // (to keep the underlying fields encapsulated)

    public Vector2D[] getFreeVertexPositions() {
        Vector2D[] out = new Vector2D[freeVertices.size()];

        for (int i = 0; i < out.length; i++)
            out[i] = freeVertices.get(i).transform.getPosition();

        return out;
    }

    public void setFreeVertexPositions(Vector2D[] pos, Vector2D delta) {
        if (pos.length != freeVertices.size())
            throw new IllegalArgumentException("Wrong array size.");

        for (int i = 0; i < pos.length; i++)
            freeVertices.get(i).transform.setPosition(pos[i].plus(delta));
    }

    public Vector2D[] getElementPositions() {
        Vector2D[] out = new Vector2D[elements.size()];

        for (int i = 0; i < out.length; i++)
            out[i] = elements.get(i).transform.getPosition();

        return out;
    }

    public void setElementPositions(Vector2D[] pos, Vector2D delta) {
        if (pos.length != elements.size())
            throw new IllegalArgumentException("Wrong array size.");

        for (int i = 0; i < pos.length; i++)
            elements.get(i).transform.setPosition(pos[i].plus(delta));
    }
}
