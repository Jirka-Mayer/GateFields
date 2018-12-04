package cs.jirkamayer.gatefields.scheme;

import cs.jirkamayer.gatefields.Camera;
import cs.jirkamayer.gatefields.Renderer;
import cs.jirkamayer.gatefields.editor.Selection;
import cs.jirkamayer.gatefields.math.Vector2D;

public class NotGate extends Element {
    public static double DELAY = 0.2;

    private Vertex inputVertex, outputVertex;

    private SimulationEvent activateOutput = (Simulator sim) -> {
        sim.activateVertex(outputVertex);
    };

    private SimulationEvent deactivateOutput = (Simulator sim) -> {
        sim.deactivateVertex(outputVertex);
    };

    public NotGate() {
        vertices.add(inputVertex = new Vertex(new Vector2D(-1, 0), this));
        vertices.add(outputVertex = new Vertex(new Vector2D(2, 0), this));
    }

    @Override
    public void initializeSignals(Simulator sim) {
        sim.activateVertex(outputVertex);
    }

    @Override
    public void signalsChanged(Simulator sim) {
        SimulationQueue queue = sim.getSimulationQueue();

        if (sim.hasSignal(inputVertex)) {
            this.planDeactivation(sim, queue);
        } else {
            this.planActivation(sim, queue);
        }
    }

    private void planActivation(Simulator sim, SimulationQueue queue) {
        // activate if it's deactivated now and there is
        // no plan of activation it in the future

        if (sim.isActive(outputVertex)) {
            // may exist - be planned earlier
            queue.remove(this, deactivateOutput);
        } else {
            if (!queue.contains(this, activateOutput))
                queue.addOrMove(DELAY, this, activateOutput);
        }
    }

    private void planDeactivation(Simulator sim, SimulationQueue queue) {
        // deactivate if it's activated now and there is
        // no plan of deactivating it in the future

        if (sim.isActive(outputVertex)) {
            if (!queue.contains(this, deactivateOutput))
                queue.addOrMove(DELAY, this, deactivateOutput);
        } else {
            // may exist - be planned earlier
            queue.remove(this, activateOutput);
        }
    }

    @Override
    public void draw(Camera c, Selection s, Simulator sim) {
        Renderer r = c.getRenderer();
        c.setTransform(transform);

        boolean selected = s.isSelected(this);

        // stem
        r.drawWire(
            new Vector2D(-1, 0),
            new Vector2D(0, 0),
            sim.hasSignal(inputVertex),
            selected
        );

        // triangle
        r.drawElementLine(new Vector2D(0, -0.8f), new Vector2D(0, 0.8f), selected);
        r.drawElementLine(new Vector2D(0, -0.8f), new Vector2D(1, 0), selected);
        r.drawElementLine(new Vector2D(0, 0.8f), new Vector2D(1, 0), selected);

        // peak
        r.drawWire(
            new Vector2D(1, 0),
            new Vector2D(2, 0),
            sim.hasSignal(outputVertex),
            selected
        );

        // circle
        r.drawElementCircle(new Vector2D(1.1f, 0), selected);

        // origin
        r.drawElementOrigin(Vector2D.ZERO, selected);
    }
}
