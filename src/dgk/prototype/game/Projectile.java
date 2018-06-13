package dgk.prototype.game;

import dgk.prototype.game.entities.Person;
import dgk.prototype.util.Vec2D;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

/**
 * A projectile, which is an entity that is thrown, shot, or falling.
 *
 * This class will represent arrows, cannon balls, and so on and deal with collision with other GameObjects.
 * Once the projectile has reached a limit in its distance or collided with an object aside from its creator, it will be removed from the world.
 *
 * TODO: Add the ability to use a Vec3D, using the Z component to illustrate the height of the component in the 'air' to simulate RL physics.
 * TODO: And also scale the model based on the ratio of how high it is in the air.
 *
 */
public abstract class Projectile extends GameObject {

    private GameCamera worldCamera;

    private Vec2D velocity;

    private int damage;

    private int width;
    private int height;

    private boolean shouldRemove;

    public Projectile(int textureId, Vec2D initPosition, int damage, int width, int height, Vec2D initVelocity) {
        super(textureId, initPosition);

        this.worldCamera = GameWindow.getInstance().getWorldCamera();

        this.damage = damage;

        if(this.damage <= 0)
            this.damage = 5;

        this.width = width;
        this.height = height;

        this.velocity = initVelocity;

        this.shouldRemove = true;
    }

    public void onImpact(IEntity entity) {
        // I need to fix this ambiguity for the Entity

        if(entity instanceof Person) {
            Person target = (Person) entity;

            if(!target.isDead()) {
                if(target.hasArmor()) {
                    target.setHealthPoints(target.getHealthPoints() - (int) (damage * .75));
                    target.setArmorPoints(target.getArmorPoints() - (int) (damage * .5));
                }else{
                    target.setHealthPoints(target.getHealthPoints() - damage);
                }
            }
        }
    }

    public boolean shouldRemove() {
        return this.shouldRemove;
    }

    public float getHeightScale() {

        // get Z component of vector or variable
        // var maxHeight = 50;
        // var projHeight = current...
        // velocity affects this over time
        // return projHeight / maxHeight + constant float (.25f) to not make it extremely small;

        return 1;
    }

    /**
     * The "Impulse" velocity, or the reverse direction of the velocity vector.
     * This may be used for when an Entity is hit on impact by an explosion, and this is
     * used to create a "punch effect" on the Entity.
     *
     * @return The "Impulse" velocity, or the reverse direction of the velocity vector.
     */
    public Vec2D getImpulseVelocity() {
        return new Vec2D(-velocity.getX(), -velocity.getY());
    }

    @Override
    public void render() {
        glEnable(GL_TEXTURE_2D);

        GL11.glMatrixMode(GL_MODELVIEW_MATRIX);
        GL11.glPushMatrix();

        GL11.glScalef(getHeightScale(), getHeightScale(), 0);

        GL11.glTranslated(getPosition().x - worldCamera.getPosition().getX() + width / 2, getPosition().y - worldCamera.getPosition().getY() + height / 2, 0);
        GL11.glRotatef((float) getPosition().getAngle(), 0 ,0, 1);
        GL11.glTranslated(-width / 2, -height / 2, 0);

        //this.getTexture().bind();

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex2f(0, 0);

        GL11.glTexCoord2f(0, 1);
        GL11.glVertex2f(width, 0);

        GL11.glTexCoord2f(1, 1);
        GL11.glVertex2f(width, height);

        GL11.glTexCoord2f(1, 0);
        GL11.glVertex2f(0, height);
        GL11.glEnd();

        GL11.glPopMatrix();

        glDisable(GL_TEXTURE_2D);
    }

    @Override
    public void onUpdate() {
        if(!shouldRemove) {
            getPosition().add(velocity);
        }
    }
}
