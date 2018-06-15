package dgk.prototype.util;

public class Animation {

    /**
     * The beginning Texture ID for the Animation.
     */
    private int beginningFrameId;

    private int currentFrame;

    /**
     * The length, or amount of Texture IDs that are in this animation.
     */
    private int animationLength;

    /**
     * Tells us if this animation is repeatable or not. If it's not repeatable, the animation
     * will be stopped and will notify the Entity or GameObject.
     */
    private boolean isRepeatable;

    private int[] animationArray;

    private long startTime = -1L;
    private long animationTime;

    private boolean isStopped;

    public Animation(int beginningFrameId, int animationLength, long animationTime, boolean isRepeatable) {
        this.beginningFrameId = beginningFrameId;

        this.animationLength = animationLength;
        this.animationTime = animationTime;

        this.currentFrame = 0;
        this.isRepeatable = isRepeatable;

        this.animationArray = new int[animationLength];

        int counter = 0;

        for(int i = beginningFrameId; i < (beginningFrameId + animationLength); i++) {
            animationArray[counter] = i;
            counter++;
        }

        this.isStopped = false;
    }

    public void onUpdate() {
        if(isStopped) {
            return;
        }

        if(isAnimationComplete() && shouldChangeFrame()) {
            if(isRepeatable) {
                currentFrame = 0;
                startTime = System.currentTimeMillis();
            }else{
                stop();
            }
        }

        if(shouldChangeFrame()) {
            currentFrame++;
            startTime = System.currentTimeMillis();
        }
    }

    public boolean shouldChangeFrame() {
        return (System.currentTimeMillis() - startTime) >= animationTime;
    }

    public boolean isOnLastFrame() {
        return currentFrame == animationLength - 1;
    }

    public void start() {
        // We will return if the Animation was already started.
        if(startTime != -1L || isStopped) {
            return;
        }

        startTime = System.currentTimeMillis();
    }

    public void stop() {
        if(!isRepeatable) {
            isStopped = true;
        }
    }

    public boolean isStopped() {
        return isStopped;
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public int getCurrentFrameTextureId() {
        return animationArray[currentFrame];
    }

    /**
     * Returns if the animation is complete. This varies on usage, as the animation
     * can be repeatable or used once.
     * @return
     */
    public boolean isAnimationComplete() {
        return getCurrentFrameTextureId() == (beginningFrameId + animationLength - 1);
    }
}
