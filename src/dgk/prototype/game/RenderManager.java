package dgk.prototype.game;

import dgk.prototype.util.IManager;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;

public class RenderManager implements IManager {

    public RenderManager() {

    }

    @Override
    public void start() {
        glOrtho(0f, 800, 600, 0f, 1f, -1f);

        glClearColor(1.0f, 1f, 1f, 1f);

        glEnable(GL_MULTISAMPLE);
        glEnable(GL_BLEND);

        glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    public void stop() {

    }

    @Override
    public void onUpdate() {

    }
}
