package net.acomputerdog.boxle.block.atom;

import net.acomputerdog.boxle.block.sim.program.instruction.Instruction;
import net.acomputerdog.core.identity.Identifiable;
import net.acomputerdog.core.logger.CLogger;

public abstract class Atom extends Instruction implements Identifiable {
    public static final CLogger LOGGER = new CLogger("Atom", false, true);

    private final String name;
    private final String def;

    public Atom(String def, String id, String name) {
        super(id);
        if (id == null) {
            throw new NullPointerException("ID cannot be null!");
        }
        if (name == null) {
            name = id;
        }
        if (def == null) {
            def = id;
        }
        this.def = def;
        this.name = name;
    }

    public String getDefinition() {
        return def;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Atom)) return false;

        Atom atom = (Atom) o;
        return getId().equals(atom.getId());

    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public String toString() {
        return name;
    }
}
