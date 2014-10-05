package net.acomputerdog.boxle.render.wrapper;

import net.acomputerdog.boxle.render.engine.RenderEngine;

import java.util.HashSet;
import java.util.Set;

import static org.lwjgl.opengl.ARBShaderObjects.*;

public class ShaderProgram {
    private static final int PROGRAM_NULL = 0;

    private final RenderEngine engine;
    private final Set<Shader> attachedShaders = new HashSet<>();

    private boolean isLinked = false;
    private int programID = 0;

    public ShaderProgram(RenderEngine engine) {
        if (engine == null) {
            throw new IllegalArgumentException("Render engine cannot be null!");
        }
        this.engine = engine;
    }

    public void addShader(Shader shader) {
        if (isLinked) {
            throw new IllegalStateException("Cannot add a shader to a linked program!");
        }
        if (shader == null) {
            throw new IllegalArgumentException("Cannot add a null shader!");
        }
        if (shader.getRenderEngine() != engine) {
            throw new IllegalArgumentException("Cannot add a shader with a different render engine!");
        }
        if (!shader.isCompiled()) {
            throw new IllegalArgumentException("Cannot add an un-compiled shader!");
        }
        attachedShaders.add(shader);
    }

    public void link() {
        if (isLinked) {
            throw new IllegalStateException("Program is already linked!");
        }
        if (attachedShaders.size() == 0) {
            throw new IllegalStateException("Cannot link a shader program with no shaders!");
        }
        try {
            programID = glCreateProgramObjectARB();
            for (Shader shader : attachedShaders) {
                glAttachObjectARB(programID, shader.getShaderID());
            }
            glLinkProgramARB(programID);
            glValidateProgramARB(programID);
            isLinked = true;
        } catch (Exception e) {
            Shader.LOGGER.logFatal("Exception creating shader program!", e);
            Shader.LOGGER.logFatal("Shader log contents: " + getLogInfo());
            engine.getBoxle().stop();
        }
    }

    public void use() {
        glUseProgramObjectARB(programID);
    }

    public void release() {
        glUseProgramObjectARB(PROGRAM_NULL);
    }

    private String getLogInfo() {
        return glGetInfoLogARB(programID, glGetObjectParameteriARB(programID, GL_OBJECT_INFO_LOG_LENGTH_ARB));
    }

    public RenderEngine getEngine() {
        return engine;
    }

    public boolean isLinked() {
        return isLinked;
    }

    public int getProgramID() {
        return programID;
    }
}
