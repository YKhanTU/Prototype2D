package dgk.prototype.util;

import dgk.prototype.game.IEntity;

public class AABB {

    public Vec2D min;
    public Vec2D max;

    public AABB(Vec2D min, Vec2D max) {
        this.min = min;
        this.max = max;
    }

    public Vec2D getMax() {
        return max;
    }

    public Vec2D getMin() {
        return min;
    }

    /**
     * Updates the AABB to the current object's position.
     */
    public void update(IEntity entity) {

    }

    public boolean isIntersecting(AABB other) {
        if(max.getX() < other.getMin().getX() ||
                min.getX() > other.getMax().getX()) {
            return false;
        }

        if(max.getY() < other.getMin().getY() ||
                min.getY() > other.getMax().getY()) {
            return false;
        }

        return true;
    }

}
