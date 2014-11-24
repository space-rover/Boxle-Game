package net.acomputerdog.boxle.math.spiral;

import net.acomputerdog.boxle.math.vec.Vec2i;
import net.acomputerdog.boxle.math.vec.VecPool;

public class Spiral2i {
    private final Vec2i currLoc;
    private final Vec2i center;

    private int direction = 0; //0 is right, 1 is up, 2 is left, 3 is down
    private int ring = 0;
    private boolean initialVal = true;

    public Spiral2i(Vec2i center) {
        this.center = center;
        this.currLoc = center.duplicate();
    }

    public Spiral2i() {
        this(VecPool.getVec2i(0, 0));
    }

    public Vec2i next() {
        return next(VecPool.getVec2i());
    }

    public Vec2i next(Vec2i temp) {
        if (!initialVal) {
            if (direction == 0) {
                if (currLoc.x > center.x + ring) {
                    direction = 1;
                    ring++;
                    currLoc.y++;
                } else {
                    currLoc.x++;
                }
            } else if (direction == 1) {
                if (currLoc.y == center.y + ring) {
                    direction = 2;
                    currLoc.x--;
                } else {
                    currLoc.y++;
                }
            } else if (direction == 2) {
                if (currLoc.x == center.y - ring) {
                    direction = 3;
                    currLoc.y--;
                } else {
                    currLoc.x--;
                }
            } else {
                if (currLoc.y == center.y - ring) {
                    direction = 0;
                    currLoc.x++;
                } else {
                    currLoc.y--;
                }
            }
        } else {
            initialVal = false;
        }
        temp.x = currLoc.x;
        temp.y = currLoc.y;
        return temp;
    }

    public static void main(String[] args) {
        Spiral2i spiral = new Spiral2i();
        int count = 0;
        while (++count <= 20) {
            System.out.println(spiral.next().asCoords());
        }
    }

}
