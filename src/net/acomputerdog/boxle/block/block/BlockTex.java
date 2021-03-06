package net.acomputerdog.boxle.block.block;

import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.texture.Texture;
import net.acomputerdog.boxle.main.Boxle;

public class BlockTex {
    private static final Boxle boxle = Boxle.instance();

    private static String textureFieldName = Boxle.instance().getGameConfig().lightingMode >= 1 ? "DiffuseMap" : "ColorMap";
    private static String jm3dName = Boxle.instance().getGameConfig().lightingMode >= 1 ? "Common/MatDefs/Light/Lighting.j3md" : "Common/MatDefs/Misc/Unshaded.j3md";

    private final Block block;

    private Texture frontTex;
    private Texture backTex;
    private Texture leftTex;
    private Texture rightTex;
    private Texture topTex;
    private Texture bottomTex;

    private Material frontMat;
    private Material backMat;
    private Material leftMat;
    private Material rightMat;
    private Material topMat;
    private Material bottomMat;

    public BlockTex(Block block) {
        this.block = block;
        frontMat = createMaterial("front");
        backMat = createMaterial("back");
        leftMat = createMaterial("left");
        rightMat = createMaterial("right");
        topMat = createMaterial("top");
        bottomMat = createMaterial("bottom");
    }

    public Material getFrontMat() {
        return frontMat;
    }

    public Material getBackMat() {
        return backMat;
    }

    public Material getLeftMat() {
        return leftMat;
    }

    public Material getRightMat() {
        return rightMat;
    }

    public Material getTopMat() {
        return topMat;
    }

    public Material getBottomMat() {
        return bottomMat;
    }

    public Material getFaceMat(BlockFace face) {
        switch (face) {
            case LEFT:
                return getLeftMat();
            case RIGHT:
                return getRightMat();
            case FRONT:
                return getFrontMat();
            case BACK:
                return getBackMat();
            case TOP:
                return getTopMat();
            case BOTTOM:
                return getBottomMat();
            default:
                throw new IllegalArgumentException("Impossible BlockFace!");
        }
    }

    public void setFrontMat(Material mat) {
        frontMat = mat;
    }

    public void setBackMat(Material mat) {
        backMat = mat;
    }

    public void setLeftMat(Material mat) {
        leftMat = mat;
    }

    public void setRightMat(Material mat) {
        rightMat = mat;
    }

    public void setTopMat(Material mat) {
        topMat = mat;
    }

    public void setBottomMat(Material mat) {
        bottomMat = mat;
    }

    public void setAllMat(Material mat) {
        frontMat = backMat = leftMat = rightMat = topMat = backMat = mat;
    }

    public Texture getFrontTex() {
        return frontTex;
    }

    public Texture getBackTex() {
        return backTex;
    }

    public Texture getLeftTex() {
        return leftTex;
    }

    public Texture getRightTex() {
        return rightTex;
    }

    public Texture getTopTex() {
        return topTex;
    }

    public Texture getBottomTex() {
        return bottomTex;
    }

    public void setFrontTex(Texture tex) {
        frontTex = tex;
        frontMat.setTexture(textureFieldName, frontTex);
    }

    public void setBackTex(Texture tex) {
        backTex = tex;
        backMat.setTexture(textureFieldName, backTex);
    }

    public void setLeftTex(Texture tex) {
        leftTex = tex;
        leftMat.setTexture(textureFieldName, leftTex);
    }

    public void setRightTex(Texture tex) {
        rightTex = tex;
        rightMat.setTexture(textureFieldName, rightTex);
    }

    public void setTopTex(Texture tex) {
        topTex = tex;
        topMat.setTexture(textureFieldName, topTex);
    }

    public void setBottomTex(Texture tex) {
        bottomTex = tex;
        bottomMat.setTexture(textureFieldName, bottomTex);
    }

    public void setAllTex(Texture tex) {
        frontTex = backTex = leftTex = rightTex = topTex = bottomTex = tex;
        frontMat.setTexture(textureFieldName, frontTex);
        backMat.setTexture(textureFieldName, backTex);
        leftMat.setTexture(textureFieldName, leftTex);
        rightMat.setTexture(textureFieldName, rightTex);
        topMat.setTexture(textureFieldName, topTex);
        bottomMat.setTexture(textureFieldName, bottomTex);
    }


    public void loadFrontTex(String path) {
        Texture tex = boxle.getAssetManager().loadTexture(path);
        tex.setMagFilter(Texture.MagFilter.Nearest);
        setFrontTex(tex);
    }

    public void loadBackTex(String path) {
        Texture tex = boxle.getAssetManager().loadTexture(path);
        tex.setMagFilter(Texture.MagFilter.Nearest);
        setBackTex(tex);
    }

    public void loadLeftTex(String path) {
        Texture tex = boxle.getAssetManager().loadTexture(path);
        tex.setMagFilter(Texture.MagFilter.Nearest);
        setLeftTex(tex);
    }

    public void loadRightTex(String path) {
        Texture tex = boxle.getAssetManager().loadTexture(path);
        tex.setMagFilter(Texture.MagFilter.Nearest);
        setRightTex(tex);
    }

    public void loadTopTex(String path) {
        Texture tex = boxle.getAssetManager().loadTexture(path);
        tex.setMagFilter(Texture.MagFilter.Nearest);
        setTopTex(tex);
    }

    public void loadBottomTex(String path) {
        Texture tex = boxle.getAssetManager().loadTexture(path);
        tex.setMagFilter(Texture.MagFilter.Nearest);
        setBottomTex(tex);
    }

    public void loadAllTex(String path) {
        Texture tex = boxle.getAssetManager().loadTexture(path);
        tex.setMagFilter(Texture.MagFilter.Nearest);
        setAllTex(tex);
    }

    public void loadAllDefault() {
        loadAllTex(getDefaultTexPath());
    }

    public String getDefaultTexPath() {
        return "/tex/block/".concat(block.getId()).concat(".png");
    }

    private Material createMaterial(String side) {
        Material mat = new Material(boxle.getAssetManager(), jm3dName);
        mat.setName("block_" + block.getName() + "_" + side);
        mat.setTransparent(block.isTransparent()); //Does not actually do anything, but is used as a marker
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        mat.getAdditionalRenderState().setAlphaTest(true);
        mat.getAdditionalRenderState().setAlphaFallOff(.9f);
        return mat;
    }

    public static void setTextureFieldName(String textureFieldName) {
        BlockTex.textureFieldName = textureFieldName;
    }

    public static void setJm3dName(String jm3dName) {
        BlockTex.jm3dName = jm3dName;
    }
}
