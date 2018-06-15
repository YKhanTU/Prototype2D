package dgk.prototype.util;

import dgk.prototype.game.GameWindow;
import dgk.prototype.game.tile.World;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 * The ParticleSystem class. This represents the 'system' or body that contains all of the
 * particles. This class is intended to have multiple forms of functionality:
 *
 * TODO: Influence of scale/angle by the origin of the particle system.
 * TODO: ParticleSystem types, such as "spurts" of particles every few seconds, or a "waterfall".
 */
public abstract class ParticleSystem {

    protected static final Random RANDOM = new Random();

    protected static final Vec2D CONSTANT_ACCELERATION = new Vec2D(0, .098);

    protected Vec2D origin;

    protected int xOffset;

    /**
     * FIFO, based on the assumption that the particles are gone lifeSpan wise.
     */
    protected Queue<Particle> particles;

    public ParticleSystem(Vec2D origin, int xOffset) {
        this.origin = origin;
        this.xOffset = xOffset;

        this.particles = new LinkedList<Particle>();
    }

    public int getSize() {
        return this.particles.size();
    }

    /**
     * This method is executed when the Particle System is started up.
     */
    abstract void init();

    /**
     * This method is called when a particle is added at a constant rate per second.
     */
    abstract void addParticle();

    public void onUpdate() {
        addParticle();

        Iterator<Particle> i = particles.iterator();

        while(i.hasNext()) {
            Particle particle = i.next();

            if(particle.isDead()) {
                i.remove();
            }

            particle.applyForce(CONSTANT_ACCELERATION);
            particle.onUpdate();
        }
    }

    public void render() {
        Iterator<Particle> i = particles.iterator();

        while(i.hasNext()) {
            Particle particle = i.next();

            particle.render();
        }
    }
}
