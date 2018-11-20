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
        assertTrue(selection.isSelected(e), "Element has not been selected");

        selection.select(e.vertices.get(1));
        assertTrue(selection.isSelected(e), "Element should stay selected on selection");

        selection.deselect(e.vertices.get(0));
        assertTrue(selection.isSelected(e), "Element should stay selected on partial deselection");

        selection.deselect(e.vertices.get(1));
        assertFalse(selection.isSelected(e), "Element should have been deselected");
    }
}