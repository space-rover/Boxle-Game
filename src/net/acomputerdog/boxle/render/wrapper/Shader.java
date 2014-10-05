package net.acomputerdog.boxle.render.wrapper;

import net.acomputerdog.boxle.render.engine.RenderEngine;
import net.acomputerdog.core.logger.CLogger;

import static org.lwjgl.opengl.ARBShaderObjects.*;
import static org.lwjgl.opengl.GL11.GL_FALSE;

public class Shader {
    static final CLogger LOGGER = new CLogger("Shaders", true, true);

    private int shaderID = 0;
    private boolean isCompiled = false;

    private final RenderEngine renderEngine;
    private final String shaderContents;
    private final int shaderType;

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

    private String getLogInfo() {
        return glGetInfoLogARB(shaderID, glGetObjectParameteriARB(shaderID, GL_OBJECT_INFO_LOG_LENGTH_ARB));
    }

    public int getShaderID() {
        return shaderID;
    }

    public RenderEngine getRenderEngine() {
        return renderEngine;
    }

    public int getShaderType() {
        return shaderType;
    }

    public boolean isCompiled() {
        return isCompiled;
    }
}
