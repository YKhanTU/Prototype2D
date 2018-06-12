package dgk.prototype.game;

import dgk.prototype.util.AABB;
import dgk.prototype.util.Vec2D;
import org.lwjgl.opengl.GL11;

import java.util.List;

import static org.lwjgl.opengl.GL11.GL_MODELVIEW_MATRIX;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glEnable;

public abstract class Person extends Entity {

    public static final int WIDTH = 32;
    public static final int HEIGHT = 48;

    private String name;
    private Person spouse;

    private Direction direction;
    private CharacterState state;

    private Inventory inventory;

    private int healthPoints;
    private int armorPoints;

    private transient IEntity target;

    public boolean isMoving = false;

    protected Vec2D lastPosition;

    public Person(int textureId, String name, double x, double y) {
        super(textureId, x, y);

        this.name = name;
        this.spouse = null;

        this.direction = Direction.SOUTH;

        this.inventory = new Inventory(24, false);

        this.state = CharacterState.IDLE;

        this.healthPoints = 100;
        this.armorPoints = 0;

        this.target = null;

        this.lastPosition = getPosition();
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

    public boolean isIdle() {
        return !isMoving;
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

    public boolean hasArmor() {
        return this.getArmorPoints() > 0;
    }

    @Override
    public AABB getAABB() {
        Vec2D position = getPosition();
        Vec2D AxisAlignedBBpos = new Vec2D(position.getX(), position.getY() + (HEIGHT / 2 + 8));

        /**
         * AABB
         * [            0
         *            --|--     ____
         *             /\     [     ]
         *                     ^^^^^
         *                      64x16
         */

        return new AABB(AxisAlignedBBpos, new Vec2D(getPosition().getX() + WIDTH, getPosition().getY() + HEIGHT));
    }

    @Override
    public void onCollision(IEntity other) {
        System.out.println("Collision detected.");
    }

    /**
     * Checks for collision between Entities, then checks for Collision between this Entity and Tiles.
     * @param world
     */
    protected void checkForCollision(World world) {
        List<Entity> entityList = world.getEntities();

        for(Entity e : entityList) {
            if(e.equals(this))
                continue;

            if(this.getPosition().getDistance(e.getPosition()) > (WIDTH + 5))
                continue;

            if(this.getAABB().isIntersecting(e.getAABB())) {
                this.position = lastPosition;

                return;
            }
        }
    }

    /**
     * Draws the shadow of the Person. For now, it just draws a simple low opacity eclipse that
     * goes under the player's name. This simplifies the implementation for 'ray tracing' or any
     * kind of advance lighting techniques (at least for now)
     * @param worldCamera
     */
    protected void drawShadow(Camera worldCamera) {
        glEnable(GL_TEXTURE_2D);

        GL11.glMatrixMode(GL_MODELVIEW_MATRIX);
        GL11.glPushMatrix();

        GL11.glScalef(worldCamera.getZoom(), worldCamera.getZoom(), 0);
        GL11.glTranslated(getPosition().getX() - worldCamera.getPosition().getX() + 16, getPosition().getY() - worldCamera.getPosition().getY() + 46, 0);

        GameWindow.getInstance().resourceManager.getSpriteSheet("Shadow").bindTexture(42);

        GL11.glBegin(GL11.GL_QUADS);
        {
            GL11.glTexCoord2f(0, 0);
            GL11.glVertex2f(0, 0);

            GL11.glTexCoord2f(1, 0);
            GL11.glVertex2f(32, 0);

            GL11.glTexCoord2f(1, 1);
            GL11.glVertex2f(32, 32);

            GL11.glTexCoord2f(0, 1);
            GL11.glVertex2f(0, 32);
        }
        GL11.glEnd();

        GL11.glPopMatrix();
        GL11.glDisable(GL_TEXTURE_2D);
    }

    protected void drawHealthBar(Camera worldCamera) {
        int currentHealth = getHealthPoints();

        final int healthBarWidth = 60;
        final int healthBarHeight = 10;

        GL11.glDisable(GL_TEXTURE_2D);

        GL11.glMatrixMode(GL_MODELVIEW_MATRIX);
        GL11.glPushMatrix();

        GL11.glColor4f(0f, 1f, 0f, 1f);

        GL11.glScalef(worldCamera.getZoom(), worldCamera.getZoom(), 0);
        GL11.glTranslated(getPosition().getX() - worldCamera.getPosition().getX() + 2, getPosition().getY() - worldCamera.getPosition().getY() - (healthBarHeight + 5), 0);

        GL11.glBegin(GL11.GL_QUADS);
        {
            GL11.glVertex2f(0, 0);
            GL11.glVertex2f(healthBarWidth  * (currentHealth / 100), 0);
            GL11.glVertex2f(healthBarWidth * (currentHealth / 100), healthBarHeight);
            GL11.glVertex2f(0, healthBarHeight);
        }
        GL11.glEnd();

        GL11.glPopMatrix();
        GL11.glEnable(GL_TEXTURE_2D);
    }

    /**
     * Draws information above the character when they are selected.
     */
    protected void drawSelectionInfo() {}
}
