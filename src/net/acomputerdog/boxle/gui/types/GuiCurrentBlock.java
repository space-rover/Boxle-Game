package net.acomputerdog.boxle.gui.types;

import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.texture.Texture2D;
import com.jme3.ui.Picture;
import net.acomputerdog.boxle.block.block.Block;
import net.acomputerdog.boxle.config.GameConfig;
import net.acomputerdog.boxle.gui.Gui;
import net.acomputerdog.boxle.main.Boxle;

public class GuiCurrentBlock extends Gui {
    private final Picture picture;
    private final BitmapText text;

    public GuiCurrentBlock() {
        Boxle boxle = Boxle.instance();
        GameConfig config = boxle.getGameConfig();
        picture = new Picture("Current Block");
        picture.setPosition(config.screenWidth - 128 - 25, 25);
        picture.setWidth(128f);
        picture.setHeight(128f);
        text = new BitmapText(boxle.getFont());
        text.setColor(ColorRGBA.LightGray);
        text.setLocalTranslation(config.screenWidth - 128 - 25 + 2, 25 + (int) text.getLineHeight(), 0);
        setBlock(boxle.getRenderEngine().getInputHandler().getCurrentBlock());
    }

    @Override
    public void render(Node node) {
        node.attachChild(picture);
        node.attachChild(text);
    }

    public void setBlock(Block block) {
        Material mat = block.getTextures().getFrontMat();
        Texture2D tex = (Texture2D) block.getTextures().getFrontTex();
        picture.setTexture(Boxle.instance().getAssetManager(), tex, mat.isTransparent());
        text.setText(block.getName());
    }
}
