package dgk.prototype.util;

import dgk.prototype.game.tile.World;

import java.util.Iterator;

public class ParticleSystemRain extends ParticleSystem {

    public ParticleSystemRain(Vec2D origin, int xOffset) {
        super(origin, xOffset);
    }

    @Override
    void addParticle() {
        double minVelocity = .05D;
        double maxVelocity = 1.5D;
        double randomVelocity = RANDOM.nextDouble() * (maxVelocity - minVelocity) + minVelocity;

        AnimatableParticle rainParticle = new RainParticle(-1, World.RAIN_WIDTH, World.RAIN_HEIGHT, 1, new Vec2D(RANDOM.nextInt(xOffset), origin.getY()), new Vec2D(.9D, randomVelocity));
        Animation particleAnimation = new Animation(85, 5, RANDOM.nextInt(100) + 500, false);
        rainParticle.setAnimation(particleAnimation);
        rainParticle.getAnimation().start();
        this.particles.add(rainParticle);
    }

    public void onUpdate() {
        addParticle();

        Iterator<Particle> i = particles.iterator();

        while(i.hasNext()) {
            RainParticle particle = (RainParticle) i.next();

            if(particle.isDead()) {
                i.remove();
            }

            if(!particle.hasHitGround()) {
                particle.applyForce(CONSTANT_ACCELERATION);
            }
            particle.onUpdate();
        }
    }
}
