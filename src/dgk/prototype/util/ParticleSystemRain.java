package dgk.prototype.util;

import dgk.prototype.game.Camera;
import dgk.prototype.game.GameWindow;
import dgk.prototype.game.tile.World;
import dgk.prototype.sound.Sound;

import java.util.Iterator;

public class ParticleSystemRain extends ParticleSystem {

    private Sound rainLoop;

    public ParticleSystemRain(Vec2D origin, int xOffset) {
        super(origin, xOffset);

        this.rainLoop = GameWindow.getInstance().soundManager.getSound("rainLoop");

        this.init();
    }

    public void init() {
        this.rainLoop.playSound();
    }

    @Override
    void addParticle() {
        double minVelocity = .05D;
        double maxVelocity = 1.5D;
        double randomVelocity = RANDOM.nextDouble() * (maxVelocity - minVelocity) + minVelocity;

        AnimatableParticle rainParticle = new RainParticle(-1, World.RAIN_WIDTH, World.RAIN_HEIGHT, 1, new Vec2D(RANDOM.nextInt(xOffset + 130) + origin.getX() - 130, origin.getY() - 130), new Vec2D(2D, randomVelocity));
        Animation particleAnimation = new Animation(85, 5, RANDOM.nextInt(100) + 500, false);
        rainParticle.setAnimation(particleAnimation);
        rainParticle.getAnimation().start();
        this.particles.add(rainParticle);
    }

    public void onUpdate() {
        Camera gameCamera = GameWindow.getInstance().getWorldCamera();


        this.origin = new Vec2D(gameCamera.getPosition().getX(), gameCamera.getPosition().getY());

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
