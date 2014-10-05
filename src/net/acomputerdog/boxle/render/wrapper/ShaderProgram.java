package net.acomputerdog.boxle.render.wrapper;

import net.acomputerdog.boxle.render.engine.RenderEngine;

import java.util.HashSet;
import java.util.Set;

import static org.lwjgl.opengl.ARBShaderObjects.*;

/**
 * A wrapper for a GL Shader Program
 */
public class ShaderProgram {
    /**
     * The ID of the "null" program.  Used to disable the active shader.
     */
    private static final int PROGRAM_NULL = 0;

    /**
     * The render engine that owns this ShaderProgram
     */
    private final RenderEngine engine;

    /**
     * The set of attached shaders.
     */
    private final Set<Shader> attachedShaders = new HashSet<>();

    /**
     * True if the shader program has been linked
     */
    private boolean isLinked = false;

    /**
     * The ID of this program, as returned by the GPU
     */
    private int programID = 0;

    /**
     * Creates a new shader program
     *
     * @param engine The render engine of the shader
     */
    public ShaderProgram(RenderEngine engine) {
        if (engine == null) {
            throw new IllegalArgumentException("Render engine cannot be null!");
        }
        this.engine = engine;
    }

    /**
     * Adds a shader to this ShaderProgram
     * @param shader The shader to add. Must be already compiled and use the same render engine.
     */
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

    /**
     * Links this Shader Program.
     */
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

    /**
     * Activates this shader program on successive render actions.
     */
    public void use() {
        glUseProgramObjectARB(programID);
    }

    /**
     * Deactivates the shader program
     */
    public void release() {
        glUseProgramObjectARB(PROGRAM_NULL);
    }

    /**
     * Gets the log info from any GL errors
     * @return Return the log info from any GL errors
     */
    private String getLogInfo() {
        return glGetInfoLogARB(programID, glGetObjectParameteriARB(programID, GL_OBJECT_INFO_LOG_LENGTH_ARB));
    }

    /**
     * Gets the render engine of this shader program
     * @return Return the render engine of this shader program
     */
    public RenderEngine getEngine() {
        return engine;
    }

    /**
     * Checks if the shader program has been linked
     * @return Return true if the shader program has been linked, false otherwise
     */
    public boolean isLinked() {
        return isLinked;
    }

    /**
     * Gets the ID of this shader program
     * @return Return the ID of this shader program
     */
    public int getProgramID() {
        return programID;
    }
}
