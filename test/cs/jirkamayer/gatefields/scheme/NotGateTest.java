package cs.jirkamayer.gatefields.scheme;

import cs.jirkamayer.gatefields.math.Vector2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotGateTest {
    private Scheme scheme;
    private Simulator sim;
    private NotGate notGate;
    private Vertex inputVertex;

    @BeforeEach
    void setUp() {
        scheme = new Scheme();
        sim = scheme.getSimulator();

        notGate = new NotGate();
        scheme.add(notGate);

        inputVertex = new Vertex(Vector2D.ZERO);
        scheme.add(inputVertex);
        scheme.add(new Wire(inputVertex, notGate.inputVertex));

        sim.simulationTick(NotGate.DELAY * 10);
    }

    @Test
    void itInvertsSignal() {
        assertTrue(sim.hasSignal(notGate.outputVertex));

        // activation is buffered until the simulation step ends
        sim.activateVertex(inputVertex);
        assertTrue(sim.hasSignal(notGate.outputVertex));

        sim.simulationTick(NotGate.DELAY * 10);
        assertFalse(sim.hasSignal(notGate.outputVertex));

        sim.deactivateVertex(inputVertex);
        sim.simulationTick(NotGate.DELAY * 10);
        assertTrue(sim.hasSignal(notGate.outputVertex));
    }

    @Test
    void itIgnoresFlickerActivation() {
        sim.activateVertex(inputVertex);
        sim.simulationTick(NotGate.DELAY / 4);
        sim.deactivateVertex(inputVertex);

        sim.simulationTick(NotGate.DELAY / 4);
        assertTrue(sim.hasSignal(notGate.outputVertex));

        sim.simulationTick(NotGate.DELAY / 4);
        assertTrue(sim.hasSignal(notGate.outputVertex));

        sim.simulationTick(NotGate.DELAY / 4);
        assertTrue(sim.hasSignal(notGate.outputVertex));

        sim.simulationTick(NotGate.DELAY / 4);
        assertTrue(sim.hasSignal(notGate.outputVertex));
    }

    @Test
    void itIgnoresFlickerDeactivation() {
        sim.activateVertex(inputVertex);
        sim.simulationTick(NotGate.DELAY * 10);

        sim.deactivateVertex(inputVertex);
        sim.simulationTick(NotGate.DELAY / 4);
        sim.activateVertex(inputVertex);

        sim.simulationTick(NotGate.DELAY / 4);
        assertFalse(sim.hasSignal(notGate.outputVertex));

        sim.simulationTick(NotGate.DELAY / 4);
        assertFalse(sim.hasSignal(notGate.outputVertex));

        sim.simulationTick(NotGate.DELAY / 4);
        assertFalse(sim.hasSignal(notGate.outputVertex));

        sim.simulationTick(NotGate.DELAY / 4);
        assertFalse(sim.hasSignal(notGate.outputVertex));
    }
}