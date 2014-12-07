package net.acomputerdog.boxle.gui.types;

import com.jme3.scene.Node;
import com.jme3.ui.Picture;
import net.acomputerdog.boxle.config.GameConfig;
import net.acomputerdog.boxle.gui.Gui;
import net.acomputerdog.boxle.main.Boxle;

public class GuiCrossHairs extends Gui {

    private final Picture crossHairs;

    public GuiCrossHairs() {
        Boxle boxle = Boxle.instance();
        GameConfig config = boxle.getGameConfig();
        crossHairs = new Picture("CrossHairs");
        crossHairs.setImage(boxle.getAssetManager(), "tex/ui/crosshairs.png", true);
        crossHairs.setWidth(10);
        crossHairs.setHeight(10);
        crossHairs.setPosition(config.screenWidth / 2, config.screenHeight / 2);
    }


    @Override
    public void render(Node node) {
        node.attachChild(crossHairs);
    }
}
