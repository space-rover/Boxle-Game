package net.acomputerdog.boxle.block.atom;

import net.acomputerdog.boxle.block.dynamic.Identifiable;
import net.acomputerdog.boxle.block.sim.program.Instruction;

public abstract class Atom extends Instruction implements Identifiable {
    private final String id;
    private final String name;

    public Atom(String id) {
        this(id, id);
    }

    public Atom(String id, String name) {
        this(id, name, true);
    }

    public Atom(String id, String name, boolean addToRegistry) {
        super(id);
        if (id == null) {
            throw new NullPointerException("id cannot be null!");
        }
        if (name == null) {
            name = "";
        }
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Atom)) return false;

        Atom atom = (Atom) o;
        return id.equals(atom.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return ("".equals(name)) ? id : name;
    }
}
