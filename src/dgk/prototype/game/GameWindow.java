package dgk.prototype.game;

import dgk.prototype.font.TrueTypeFont;
import dgk.prototype.game.tile.Node;
import dgk.prototype.game.tile.Pathfinder;
import dgk.prototype.game.tile.TileMap;
import dgk.prototype.game.tile.World;
import dgk.prototype.gui.GUI;
import dgk.prototype.gui.GUIInGameMenu;
import dgk.prototype.gui.GUILoadingScreen;
import dgk.prototype.gui.GUIOptions;
import dgk.prototype.input.InputManager;
import dgk.prototype.sound.SoundManager;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.io.File;
import java.nio.*;
import java.util.ArrayList;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjglx.util.glu.GLU.gluPerspective;

public class GameWindow {

    public static final String TITLE = "Prototype by DGK - twitch.tv/givemesomecurry";

    public int width = 800;
    public int height = 600;

    public static GameWindow INSTANCE = null;

    public boolean isRunning = false;

    public GUI gui;

    public ResourceManager resourceManager;
    public InputManager inputManager;
    public SoundManager soundManager;

    public World world;
    private GameCamera worldCamera;

    private long handle;

    private boolean isMinimized;

    private boolean isResizing;

    public GameWindow(InputManager inputManager) {
        INSTANCE = this;

        this.world = new World();
        this.worldCamera = new GameCamera(0,0, width, height);

        this.gui = new GUILoadingScreen(width, height, resourceManager);

        this.inputManager = inputManager;
        this.resourceManager = new ResourceManager();
        this.soundManager = new SoundManager();

        this.isMinimized = false;
        this.isResizing = false;
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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean setup() {
        GLFWErrorCallback.createPrint(System.err).set();

        if(!GLFW.glfwInit()) {
            return false;
        }

        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
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

        GLFW.glfwSetKeyCallback(handle, (window, key, scancode, action, mods) -> {
            inputManager.onKeyCallback(key, scancode, action, mods);
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

//        GLFW.glfwSetWindowSizeCallback(handle, (window, width, height) -> {
//
//            System.out.println("GameWindow Resize Request -> (" + width + ", " + height + ")");
//
//            this.width = width;
//            this.height = height;
//            getWorldCamera().onWindowResize(width, height);
//
//            float aspect = (float) worldCamera.getWidth() / (float) worldCamera.getHeight();
//
//            glViewport(0, 0, width, height);
//
//            glMatrixMode(GL_PROJECTION_MATRIX);
//            glPushMatrix();
//
//            glOrtho(0, height, width, 0, 1f, -1);
//
//            glPopMatrix();
//
//            glMatrixMode(GL_MODELVIEW_MATRIX);
//        });

        GLFW.glfwSetFramebufferSizeCallback(handle, (window, width, height) -> {
            this.width = width;
            this.height = height;
            getWorldCamera().onWindowResize(width, height);
            glViewport(0, 0, width, height);

            glMatrixMode(GL_PROJECTION);
            glLoadIdentity();
            glOrtho(0, width, height, 0, 1f, -1f);
            glMatrixMode(GL_MODELVIEW);
            glLoadIdentity();
        });

        GLFW.glfwSetWindowIconifyCallback(handle, (window, iconified) -> {
            System.out.println(iconified);
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

        glViewport(0, 0, width, height);
        glOrtho(0f, width, height, 0f, 1f, -1f);

        glClearColor(1f, 1f, 1f, 1f);

        glEnable(GL_MULTISAMPLE);
        glEnable(GL_BLEND);

        glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);

        boolean isLoading = true;

        // TODO: ResourceManager that loads the textures and all IDs are stored adequately
        resourceManager.start();
        inputManager.start();
        soundManager.start();

        gui.init();
        world.load();

        long start = System.currentTimeMillis();

        while(!hasWindowRequestedClose() && isRunning) {

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            if (gui instanceof GUILoadingScreen) {
                GUILoadingScreen loadingScreen = (GUILoadingScreen) gui;

                if (loadingScreen.isComplete()) {
                    setGUI(new GUIInGameMenu(800, 600));
                    isLoading = false;
                }
            }

            worldCamera.onUpdate(this);

            if (!isLoading) {
                world.onUpdate();
            }

            gui.update();

            if (!isLoading) {
                world.render();
            }

            gui.render();

            glfwSwapBuffers(handle);

            glfwPollEvents();
        }

        destroy();
    }

    public void setGUI(GUI gui) {
        this.gui = gui;
    }

    public void stop() {
        if(!isRunning)
            return;

        isRunning = false;
    }

    public boolean isKeyPressed(int keyCode) {
        return inputManager.isKeyPressed(keyCode);
    }

    private void destroy() {
        GLFW.glfwDestroyWindow(handle);
        GLFW.glfwTerminate();
        GLFW.glfwSetErrorCallback(null).free();

        resourceManager.stop();
        soundManager.stop();
        inputManager.stop();

        //spriteSheet.destroyTextures();
        //charSheet.destroyTextures();
    }

    private boolean hasWindowRequestedClose() {
        return GLFW.glfwWindowShouldClose(handle);
    }
}
