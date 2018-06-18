package dgk.prototype.game;

import dgk.prototype.util.Vec2D;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;

public class GameCamera extends Camera {

    public static final float MAX_ZOOM = 1.5f;
    public static final float MIN_ZOOM = .75f;

    public static final float CAMERA_ZOOM_VALUE = .01f;

    private double chaseFactor;

    public GameCamera(double x, double y, double width, double height) {
        super(x, y, width, height);

        this.chaseFactor = 25;
    }

    public void setChaseFactor(float factor) {
        if(factor <= .1f) {
            this.chaseFactor = 25;
            return;
        }

        this.chaseFactor = factor * 100;
    }

    public void onUpdate(GameWindow gameWindow) {
        if(hasTarget()) {
            Vec2D targetPos = getTarget().getPosition();

            // We are targeting the 'center', where the camera wants to be, with the target's size taking into account.
            Vec2D targetVector = new Vec2D(targetPos.getX() - (getWidth() / 2) + 32, targetPos.getY() - (getHeight() / 2) + 32);

            // Target Vector - Source Vector
            // Destination Vector - Source Vector
            Vec2D vec = targetVector.subtract(getPosition());

            vec.divide(chaseFactor);

            getPosition().add(vec);

            if(getPosition().getX() < 0) {
                getPosition().setX(0);
            }
            if(getPosition().getY() < 0) {
                getPosition().setY(0);
            }

            return;
        }

        if(gameWindow.isKeyPressed(GLFW_KEY_LEFT)) {
            getPosition().add(new Vec2D(-5, 0));

            if(getPosition().getX() < 0) {
                getPosition().x = 0;
            }
        }
        if(gameWindow.isKeyPressed(GLFW_KEY_RIGHT)) {
            getPosition().add(new Vec2D(5, 0));
        }
        if(gameWindow.isKeyPressed(GLFW_KEY_UP)) {
            getPosition().add(new Vec2D(0, -5));

            if(getPosition().getY() < 0) {
                getPosition().y = 0;
            }
        }
        if(gameWindow.isKeyPressed(GLFW_KEY_DOWN)) {
            getPosition().add(new Vec2D(0, 5));
        }

        if(gameWindow.isKeyPressed(GLFW_KEY_PAGE_UP)) {
            setZoom(getZoom() + CAMERA_ZOOM_VALUE);

            if(getZoom() >= MAX_ZOOM) {
                setZoom(MAX_ZOOM);
            }

            System.out.println(this);
        }

        if(gameWindow.isKeyPressed(GLFW_KEY_PAGE_DOWN)) {
            setZoom(getZoom() - CAMERA_ZOOM_VALUE);

            if(getZoom() < MIN_ZOOM) {
                setZoom(MIN_ZOOM);
            }

            System.out.println(this);
        }
    }
}
