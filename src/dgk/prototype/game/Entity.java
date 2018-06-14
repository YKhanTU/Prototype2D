package dgk.prototype.game;

import dgk.prototype.game.entities.Person;
import dgk.prototype.util.Vec2D;

import java.io.Serializable;

public abstract class Entity implements IEntity, Serializable {

    private int textureId;

    private String name;

    protected Vec2D position;
    protected Vec2D velocity;

    private Direction direction;
    private CharacterState state;

    private Inventory inventory;

    private int healthPoints;
    private int armorPoints;

    private transient IEntity target;

    public boolean isMoving = false;

    protected Vec2D lastPosition;
    /**
     * Represents if this Entity has been selected or not by the user.
     */
    protected boolean isSelected;

    public Entity(int textureId, String name, double x, double y) {
        this.textureId = textureId;
        this.position = new Vec2D(x, y);
        this.velocity = new Vec2D();
        this.isSelected = false;

        this.name = name;

        this.direction = Direction.SOUTH;

        this.inventory = new Inventory(24, false);

        this.state = CharacterState.IDLE;

        this.healthPoints = 100;
        this.armorPoints = 0;

        this.target = null;

        this.lastPosition = getPosition();
    }

    public Vec2D getPosition() {
        return this.position;
    }

    public Vec2D getVelocity() {
        return this.velocity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public int getHealthPoints() {
        return this.healthPoints;
    }

    public void setHealthPoints(int healthPoints) {
        if(healthPoints < 0) {
            this.healthPoints = 0;
            return;
        }

        this.healthPoints = healthPoints;
    }

    public boolean isDead() {
        return (healthPoints <= 0);
    }

    public int getArmorPoints() {
        return armorPoints;
    }

    public void setArmorPoints(int armorPoints) {
        if(armorPoints < 0) {
            this.armorPoints = 0;
            return;
        }

        this.armorPoints = armorPoints;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public boolean isIdle() {
        return !isMoving;
    }

    public IEntity getTarget() {
        return target;
    }

    public void setTarget(IEntity target) {
        this.target = target;
    }

    public boolean hasTarget() {
        return (target == null) ? false : true;
    }

    private boolean isInCombat() {
        return hasTarget();
    }

    public boolean hasArmor() {
        return this.getArmorPoints() > 0;
    }

    public int getTextureId() {
        return this.textureId;
    }

    public boolean isSelected() {
        return isSelected;
    }

}
