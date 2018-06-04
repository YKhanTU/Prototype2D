package dgk.prototype.game;

import dgk.prototype.gui.GUI;
import dgk.prototype.util.SpriteSheet;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;
import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class GameWindow {

    public static final String TITLE = "Prototype by DGK - twitch.tv/givemesomecurry";

    public int width = 800;
    public int height = 600;

    public static GameWindow INSTANCE = null;

    public boolean isRunning = false;

    private HashMap<Integer, Boolean> keyMap;

    private GUI gui;

    public SpriteSheet spriteSheet;
    public SpriteSheet charSheet;
    public SpriteSheet rulerAnimations;
    public SpriteSheet peasantAnimations;

    public World world;
    private GameCamera worldCamera;

    private long handle;
    private boolean isHidden;

    public GameWindow() {
        INSTANCE = this;

        this.world = new World();
        this.worldCamera = new GameCamera(0,0, width, height);

        this.gui = new GUI(800, 600);

        isHidden = false;
    }

    public static GameWindow getInstance() {
        return INSTANCE;
    }

    public long getHandle() {
        return handle;
    }

    public GameCamera getWorldCamera() {
        return this.worldCamera;
    }

    public boolean setup() {
        GLFWErrorCallback.createPrint(System.err).set();

        if(!GLFW.glfwInit()) {
            return false;
        }

        // Input
        this.keyMap = new HashMap<Integer, Boolean>();

        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, 4);

        // NULL is the equivalent of a long with the value of 0
        handle = GLFW.glfwCreateWindow(width, height, TITLE, NULL, NULL);

        if(handle == NULL) {
            return false;
        }

        try(MemoryStack stack = stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);

            GLFW.glfwGetWindowSize(handle, width, height);

            GLFWVidMode videoMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

            GLFW.glfwSetWindowPos(handle, (videoMode.width() - width.get(0)) / 2,
                                                (videoMode.height() - width.get(0)) / 2);
        }

        // OpenGL context current
        GLFW.glfwMakeContextCurrent(handle);

        GLFW.glfwSetWindowSizeCallback(handle, (window, width, height) -> {
            //this.width = width;
            //this.height = height;
        });

        GLFW.glfwSetKeyCallback(handle, (window, key, scancode, action, mods) -> {
            if(action == GLFW_PRESS) {
                keyMap.put(key, true);
            }else if(action == GLFW_RELEASE) {
                keyMap.replace(key, false);
            }
        });

        GLFW.glfwSetMouseButtonCallback(handle, (window, button, action, mods) -> {
            if(button == GLFW_MOUSE_BUTTON_1) {
                DoubleBuffer xBuffer = BufferUtils.createDoubleBuffer(1);
                DoubleBuffer yBuffer = BufferUtils.createDoubleBuffer(1);

                glfwGetCursorPos(handle, xBuffer, yBuffer);

                double mX = xBuffer.get(0);
                double mY = yBuffer.get(0);

                if(action == GLFW_PRESS) {
                    gui.onMouseInput(mX, mY, true);
                }else{
                    gui.onMouseInput(mX, mY, false);
                }
            }
        });

        GLFW.glfwShowWindow(handle);

        return true;
    }

    private void hide() {
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
    }

    // Temporarily here
    public void loop() {
        isRunning = true;

        // Binds GLFW with OpenGL
        GL.createCapabilities();

        glOrtho(0f, 800, 600, 0f, 1f, -1f);

        glClearColor(1.0f, 1f, 1f, 1f);

        glEnable(GL_MULTISAMPLE);
        glEnable(GL_BLEND);

        glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);

        spriteSheet = new SpriteSheet("SpriteSheet");
        spriteSheet.loadTexture(0, 0, 39, 39);
        spriteSheet.loadTexture(1, 0, 39, 39);
        spriteSheet.loadTexture(0, 2, 39, 39);
        charSheet = new SpriteSheet("CharSpriteSheet");
        charSheet.loadTexture(0, 0, 48, 48);
        charSheet.loadTexture(1, 0, 48, 48);
        spriteSheet.loadTexture(234, 0, 78);
        rulerAnimations = new SpriteSheet("RulerAnimations");

        for(int col = 0; col < 4; col++) {
            for(int row = 0; row < 3; row++) {
                rulerAnimations.loadTexture(row, col, 48, 48);
            }
        }

        peasantAnimations = new SpriteSheet("PeasantAnimations");

        for(int col = 0; col < 4; col++) {
            for(int row = 0; row < 3; row++) {
                peasantAnimations.loadTexture(row, col, 48, 48);
            }
        }

        gui.init();
        world.load();

        while(!hasWindowRequestedClose() && isRunning) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            worldCamera.onUpdate(this);
            world.onUpdate();
            gui.onUpdate();

            world.render();
            gui.render();

            glfwSwapBuffers(handle);

            glfwPollEvents();
        }

        destroy();
    }

    public void stop() {
        if(!isRunning)
            return;

        isRunning = false;
    }

    public boolean isKeyPressed(int keyCode) {
        if(keyMap.containsKey(keyCode)) {
            return keyMap.get(keyCode);
        }

        return false;
    }

    private void destroy() {
        GLFW.glfwDestroyWindow(handle);
        GLFW.glfwTerminate();
        GLFW.glfwSetErrorCallback(null).free();

        spriteSheet.destroyTextures();
        charSheet.destroyTextures();
    }

    private boolean hasWindowRequestedClose() {
        return GLFW.glfwWindowShouldClose(handle);
    }
}
