package dgk.prototype.game;

import dgk.prototype.util.Vec2D;

import java.util.ArrayList;

public abstract class BuildingComponent extends GameObject {

    private double width;
    private double height;

    private boolean isConnected;

    public BuildingComponent(int textureId, double x, double y, double width, double height) {
        super(textureId, new Vec2D(x, y));

        this.width = width;
        this.height = height;

        isConnected = false;
    }

    public int getGridX() {
        return ((int) Math.ceil(this.getPosition().getX() / World.GRID_SIZE));
    }

    public int getGridY() {
        return ((int) Math.ceil(this.getPosition().getY() / World.GRID_SIZE));
    }

    public boolean isConnected() {
        return isConnected;
    }

    public ArrayList<BuildingComponent> getNeighbors() {
        return null;
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
}
