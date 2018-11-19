package cs.jirkamayer.gatefields.editor;

import cs.jirkamayer.gatefields.scheme.Element;
import cs.jirkamayer.gatefields.scheme.Vertex;

import java.util.ArrayList;
import java.util.List;

public class Selection {
    private List<Vertex> vertices = new ArrayList<>();
    private List<Element> elements = new ArrayList<>();

    public void select(Vertex v) {
        if (v.selected)
            return;

        vertices.add(v);
        v.selected = true;

        if (v.isBound())
            this.selectJustElement(v.getBoundElement());
    }

    public void deselect(Vertex v) {
        if (!v.selected)
            return;

        vertices.remove(v);
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
}
