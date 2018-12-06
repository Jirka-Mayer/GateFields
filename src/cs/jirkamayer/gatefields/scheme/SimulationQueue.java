package cs.jirkamayer.gatefields.scheme;

import java.util.*;

public class SimulationQueue {

    private PriorityQueue<Item> queue = new PriorityQueue<>();

    // maps elements to queue items
    private Dictionary<Element, Item> itemForElement = new Hashtable<>();

    private double currentTime = 0.0;

    private static class Item implements Comparable<Item> {
        public double time;
        public Element element;

        public Item(double time, Element element) {
            this.time = time;
            this.element = element;
        }

        @Override
        public int compareTo(Item o) {
            return Double.compare(time, o.time);
        }
    }

    public void advanceTime(double delta) {
        currentTime += delta;
    }

    /**
     * Plan a signal update for an element
     */
    public void planElementUpdate(Element element) {
        Item item = itemForElement.get(element);

        if (item == null) { // add
            item = new Item(currentTime + element.getDelay(), element);
            queue.add(item);
            itemForElement.put(element, item);
        } else { // move
            queue.remove(item);
            item.time = currentTime + element.getDelay();
            queue.add(item);
        }
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
