package cs.jirkamayer.gatefields.scheme;

import java.util.*;

public class SimulationQueue {

    private PriorityQueue<Item> queue = new PriorityQueue<>();

    // maps elements to queue items
    private Dictionary<Element, Item> itemForElement = new Hashtable<>();

    private double currentTime = 0.0;
    private long nextId = 0;

    private static class Item implements Comparable<Item> {
        public double time;
        public long id;
        public Element element;

        public Item(double time, long id, Element element) {
            this.time = time;
            this.id = id;
            this.element = element;
        }

        @Override
        public int compareTo(Item o) {
            int c = Double.compare(time, o.time);

            if (c != 0)
                return c;

            return Long.compare(id, o.id);
        }
    }

    public void advanceTime(double delta) {
        currentTime += delta;
    }

    public void clear() {
        queue.clear();
        itemForElement = new Hashtable<>();
        currentTime = 0.0;
    }

    /**
     * Plan a signal update for an element
     */
    public void planElementUpdate(Element element) {
        Item item = itemForElement.get(element);

        if (item == null) { // add
            item = new Item(currentTime + element.getDelay(), nextId++, element);
            queue.add(item);
            itemForElement.put(element, item);
        } else { // move
            queue.remove(item);
            item.time = currentTime + element.getDelay();
            queue.add(item);
        }
    }

    public void remove(Element e) {
        Item item = itemForElement.get(e);

        if (item == null)
            return;

        itemForElement.remove(e);
        queue.remove(item);
    }

    public boolean hasItemToExecute() {
        Item i = queue.peek();

        if (i == null)
            return false;

        return i.time < currentTime;
    }

    public void executeNext(Simulator sim) {
        Item item = queue.poll();

        if (item == null)
            return;

        itemForElement.remove(item.element);

        item.element.updateSignals(sim);
    }
}
