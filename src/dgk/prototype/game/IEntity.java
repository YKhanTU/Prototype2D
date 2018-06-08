package dgk.prototype.game;

import dgk.prototype.util.AABB;
import dgk.prototype.util.Vec2D;

/**
 * This is a representation of an entity, that is anything in the game that
 * is a living thing, game object, or so on.
 */
public interface IEntity {

    Vec2D getPosition();

    /**
     * Called every time a frame is set to be drawn.
     */
    void render();

    /**
     * Called every time a frame is set to be updated.
     */
    void onUpdate();

    AABB getAABB();

    void onCollision(IEntity other);
}
