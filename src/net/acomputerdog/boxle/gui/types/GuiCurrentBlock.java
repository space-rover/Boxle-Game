package net.acomputerdog.boxle.gui.types;

import com.jme3.material.Material;
import com.jme3.scene.Node;
import com.jme3.texture.Texture2D;
import com.jme3.ui.Picture;
import net.acomputerdog.boxle.block.block.Block;
import net.acomputerdog.boxle.config.GameConfig;
import net.acomputerdog.boxle.gui.Gui;
import net.acomputerdog.boxle.main.Boxle;

public class GuiCurrentBlock extends Gui {
    private final Picture picture;

    public GuiCurrentBlock() {
        Boxle boxle = Boxle.instance();
        GameConfig config = boxle.getGameConfig();
        picture = new Picture("Current Block");
        setBlock(boxle.getRenderEngine().getInputHandler().getCurrentBlock());
        picture.setPosition(config.screenWidth - 128 - 25, 25);
        picture.setWidth(128f);
        picture.setHeight(128f);
    }

    @Override
    public void render(Node node) {
        node.attachChild(picture);
    }

    public void setBlock(Block block) {
        Material mat = block.getTextures().getFrontMat();
        Texture2D tex = (Texture2D) block.getTextures().getFrontTex();
        picture.setTexture(Boxle.instance().getAssetManager(), tex, mat.isTransparent());
    }
}
