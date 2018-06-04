package dgk.prototype.game;

public abstract class Person extends Entity {

    public static final int SIZE = 32;

    private String name;
    private Person spouse;
    private Direction direction;
    private Inventory inventory;
    private int healthPoints;
    private int armorPoints;
    private IEntity target;

    public boolean isMoving = false;

    public Person(int textureId, String name, double x, double y) {
        super(textureId, x, y);

        this.name = name;
        this.spouse = null;

        this.direction = Direction.SOUTH;

        this.inventory = new Inventory(24, false);

        this.healthPoints = 100;
        this.armorPoints = 0;
        this.target = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isMarried() {
        return hasSpouse();
    }

    public boolean hasSpouse() {
        return (spouse == null) ? false : true;
    }

    public Person getSpouse() {
        return spouse;
    }

    public void setSpouse(Person spouse) {
        this.spouse = spouse;
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
        return healthPoints;
    }

    public void setHealthPoints(int healthPoints) {
        if(healthPoints < 0) {
            this.healthPoints = 0;
            return;
        }

        this.healthPoints = healthPoints;
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

    private boolean isInCombat() {
        return hasTarget();
    }

    public boolean isDead() {
        return this.healthPoints <= 0;
    }

    public boolean hasTarget() {
        return (target == null) ? false : true;
    }

    public IEntity getTarget() {
        return target;
    }

    public void setTarget(IEntity target) {
        this.target = target;
    }
}
