package net.acomputerdog.boxle.block.atom.types.util;

import net.acomputerdog.boxle.block.atom.Atom;
import net.acomputerdog.boxle.block.block.Block;
import net.acomputerdog.boxle.block.sim.sim.Sim;
import net.acomputerdog.boxle.block.sim.sim.exec.InvalidStackDataException;
import net.acomputerdog.boxle.block.sim.sim.exec.SimException;
import net.acomputerdog.boxle.block.sim.stack.Stack;
import net.acomputerdog.boxle.block.sim.stack.StackItem;
import net.acomputerdog.boxle.block.util.SimUtils;
import net.acomputerdog.boxle.main.Boxle;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CreateTexAtom extends Atom {
    private final File generatePath;

    public CreateTexAtom(String name) {
        super(null, "UTIL.CREATE_TEXTURE", name);
        generatePath = new File(Boxle.instance().getGameConfig().tempDir, "/tex/");
        if (!(generatePath.isDirectory() || generatePath.mkdirs())) {
            Boxle.instance().LOGGER_MAIN.logError("Could not create temporary textures directory!");
            Boxle.instance().LOGGER_MAIN.logError("Make sure boxle has write access to the current directory, or run boxle from a directory with write access!");
        }
    }

    @Override
    public void execute(Sim sim, Stack stack, Block block) throws SimException {
        SimUtils.verifyStack(sim, this, stack, StackItem.TYPE_INT, StackItem.TYPE_INT, StackItem.TYPE_STRING);
        int ySize = (Integer) stack.pop().getObj();
        int xSize = (Integer) stack.pop().getObj();
        String name = (String) stack.pop().getObj();
        //int[][] image = new int[xSize][ySize]; //x by y image, with ARGB colorspace.
        //int imageDataSize = xSize * ySize * 4;
        String typeInt = StackItem.TYPE_INT;
        /*
        String[] stackItemTypes = new String[imageDataSize];
        for (int index = 0; index < imageDataSize; index++) {
            stackItemTypes[index] = typeInt;
        }
        SimUtils.verifyStack(sim, this, stack, stackItemTypes);
        */
        //int numPixels = 0;
        BufferedImage buf = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_ARGB);
        if (stack.size() < xSize * ySize) {
            throw new InvalidStackDataException(sim, this, "Not enough items on stack to build a " + xSize + " x " + ySize + " image!");
        }
        for (int x = 0; x < xSize; x++) {
            for (int y = 0; y < ySize; y++) {
                StackItem item = stack.pop();
                if (!item.getType().equals(typeInt)) {
                    throw new InvalidStackDataException(sim, this, "Expected " + typeInt + " on stack, instead found " + item.getType());
                }
                buf.setRGB(x, y, (Integer) item.getObj());
            }
        }
        try {
            ImageIO.write(buf, "PNG", new File(generatePath, name));
        } catch (IOException e) {
            throw new SimException(sim, this, "Unable to save generated image!", e);
        }
        //AssetManager assets = Boxle.instance().getAssetManager();
    }
}
