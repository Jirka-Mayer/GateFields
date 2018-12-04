package cs.jirkamayer.gatefields.scheme;

import java.util.*;

public class Simulator {
    private Scheme scheme;

    private Set<Vertex> activeVertices = new HashSet<>();
    private Dictionary<Vertex, Component> componentAtVertex = new Hashtable<>();

    private static class Component {
        public Set<Vertex> vertices = new HashSet<>();
        public int activeVertices = 0;

        public boolean isActive() {
            return activeVertices > 0;
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
        return this.hasSignal(w.start) && this.hasSignal(w.end);
    }

    public void activateVertex(Vertex v) {
        activeVertices.add(v);
        componentAtVertex.get(v).activeVertices++;
    }

    public void deactivateVertex(Vertex v) {
        activeVertices.remove(v);
        componentAtVertex.get(v).activeVertices--;
    }

    ////////////////////
    // Scheme changes //
    ////////////////////

    public void vertexAdded(Vertex v) {
        Component c = new Component();
        c.vertices.add(v);

        componentAtVertex.put(v, c);
    }

    public void wireAdded(Wire w) {
        Component a = componentAtVertex.get(w.start);
        Component b = componentAtVertex.get(w.end);

        if (a != b)
            this.joinComponents(a, b);
    }

    public void wireRemoved(Wire w) {
        Set<Vertex> startVertices = this.constructComponent(w.start);

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
    }

    private void joinComponents(Component a, Component b) {
        Component j = new Component();

        j.vertices.addAll(a.vertices);
        j.vertices.addAll(b.vertices);
        j.activeVertices = a.activeVertices + b.activeVertices;

        for (Vertex v : a.vertices)
            componentAtVertex.put(v, j);

        for (Vertex v : b.vertices)
            componentAtVertex.put(v, j);
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
