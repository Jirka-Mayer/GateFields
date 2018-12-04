package cs.jirkamayer.gatefields.scheme;

import java.util.*;

public class Simulator {
    private Scheme scheme;

    private Set<Vertex> activeVertices = new HashSet<>();
    private Dictionary<Vertex, Component> componentAtVertex = new Hashtable<>();

    // Elements that should be examined due to signal propagation
    private Set<Element> touchedElements = new HashSet<>();

    private SimulationQueue simulationQueue = new SimulationQueue();

    private class Component {
        public Set<Vertex> vertices = new HashSet<>();
        public int activeVertices = 0;

        public boolean isActive() {
            return activeVertices > 0;
        }
    }

    public Simulator(Scheme scheme) {
        this.scheme = scheme;
    }

    public SimulationQueue getSimulationQueue() {
        return simulationQueue;
    }

    public boolean hasSignal(Vertex v) {
        return componentAtVertex.get(v).isActive();
    }

    public boolean hasSignal(Wire w) {
        // start has the same value as end
        return this.hasSignal(w.start) && this.hasSignal(w.end);
    }

    public boolean isActive(Vertex v) {
        return activeVertices.contains(v);
    }

    private void touchElementsInComponent(Component c) {
        for (Vertex v : c.vertices) {
            if (activeVertices.contains(v))
                continue;

            if (v.isBound())
                touchedElements.add(v.getBoundElement());
        }
    }

    ////////////////////////
    // Vertex activations //
    ////////////////////////

    public void activateVertex(Vertex vertex) {
        Component c = componentAtVertex.get(vertex);

        activeVertices.add(vertex);
        c.activeVertices++;

        // was active before
        if (c.activeVertices > 1)
            return;

        this.touchElementsInComponent(c);
    }

    public void deactivateVertex(Vertex vertex) {
        Component c = componentAtVertex.get(vertex);

        activeVertices.remove(vertex);
        c.activeVertices--;

        // stayed active
        if (c.isActive()) {
            // just feed signal back to the element
            if (vertex.isBound())
                touchedElements.add(vertex.getBoundElement());

            return;
        }

        this.touchElementsInComponent(c);
    }

    public void processVertexActivations() {
        for (Element e : touchedElements)
            e.signalsChanged(this);
        touchedElements.clear();
    }

    /////////////////////
    // Gate simulation //
    /////////////////////

    public void simulationTick(double elapsedTime) {
        if (!touchedElements.isEmpty())
            this.processVertexActivations();

        simulationQueue.advanceTime(elapsedTime);

        while (simulationQueue.hasItemToExecute()) {
            simulationQueue.executeNext(this);
        }

        this.processVertexActivations();
    }

    ////////////////////
    // Scheme changes //
    ////////////////////

    public void vertexAdded(Vertex v) {
        Component c = new Component();
        c.vertices.add(v);

        componentAtVertex.put(v, c);
    }

    public void vertexRemoved(Vertex v) {
        componentAtVertex.remove(v);
    }

    public void elementAdded(Element e) {
        e.initializeSignals(this);
    }

    public void wireAdded(Wire w) {
        Component a = componentAtVertex.get(w.start);
        Component b = componentAtVertex.get(w.end);

        if (a == b)
            return;

        Component j = this.joinComponents(a, b);

        if (a.isActive() != b.isActive())
            this.touchElementsInComponent(j);
    }

    public void wireRemoved(Wire w) {
        Set<Vertex> startVertices = this.constructComponent(w.start);
        Component originalComponent = componentAtVertex.get(w.start);

        if (startVertices.contains(w.end))
            return;

        Component a = new Component();
        a.vertices = startVertices;
        Component b = new Component();
        b.vertices = this.constructComponent(w.end);

        for (Vertex v : a.vertices) {
            componentAtVertex.put(v, a);
            if (activeVertices.contains(v))
                a.activeVertices++;
        }

        for (Vertex v : b.vertices) {
            componentAtVertex.put(v, b);
            if (activeVertices.contains(v))
                b.activeVertices++;
        }

        if (a.isActive() != originalComponent.isActive())
            this.touchElementsInComponent(a);

        if (b.isActive() != originalComponent.isActive())
            this.touchElementsInComponent(b);
    }

    private Component joinComponents(Component a, Component b) {
        Component j = new Component();

        j.vertices.addAll(a.vertices);
        j.vertices.addAll(b.vertices);
        j.activeVertices = a.activeVertices + b.activeVertices;

        for (Vertex v : a.vertices)
            componentAtVertex.put(v, j);

        for (Vertex v : b.vertices)
            componentAtVertex.put(v, j);

        return j;
    }

    private Set<Vertex> constructComponent(Vertex v) {
        Set<Vertex> closedVertices = new HashSet<>();
        Queue<Vertex> openVertices = new LinkedList<>();

        openVertices.add(v);

        while (!openVertices.isEmpty()) {
            v = openVertices.remove();
            closedVertices.add(v);

            for (Vertex n : scheme.getVertexNeighbors(v)) {
                if (!closedVertices.contains(n))
                    openVertices.add(n);
            }
        }

        return closedVertices;
    }
}
