package net.acomputerdog.boxle.world.gen.structures;

import net.acomputerdog.boxle.block.util.Registry;
import net.acomputerdog.boxle.world.gen.structures.types.StructureTree;

public class Structures {
    public static final Registry<Structure> STRUCTURES = new Registry<>();

    public static StructureTree tree = STRUCTURES.register(new StructureTree());
}
