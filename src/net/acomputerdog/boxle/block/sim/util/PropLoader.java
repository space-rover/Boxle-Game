package net.acomputerdog.boxle.block.sim.util;

import net.acomputerdog.boxle.block.block.Block;
import net.acomputerdog.boxle.block.util.BlockTex;
import net.acomputerdog.boxle.main.Boxle;
import net.acomputerdog.boxle.math.aabb.AABBF;
import net.acomputerdog.core.java.Patterns;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropLoader {
    private static final Map<String, Properties> propertyMap = new HashMap<>();

    public static Block loadAndCreateBlock(String name, InputStream in) {
        try {
            return createBlock(loadProp(name, in));
        } catch (Exception e) {
            System.err.println("Exception loading block: " + name);
            e.printStackTrace();
            throw new RuntimeException("Exception loading block", e);
        }
    }

    public static Properties loadProp(String name, InputStream in) {
        Properties prop = propertyMap.get(name);
        if (prop == null) {
            prop = new Properties();
            try {
                prop.load(in);
                prop.setProperty("prop_name", name);
            } catch (IOException e) {
                throw new RuntimeException("Exception reading property file!");
            }
            propertyMap.put(name, prop);
            Boxle.instance().LOGGER_MAIN.logDetail("Loaded block from prop: " + name);
        }
        return prop;
    }

    public static Block createBlock(Properties prop) {
        //TODO possibly replace with generated StackSim script
        String parent = prop.getProperty("parent");
        Block block;
        if (parent != null) {
            Properties parentProp = new Properties(loadProp(parent, PropLoader.class.getResourceAsStream(parent)));
            if (!parentProp.containsKey("id")) parentProp.setProperty("id", prop.getProperty("id"));
            if (!parentProp.containsKey("name")) parentProp.setProperty("name", prop.getProperty("name"));
            block = createBlock(parentProp);
        } else {
            block = new Block(prop.getProperty("id"), prop.getProperty("name"));
        }
        //TODO add helper methods to parse bools, ints, etc
        if (prop.containsKey("breakable")) block.setBreakable(Boolean.parseBoolean(prop.getProperty("breakable")));
        if (prop.containsKey("collidable")) block.setCollidable(Boolean.parseBoolean(prop.getProperty("collidable")));
        if (prop.containsKey("transparent")) block.setTransparent(Boolean.parseBoolean(prop.getProperty("transparent")));
        if (prop.containsKey("renderable")) block.setRenderable(Boolean.parseBoolean(prop.getProperty("renderable")));
        if (prop.containsKey("light_reduction")) block.setLightReduction(Byte.parseByte(prop.getProperty("light_reduction")));
        if (prop.containsKey("light_output")) block.setLightOutput(Byte.parseByte(prop.getProperty("light_output")));
        if (prop.containsKey("resistance")) block.setResistance(Float.parseFloat(prop.getProperty("resistance")));
        if (prop.containsKey("explosion_resistance")) block.setResistance(Float.parseFloat(prop.getProperty("explosion_resistance")));
        if (prop.containsKey("strength")) block.setResistance(Float.parseFloat(prop.getProperty("strength")));
        if (prop.containsKey("hardness")) block.setResistance(Float.parseFloat(prop.getProperty("hardness")));
        if (prop.containsKey("bounds")) {
            String[] boundStrings = prop.getProperty("bounds").split(Patterns.COMMA);
            if (boundStrings.length < 6) {
                Boxle.instance().LOGGER_MAIN.logWarning("Improperly formatted bounds: \"" + prop.getProperty("bounds") + "\".  Should be 6 comma-separated float values.");
            } else {
                float[] bounds = new float[6];
                for (int index = 0; index < 6; index++) {
                    bounds[index] = Float.parseFloat(boundStrings[index]);
                }
                block.setBounds(new AABBF(bounds));
            }
        }
        if (prop.containsKey("tex")) {
            String[] texPaths = prop.getProperty("tex").split(Patterns.COMMA);
            if (texPaths.length < 6) {
                Boxle.instance().LOGGER_MAIN.logWarning("Improperly formatted texture paths: \"" + prop.getProperty("tex") + "\".  Should be 6 comma-separated strings.");
            } else {
                BlockTex tex = new BlockTex(block);
                //TODO don't reload existing textures
                tex.loadTopTex(texPaths[0]);
                tex.loadFrontTex(texPaths[1]);
                tex.loadBackTex(texPaths[2]);
                tex.loadLeftTex(texPaths[3]);
                tex.loadRightTex(texPaths[4]);
                tex.loadBottomTex(texPaths[5]);
                block.setTextures(tex);
            }
        }
        return block;
    }
}
