package dgk.prototype.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class ParticleSystem {

    private static final Random RANDOM = new Random();

    private static final Vec2D CONSTANT_ACCELERATION = new Vec2D(0, .098);

    private Vec2D origin;

    /**
     * FIFO, based on the assumption that the particles are gone lifeSpan wise.
     */
    private Queue<Particle> particles;

    public ParticleSystem(Vec2D origin) {
        this.origin = origin;

        this.particles = new LinkedList<Particle>();
    }

    public int getSize() {
        return this.particles.size();
    }

    public void addParticle() {
        //this.particles.add(new Particle(TextureLoader.getTexture("16xDevParticle").getId(), RANDOM.nextInt(16) + 32, RANDOM.nextInt(16) + 32, .9f, origin.getX() - RANDOM.nextInt(100), origin.getY(), 0, .35));
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
