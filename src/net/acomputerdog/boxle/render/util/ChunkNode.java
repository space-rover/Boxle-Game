package net.acomputerdog.boxle.render.util;

import com.jme3.scene.Node;

public class ChunkNode extends Node implements Comparable<Node> {

    public ChunkNode(String name) {
        super(name);
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
        //return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof ChunkNode)) return false;
        return this.name.equals(((ChunkNode) obj).getName());
        //return this == obj;
    }

    @Override
    public int compareTo(Node o) {
        return this.hashCode() - o.hashCode();
    }
}
