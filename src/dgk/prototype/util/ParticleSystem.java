package dgk.prototype.util;

import dgk.prototype.game.GameWindow;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class ParticleSystem {

    private static final Random RANDOM = new Random();

    private static final Vec2D CONSTANT_ACCELERATION = new Vec2D(0, .098);

    private Vec2D origin;

    private int xOffset;

    /**
     * FIFO, based on the assumption that the particles are gone lifeSpan wise.
     */
    private Queue<Particle> particles;

    public ParticleSystem(Vec2D origin, int xOffset) {
        this.origin = origin;
        this.xOffset = xOffset;

        this.particles = new LinkedList<Particle>();
    }

    public int getSize() {
        return this.particles.size();
    }

    public void addParticle() {
        this.particles.add(new Particle(82, 3, 20, 1, new Vec2D(RANDOM.nextInt(xOffset), origin.getY()), new Vec2D(0, .05D)));
    }

    public void onUpdate() {
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
