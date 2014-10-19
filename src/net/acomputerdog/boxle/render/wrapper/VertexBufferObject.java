package net.acomputerdog.boxle.render.wrapper;

import net.acomputerdog.boxle.render.engine.RenderEngine;
import net.acomputerdog.core.logger.CLogger;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class VertexBufferObject {
    private static CLogger logger = new CLogger("VBO", true, true);

    private final RenderEngine engine;

    private int vboId = 0;
    private int vaID = 0;

    private float[] vertexData = new float[0];
    private float[] textureData = new float[0];
    private float[] normalData = new float[0];

    public VertexBufferObject(RenderEngine engine) {
        this.engine = engine;
    }

    //Not at all complete!
    public void init() {
        try {
            vaID = glGenVertexArrays();
            engine.checkGLErrors();

            glBindVertexArray(vaID);
            engine.checkGLErrors();

            vboId = glGenBuffers();
            engine.checkGLErrors();

            FloatBuffer vertDat = BufferUtils.createFloatBuffer(vertexData.length);
            vertDat.put(this.vertexData);
            vertDat.rewind();

            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            engine.checkGLErrors();

            glBufferData(GL_ARRAY_BUFFER, vertDat, GL_DYNAMIC_DRAW);
            engine.checkGLErrors();
        } catch (Exception e) {
            Shader.LOGGER.logFatal("Exception initializing VBO!", e);
            engine.getBoxle().stop();
        }
    }

    public void render() {

    }

    public void setVertexData(float[] vertexData) {
        this.vertexData = vertexData;
    }

    public void setTextureData(float[] textureData) {
        this.textureData = textureData;
    }

    public void setNormalData(float[] normalData) {
        this.normalData = normalData;
    }

    public void cleanup() {

    }
}
