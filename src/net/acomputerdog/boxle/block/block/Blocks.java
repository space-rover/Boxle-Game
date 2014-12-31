package net.acomputerdog.boxle.block.block;

import net.acomputerdog.boxle.block.sim.loader.PropLoader;
import net.acomputerdog.boxle.block.sim.sim.Sim;
import net.acomputerdog.core.storage.Registry;

public class Blocks {
    public static final Registry<Block> BLOCKS = new Registry<>();

    public static final Block air = createAirBlock();

    public static final Block steel = loadInternalProp("steel");
    public static final Block grassySteel = loadInternalProp("grassy_steel");

    public static final Block dirt = loadInternalProp("dirt");
    public static final Block grass = loadInternalProp("grass");
    public static final Block stone = loadInternalProp("stone");

    public static final Block wood = loadInternalProp("wood");
    public static final Block leaves = loadInternalProp("leaves");

    public static final Block acomputerdog = loadInternalProp("acomputerdog");
    public static final Block seamusFD = loadInternalProp("seamusfd");

    public static void initBlockTextures() {
        for (Block block : BLOCKS.getItems()) {
            block.getTextures(); //initialize textures
        }
    }

    private static Block loadInternalProp(String name) {
        Block block = PropLoader.loadAndCreateBlock(name, Blocks.class.getResourceAsStream("/prop/block/" + name + ".prop"));
        BLOCKS.register(block);
        Sim.LOGGER.logDetail("Loaded block from internal prop: " + name);
        return block;
    }

    private static Block createAirBlock() {
        Block block = new Block("air", "Air");
        block.setBreakable(false);
        block.setResistance(0f);
        block.setExplosionResistance(0f);
        block.setStrength(9999f);
        block.setRenderable(false);
        block.setCollidable(false);
        block.setTransparent(true);
        block.setLightReduction((byte) 0);
        BLOCKS.register(block);
        return block;
    }

}
