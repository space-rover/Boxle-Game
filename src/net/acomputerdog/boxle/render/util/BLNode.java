package net.acomputerdog.boxle.render.util;

import com.jme3.scene.Node;

public class BLNode extends Node implements Comparable<Node> {

    public BLNode(String name) {
        super(name);
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof BLNode)) return false;
        return this.name.equals(((BLNode) obj).getName());
    }

    @Override
    public int compareTo(Node o) {
        return this.hashCode() - o.hashCode();
    }
}
