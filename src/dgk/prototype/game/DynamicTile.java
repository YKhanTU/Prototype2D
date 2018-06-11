package dgk.prototype.game;

/**
 * A DynamicTile represents dynamic 'moving' Tiles that may move 'out of grid'.
 * TODO: Possibly make this extend AnimatableTile?
 * These tiles have an AxisAlignedBoundingBox, but collisions may or may not be accounted for.
 */
public class DynamicTile extends Tile {

    public DynamicTile(int textureId, byte renderLayer, int x, int y, int size) {
        super(textureId, renderLayer, x, y, size);

        setPassable(false);
        setStatic(false);
    }
}
