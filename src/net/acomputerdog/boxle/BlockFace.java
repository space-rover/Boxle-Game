package net.acomputerdog.boxle;

public enum BlockFace {
    FRONT(0, 0, .5f, 0, 0, 90f),
    BACK(0, 0, -.5f, 0, 0, 90f),
    LEFT(.5f, 0, 0, 90f, 0, 0),
    RIGHT(-.5f, 0, 0, 90f, 0, 0),
    TOP(0, .5f, 0, 0, 90f, 0),
    BOTTOM(0, -.5f, 0, 0, 90f, 0);

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
