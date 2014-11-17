package net.acomputerdog.boxle.block.sim.stack;

import net.acomputerdog.boxle.block.sim.sim.Sim;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.LinkedBlockingDeque;

public class Stack {

    private final Deque<StackItem> stackItems;
    private int stackSize = 0;
    private final Sim sim;

    private Stack(Sim sim, Deque<StackItem> stackItems) {
        this.sim = sim;
        this.stackItems = stackItems;
    }

    public Stack(Sim sim) {
        this(sim, new LinkedBlockingDeque<StackItem>());
    }

    public Sim getSim() {
        return sim;
    }

    public int size() {
        return stackSize - 1;
    }

    public boolean isEmpty() {
        return stackSize > 1;
    }

    public void push(Object obj, String typeID) {
        this.push(new StackItem(obj, typeID));
    }

    public void push(StackItem item) {
        stackItems.push(item);
        stackSize++;
    }

    public StackItem peek() {
        return stackItems.peek();
    }

    public StackItem pop() {
        if (stackSize == 0) {
            return null;
        } else {
            stackSize--;
            return stackItems.pop();
        }
    }

    public Stack copy() {
        Deque<StackItem> items = new ArrayDeque<StackItem>();
        items.addAll(this.stackItems);
        return new Stack(this.sim, items);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Stack Contents:");
        Stack copy = this.copy();
        StackItem item;
        while (!copy.stackItems.isEmpty()) {
            builder.append("\n ");
            builder.append(copy.stackItems.pop());
        }
        return builder.toString();
    }
}
