package dgk.prototype.game.tile;

import dgk.prototype.util.AABB;
import dgk.prototype.util.SpriteSheet;
import dgk.prototype.util.Vec2D;

public class TileShrub extends StaticTile {
    public static final int SIZE = 48;

    public TileShrub(int x, int y) {
        super(SpriteSheet.SHRUB, World.MID_LAYER, x, y, SIZE);
    }

    @Override
    public AABB getAABB() {
        Vec2D position = getPosition();
        Vec2D AxisAlignedBBpos = new Vec2D(position.getX() - 8, position.getY() + (SIZE / 2));
        Vec2D bottomRight =  new Vec2D(getPosition().getX() + SIZE - 16, getPosition().getY() + SIZE - 8);

        return new AABB(AxisAlignedBBpos, bottomRight);
    }
}
