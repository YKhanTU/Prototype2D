package dgk.prototype.input;

import dgk.prototype.game.GameWindow;
import dgk.prototype.util.IManager;
import dgk.prototype.util.Vec2D;
import org.lwjgl.BufferUtils;

import java.nio.DoubleBuffer;
import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;

public class InputManager implements IManager {

    private HashMap<Integer, Boolean> keyMap;

    public InputManager() {
        this.keyMap = new HashMap<Integer, Boolean>();
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {
        this.keyMap.clear();
    }

    @Override
    public void onUpdate() {

    }

    /**
     * Called each time the KeyCallback is called.
     * @param key The keyCode that is typed.
     * @param scancode Not used.
     * @param action The action used - press/release.
     * @param mods Not used.
     */
    public void onKeyCallback(int key, int scancode, int action, int mods) {
        if(action == GLFW_PRESS) {
            keyMap.put(key, true);
        }else if(action == GLFW_RELEASE) {
            keyMap.replace(key, false);
        }
    }

    public boolean isKeyPressed(int keyCode) {
        if(keyMap.containsKey(keyCode)) {
            return keyMap.get(keyCode);
        }

        return false;
    }

    /**
     * Gets the OpenGL mouse position from GLFW
     *
     * TODO: Remove this method from GUI and implement it so that GUI calls this function instead from InputManager.
     *
     * @return GLFW mouse position
     */
    public Vec2D getMousePosition() {
        DoubleBuffer xBuffer = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer yBuffer = BufferUtils.createDoubleBuffer(1);

        glfwGetCursorPos(GameWindow.getInstance().getHandle(), xBuffer, yBuffer);

        double mX = xBuffer.get(0);
        double mY = yBuffer.get(0);

        return new Vec2D(mX, mY);
    }
}
