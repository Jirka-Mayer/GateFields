package cs.jirkamayer.gatefields.editor;

import cs.jirkamayer.gatefields.scheme.NotGate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SelectionTest {
    @Test
    void itSelectsElementByItsVertices() {
        NotGate e = new NotGate();
        Selection selection = new Selection();

        selection.select(e.vertices.get(0));
        assertTrue(e.selected, "Element has not been selected");

        selection.select(e.vertices.get(1));
        assertTrue(e.selected, "Element should stay selected on selection");

        selection.deselect(e.vertices.get(0));
        assertTrue(e.selected, "Element should stay selected on partial deselection");

        selection.deselect(e.vertices.get(1));
        assertFalse(e.selected, "Element should have been deselected");
    }
}