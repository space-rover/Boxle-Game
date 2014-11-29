package net.acomputerdog.boxle.world.gen.structures;

import net.acomputerdog.boxle.world.gen.structures.types.StructureTree;
import net.acomputerdog.core.storage.Registry;

public class Structures {
    public static final Registry<Structure> STRUCTURES = new Registry<>();

    public static StructureTree tree = STRUCTURES.register(new StructureTree());
}
