package cs.jirkamayer.gatefields.scheme;

import java.util.*;

public class SimulationQueue {

    private PriorityQueue<Item> queue = new PriorityQueue<>();

    // maps senders to mappings of event -> event item
    // used to obtain item given a sender and an event
    private Dictionary<Object, Dictionary<SimulationEvent, Item>> mappings = new Hashtable<>();

    private double currentTime = 0.0;
    private long nextId = 0; // keeps order for events added within one tick

    private static class Item implements Comparable<Item> {
        public double time;
        public long id;
        public Object sender;
        public SimulationEvent event;

        public Item(double time, long id, Object sender, SimulationEvent event) {
            this.time = time;
            this.id = id;
            this.sender = sender;
            this.event = event;
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

    private Dictionary<SimulationEvent, Item> getMapping(Object sender) {
        Dictionary<SimulationEvent, Item> mapping = mappings.get(sender);
        if (mapping == null) {
            mapping = new Hashtable<>();
            mappings.put(sender, mapping);
        }
        return mapping;
    }

    private Item getItem(Object sender, SimulationEvent event) {
        return this.getMapping(sender).get(event);
    }

    // remember the item in a mapping
    private void rememberItem(Item item) {
        Dictionary<SimulationEvent, Item> mapping = this.getMapping(item.sender);
        mapping.put(item.event, item);
    }

    // forget item from mapping
    private void forgetItem(Item item) {
        this.getMapping(item.sender).remove(item.event);
    }

    public boolean contains(Object sender, SimulationEvent event) {
        return this.getItem(sender, event) != null;
    }

    public void addOrMove(double delay, Object sender, SimulationEvent event) {
        Item item = this.getItem(sender, event);

        if (item == null) { // add
            item = new Item(currentTime + delay, nextId++, sender, event);
            queue.add(item);
            this.rememberItem(item);
        } else { // move
            queue.remove(item);
            item.time = currentTime + delay;
            item.id = nextId++;
            queue.add(item);
        }
    }

    public void remove(Object sender, SimulationEvent event) {
        Item item = this.getItem(sender, event);

        if (item == null)
            return;

        queue.remove(item);
        this.forgetItem(item);
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

        this.forgetItem(item);

        item.event.execute(sim);
    }
}
