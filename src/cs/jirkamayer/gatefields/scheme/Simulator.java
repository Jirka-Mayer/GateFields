package cs.jirkamayer.gatefields.scheme;

import java.util.*;

public class Simulator {
    private Scheme scheme;

    private Set<Vertex> activeVertices = new HashSet<>();
    private Dictionary<Vertex, Component> componentAtVertex = new Hashtable<>();

    private SimulationQueue simulationQueue = new SimulationQueue();

    private List<ActivationChange> activationChanges = new ArrayList<>();

    private static class Component {
        public Set<Vertex> vertices = new HashSet<>();
        public int activeVertices = 0;

        public boolean isActive() {
            return activeVertices > 0;
        }
    }

    private static class ActivationChange {
        public Vertex vertex;
        public boolean activity;

        public ActivationChange(Vertex vertex, boolean activity) {
            this.vertex = vertex;
            this.activity = activity;
        }
    }

    public Simulator(Scheme scheme) {
        this.scheme = scheme;
    }

    public boolean hasSignal(Vertex v) {
        return componentAtVertex.get(v).isActive();
    }

    public boolean hasSignal(Wire w) {
        // start has the same value as end
        return this.hasSignal(w.start);
    }

    public boolean isActive(Vertex v) {
        return activeVertices.contains(v);
    }

    public void clear() {
        activeVertices.clear();
        componentAtVertex = new Hashtable<>();
        simulationQueue.clear();
        activationChanges.clear();
    }

    ////////////////////////
    // Vertex activations //
    ////////////////////////

    public void activateVertex(Vertex vertex) {
        activationChanges.add(new ActivationChange(vertex, true));
    }

    public void deactivateVertex(Vertex vertex) {
        activationChanges.add(new ActivationChange(vertex, false));
    }

    private void performVertexActivation(Vertex vertex) {
        if (activeVertices.contains(vertex))
            return;

        Component c = componentAtVertex.get(vertex);

        activeVertices.add(vertex);
        c.activeVertices++;

        // was not active before
        if (c.activeVertices == 1)
            this.planUpdateForElementsAt(c);
    }

    private void performVertexDeactivation(Vertex vertex) {
        if (!activeVertices.contains(vertex))
            return;

        Component c = componentAtVertex.get(vertex);

        activeVertices.remove(vertex);
        c.activeVertices--;

        // stayed active -> feed signal just back to the element
        if (c.isActive())
            this.planUpdateForElementsAt(vertex);
        else // else update the whole component
            this.planUpdateForElementsAt(c);
    }

    public void processVertexActivations() {
        for (ActivationChange change : activationChanges) {
            if (change.activity)
                this.performVertexActivation(change.vertex);
            else
                this.performVertexDeactivation(change.vertex);
        }
        activationChanges.clear();
    }

    /////////////////////////////
    // Element update planning //
    /////////////////////////////

    private void planUpdateForElementsAt(Vertex v) {
        // active vertices are (except in tests) activated by the element
        // itself, so they act as output vertices and don't care about changes there
        if (activeVertices.contains(v))
            return;

        if (v.isBound())
            simulationQueue.planElementUpdate(v.getBoundElement());
    }

    private void planUpdateForElementsAt(Component c) {
        for (Vertex v : c.vertices)
            this.planUpdateForElementsAt(v);
    }

    /////////////////////
    // Gate simulation //
    /////////////////////

    public void simulationTick(double elapsedTime) {
        // if there are some forgotten activations
        this.processVertexActivations();

        simulationQueue.advanceTime(elapsedTime);

        while (simulationQueue.hasItemToExecute()) {
            simulationQueue.executeNext(this);
        }

        // process any changes that occurred during the simulation step
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
        activeVertices.remove(v);
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
            this.planUpdateForElementsAt(j);
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
            this.planUpdateForElementsAt(a);

        if (b.isActive() != originalComponent.isActive())
            this.planUpdateForElementsAt(b);
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
