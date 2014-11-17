package net.acomputerdog.boxle.block;

import com.jme3.math.FastMath;

public enum BlockFace {

    FRONT(-1f, 0, 0f, 0, 0, 0),
    BACK(0, 0, -1f, (FastMath.PI), 0, FastMath.PI),
    RIGHT(0, 0, 0, 0, FastMath.PI / 2, 0),
    LEFT(-1f, 0, -1f, 0, -(FastMath.PI / 2), 0),
    TOP(-1, 1f, 0, -(FastMath.PI / 2), 0f, 0f),
    BOTTOM(-1, 0, -1, (FastMath.PI / 2), 0f, 0f);

    public final float xPos;
    public final float yPos;
    public final float zPos;

    public final float xRot;
    public final float yRot;
    public final float zRot;

    BlockFace(float xPos, float yPos, float zPos, float xRot, float yRot, float zRot) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.zPos = zPos;
        this.xRot = xRot;
        this.yRot = yRot;
        this.zRot = zRot;
    }

}
