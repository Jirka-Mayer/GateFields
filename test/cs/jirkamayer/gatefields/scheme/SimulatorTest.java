package cs.jirkamayer.gatefields.scheme;

import cs.jirkamayer.gatefields.math.Vector2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimulatorTest {
    private Scheme scheme;
    private Simulator sim;
    private Vertex a, b, c;
    private Wire ab, bc;

    @BeforeEach
    void setUp() {
        scheme = new Scheme();
        sim = scheme.getSimulator();

        // A <---> B <---> C

        scheme.add(a = new Vertex(Vector2D.ZERO));
        scheme.add(b = new Vertex(Vector2D.ZERO));
        scheme.add(c = new Vertex(Vector2D.ZERO));

        scheme.add(ab = new Wire(a, b));
        scheme.add(bc = new Wire(b, c));
    }

    @Test
    void vertexCannotBeActivatedOrDeactivatedTwice() {
        assertFalse(sim.hasSignal(a));

        sim.activateVertex(a);
        sim.processVertexActivations();

        assertTrue(sim.hasSignal(a));

        sim.activateVertex(a);
        sim.processVertexActivations();

        assertTrue(sim.hasSignal(a));

        sim.deactivateVertex(a);
        sim.processVertexActivations();

        assertFalse(sim.hasSignal(a));

        sim.deactivateVertex(a);
        sim.processVertexActivations();

        assertFalse(sim.hasSignal(a));

        sim.activateVertex(a);
        sim.processVertexActivations();

        assertTrue(sim.hasSignal(a));
    }

    @Test
    void itActivatesComponent() {
        assertFalse(sim.hasSignal(a));
        assertFalse(sim.hasSignal(b));
        assertFalse(sim.hasSignal(c));

        sim.activateVertex(a);

        assertFalse(sim.hasSignal(a));
        assertFalse(sim.hasSignal(b));
        assertFalse(sim.hasSignal(c));

        sim.processVertexActivations();

        assertTrue(sim.hasSignal(a));
        assertTrue(sim.hasSignal(b));
        assertTrue(sim.hasSignal(c));
    }

    @Test
    void activeVertexChanges() {
        sim.activateVertex(a);
        sim.processVertexActivations();

        sim.deactivateVertex(a);
        sim.activateVertex(c);
        sim.processVertexActivations();

        assertTrue(sim.hasSignal(a));
        assertTrue(sim.hasSignal(b));
        assertTrue(sim.hasSignal(c));
    }

    @Test
    void wireRemoval() {
        sim.activateVertex(a);
        sim.processVertexActivations();

        scheme.remove(bc);

        assertFalse(sim.hasSignal(c));
    }

    @Test
    void wireAddition() {
        scheme.remove(bc);
        sim.activateVertex(a);
        sim.processVertexActivations();

        scheme.add(bc);

        assertTrue(sim.hasSignal(c));
    }
}