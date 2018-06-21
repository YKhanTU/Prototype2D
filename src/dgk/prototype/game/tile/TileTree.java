package dgk.prototype.game.tile;

import dgk.prototype.util.AABB;
import dgk.prototype.util.SpriteSheet;
import dgk.prototype.util.Vec2D;

public class TileTree extends StaticTile {

    public static final int SIZE = 96;

    public TileTree(int x, int y) {
        super(SpriteSheet.TREE, World.MID_LAYER, x, y, SIZE);
        setPassable(false);
    }

    @Override
    public AABB getAABB() {
        Vec2D position = getPosition();
        Vec2D AxisAlignedBBpos = new Vec2D(position.getX(), position.getY() + (SIZE / 2));
        Vec2D bottomRight =  new Vec2D(getPosition().getX() + SIZE - 16, getPosition().getY() + SIZE - 16);

        return new AABB(AxisAlignedBBpos, bottomRight);
    }
}
