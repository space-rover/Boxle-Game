package net.acomputerdog.boxle.render.wrapper;

import net.acomputerdog.boxle.render.engine.RenderEngine;
import net.acomputerdog.core.logger.CLogger;

import static org.lwjgl.opengl.ARBShaderObjects.*;
import static org.lwjgl.opengl.GL11.GL_FALSE;

/**
 * Wrapper around a GL shader
 */
public class Shader {
    /**
     * Logger for use in shaders and shader programs
     */
    static final CLogger LOGGER = new CLogger("Shaders", true, true);

    /**
     * The ID of this shader, as returned by the GPU
     */
    private int shaderID = 0;

    /**
     * Set to true when the shader is compiled
     */
    private boolean isCompiled = false;

    /**
     * The render engine that owns this Shader
     */
    private final RenderEngine renderEngine;

    /**
     * The contents of this shader
     */
    private final String shaderContents;

    /**
     * The type of this shader
     */
    private final int shaderType;

    /**
     * Creates a new shader
     *
     * @param renderEngine   The render engine of this shader
     * @param shaderContents The source code of this shader
     * @param shaderType     The type of this shader
     */
    public Shader(RenderEngine renderEngine, String shaderContents, int shaderType) {
        if (renderEngine == null) {
            throw new IllegalArgumentException("Render engine cannot be null!");
        }
        if (shaderContents == null) {
            throw new IllegalArgumentException("Shader contents cannot be null!");
        }
        this.renderEngine = renderEngine;
        this.shaderContents = shaderContents;
        this.shaderType = shaderType;
    }

    /**
     * Compiles the shader, so that it can be added to a shader program
     */
    public void compile() {
        if (isCompiled) {
            throw new IllegalStateException("Cannot compile an already compiled shader!");
        }
        try {
            shaderID = glCreateShaderObjectARB(shaderType);
            glShaderSourceARB(shaderID, shaderContents);
            glCompileShaderARB(shaderID);
            if (glGetObjectParameteriARB(shaderID, GL_OBJECT_COMPILE_STATUS_ARB) == GL_FALSE) {
                throw new IllegalArgumentException("Could not compile shader!");
            }
            glDeleteObjectARB(shaderID);
            isCompiled = true;
        } catch (Exception e) {
            LOGGER.logFatal("Exception creating shader!", e);
            LOGGER.logFatal("Shader log contents: " + getLogInfo());
            renderEngine.getBoxle().stop();
        }
    }

    /**
     * Gets the log info from any recent GL Shader errors
     * @return Return the log info from any recent shader errors
     */
    private String getLogInfo() {
        return glGetInfoLogARB(shaderID, glGetObjectParameteriARB(shaderID, GL_OBJECT_INFO_LOG_LENGTH_ARB));
    }

    /**
     * Gets the ID of this shader
     * @return Return the ID of this shader
     */
    public int getShaderID() {
        return shaderID;
    }

    /**
     * Gets the render engine of this shader
     * @return Return the render engine of this shader
     */
    public RenderEngine getRenderEngine() {
        return renderEngine;
    }

    /**
     * Gets the shader type
     * @return Return the type of this shader
     */
    public int getShaderType() {
        return shaderType;
    }

    /**
     * Checks if the shader has been compiled
     * @return Return true if the shader has been compiled, false otherwise
     */
    public boolean isCompiled() {
        return isCompiled;
    }
}
