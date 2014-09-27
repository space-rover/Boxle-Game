package net.acomputerdog.boxle.block.types;

import net.acomputerdog.boxle.block.Block;

/**
 * Abstract block subclass that adds methods and fields for values such as strength,, resitance, light value, etc.
 */
public abstract class BlockConfigurable extends Block {
    private boolean isBreakable = true;
    private float resistance = 1.0f;
    private float explosionResistance = 1.0f;
    private float strength = 100f;
    private float hardness = 1.0f;
    private boolean blocksMovement = true;
    private boolean isTransparent = false;
    private byte lightReduction = (byte) 255;
    private byte lightOutput = 0;

    /**
     * Creates a new Block
     *
     * @param name The name of this block.
     */
    protected BlockConfigurable(String name) {
        super(name);
    }

    @Override
    public boolean isBreakable(byte data) {
        return isBreakable;
    }

    @Override
    public float getResistance(byte data) {
        return resistance;
    }

    @Override
    public float getExplosionResistance(byte data) {
        return explosionResistance;
    }

    @Override
    public float getStrength(byte data) {
        return strength;
    }

    @Override
    public float getHardness(byte data) {
        return hardness;
    }

    @Override
    public boolean blocksMovement(byte data) {
        return blocksMovement;
    }

    @Override
    public boolean isTransparent(byte data) {
        return isTransparent;
    }

    @Override
    public byte getLightReduction(byte data) {
        return lightReduction;
    }

    @Override
    public byte getLightOutput(byte data) {
        return lightOutput;
    }

    public void setBreakable(boolean isBreakable) {
        this.isBreakable = isBreakable;
    }

    public void setResistance(float resistance) {
        this.resistance = resistance;
    }

    public void setExplosionResistance(float explosionResistance) {
        this.explosionResistance = explosionResistance;
    }

    public void setStrength(float strength) {
        this.strength = strength;
    }

    public void setHardness(float hardness) {
        this.hardness = hardness;
    }

    public void setBlocksMovement(boolean blocksMovement) {
        this.blocksMovement = blocksMovement;
    }

    public void setTransparent(boolean isTransparent) {
        this.isTransparent = isTransparent;
    }

    public void setLightReduction(byte lightReduction) {
        this.lightReduction = lightReduction;
    }

    public void setLightOutput(byte lightOutput) {
        this.lightOutput = lightOutput;
    }
}
