package dgk.prototype.game.tile;

/**
 * A StaticTile represents scenery or other objects that are there simply to be aesthetic.
 * These tiles have an AxisAlignedBoundingBox, so collisions are accounted for with Entities and other Objects.
 */
public abstract class StaticTile extends Tile {

    public StaticTile(int textureId, byte renderLayer, int x, int y, int size) {
        super(textureId, renderLayer, x, y, size);

        setPassable(false);
        setStatic(true);
    }
}
