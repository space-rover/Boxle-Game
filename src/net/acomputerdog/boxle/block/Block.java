package net.acomputerdog.boxle.block;

import net.acomputerdog.boxle.math.vec.Vec3i;

/**
 * Represents a Block in the world.
 */
public abstract class Block {
    /**
     * Name of this block.  Used internally to identify it.
     */
    public final String name;

    /**
     * Creates a new Block
     *
     * @param name The name of this block.
     */
    protected Block(String name) {
        if (name == null) throw new IllegalArgumentException("Block name cannot be null!");
        this.name = name;
        Blocks.registerBlock(this);
    }

    /**
     * Gets the name of this block
     *
     * @return return the name of this block
     */
    public final String getName() {
        return name;
    }

    /**
     * Checks if the block can be placed at the specified location
     *
     * @param location The location to be placed at
     * @param data     The data value that the block will be placed as
     * @return Return true if the block can be placed, false otherwise.
     */
    public boolean canPlaceAt(Vec3i location, byte data) {
        return true;
    }

    /**
     * Called whenever the block is updated, such as from a neighboring block change.
     *
     * @param location The location of the block
     * @param data     The data value of the block
     */
    public void onUpdate(Vec3i location, byte data) {

    }

    /**
     * Called if the block has requested to relieve tick updates.
     *
     * @param location The location of the block
     * @param data     The data value of the block
     */
    public void onTick(Vec3i location, byte data) {

    }

    /**
     * Checks if the block can be destroyed
     * @return Return true if the block can be destroyed.
     */
    public boolean canBeDestroyed(byte data) {
        return true;
    }

    /**
     * Gets the resistance to damage of this block.  The value is multiplied to incoming damage.
     *
     * @param data The data value of the block
     * @return Return a float representing the resistance of the block
     */
    public float getResistance(byte data) {
        return 1.0f;
    }

    /**
     * Gets the resistance to an explosion of the block.  The value is multiplied to incoming explosion damage.
     *
     * @param data The data value of the block
     * @return Return a float representing the explosion resistance of the block
     */
    public float getExplosionResistance(byte data) {
        return 1.0f;
    }

    /**
     * Gets the "strength" of a block.  Is used as a base "hit point" value when being damaged.
     *
     * @param data The data value of the block
     * @return Return a float of the strength of the block
     */
    public float getStrength(byte data) {
        return 100f;
    }

    /**
     * Gets the damage factor done to tools breaking this block.  Value is multiplied in to scale damage.
     *
     * @param data The data value of the block
     * @return Return a float of the hardness of this block
     */
    public float getHardness(byte data) {
        return 1f;
    }

    /**
     * Checks if the block can be collided with
     *
     * @return Return true if the block can be collided with, false otherwise.
     */
    public boolean blocksMovement(byte data) {
        return true;
    }

    /**
     * Checks if this block is clear
     *
     * @return Return true if the block is clear
     */
    public boolean isTransparent(byte data) {
        return false;
    }

    /**
     * Gets the light reduction of light passing through this block.
     *
     * @return return a byte of the reduction of light passing through this block
     */
    public byte getLightReduction(byte data) {
        return (byte) 255;
    }

    /**
     * Gets the light level emitted by this block
     *
     * @return Return a byte of the amount of light emitted by this block.
     */
    public byte getLightOutput(byte data) {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Block)) return false;
        Block block = (Block) o;

        return name.equals(block.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "Block{" +
                "name='" + name +
                '}';
    }
}
