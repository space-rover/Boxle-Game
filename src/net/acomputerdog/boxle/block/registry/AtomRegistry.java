package net.acomputerdog.boxle.block.registry;

import net.acomputerdog.boxle.block.atom.Atom;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class AtomRegistry {
    private final Map<String, Atom> atomRegistry;

    public AtomRegistry() {
        atomRegistry = new LinkedHashMap<String, Atom>();
    }

    public void registerAtom(Atom atom) {
        registerAtom(atom.getId(), atom);
    }

    public void registerAtom(String id, Atom atom) {
        atomRegistry.put(id, atom);
    }

    public boolean isAtomDefined(String id) {
        return atomRegistry.containsKey(id);
    }

    public boolean isAtomDefined(Atom atom) {
        return atomRegistry.containsValue(atom);
    }

    public Atom getAtom(String id) {
        return atomRegistry.get(id);
    }

    public Collection<Atom> getAtoms() {
        return atomRegistry.values();
    }

    public Set<String> getIDs() {
        return atomRegistry.keySet();
    }
}

