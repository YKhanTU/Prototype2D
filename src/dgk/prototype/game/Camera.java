package dgk.prototype.game;

import dgk.prototype.util.Vec2D;

import static org.lwjgl.glfw.GLFW.*;

public class Camera {

    /*
    The tween for the Camera, if it is locked onto a moving target, or simply a GameObject.
     */
    public static final double TWEEN = .5D;

    private double width;
    private double height;

    private Vec2D position;

    private float scale = 1.0f;

    private IEntity target = null;

    /*
    Zoom value between .1 and 1.0f. (1 to 100%)
     */
    private float zoom = 1.0f;

    public Camera(double x, double y, double width, double height) {
        this.position = new Vec2D(x, y);
        this.width = width;
        this.height = height;
    }

    public Vec2D getPosition() {
        return this.position;
    }

    public boolean hasTarget() {
        return (target == null) ? false : true;
    }

    public final IEntity getTarget() {
        return target;
    }

    public final void setTarget(IEntity e) {
        this.target = e;
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    public void onUpdate() {}

    @Override
    public String toString() {
        return "Camera - (x: " + getPosition().getX() + ", y: " + getPosition().getY() + ", zoom: " + (100 * zoom) +  "%, hasTarget: " + hasTarget() + ", target: " + getTarget() + ")";
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void onWindowResize(int width, int height) {
        this.width = width;
        this.height = height;
    }
}
