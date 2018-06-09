package dgk.prototype.util;

/**
 * This class is mainly used for 'flying' objects in the game.
 * Either objects that are 'dropped' in-game to represent a 'floating' tile, or
 * to represent a Projectile that is essentially flying in the air.
 */
public class Vec3D {

    public double x;
    public double y;
    public double z;

    public Vec3D() {
        this(0, 0, 0);
    }

    public Vec3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public void setX(double newX) {
        this.x = newX;
    }

    public void setY(double newY) {
        this.y = newY;
    }

    public void setZ(double newZ) {
        this.z = z;
    }

    public double getMagnitude() {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    public double getDotProduct(Vec3D other) {
        return (this.x * other.getX() + this.y * other.getY());
    }

    public void add(Vec3D other) {
        this.x += other.getX();
        this.y += other.getY();
        this.z += other.getZ();
    }

    public Vec3D subtract(Vec3D other) {
        return new Vec3D(this.x - other.getX(), this.y - other.getY(), this.z - other.getZ());
    }

    public void multiply(double mult) {
        this.x *= mult;
        this.y *= mult;
        this.z *= mult;
    }

    public void divide(double div) {
        this.x /= div;
        this.y /= div;
        this.z /= div;
    }

    public void normalize() {
        this.divide(getMagnitude());
    }

    /**
     * Return the actual angle / direction of the vector.
     * @return angle of this vector in degrees.
     */
    //public double getAngle() {
    //    return Math.atan2(getY(), getX()) * (180 / Math.PI);
    //}

    //public double getAngleBetween(Vec2D other) {
//        double angle = Math.toDegrees(Math.atan2(other.y - y, other.x - x));
//
//        if(angle < 0) {
//            angle += 360;
//        }
//
//        return angle;
//    }

    /**
     * Returns the distance between two Vectors with the Point-Distance Formula.
     * @return The square root of the addition of the differences (x2 - x1) and (y2 - y1)
     */
    public double getDistance(Vec2D other) {
        return (Math.sqrt(Math.pow(other.x - this.x, 2) + Math.pow(other.y - this.y, 2)));
    }

    @Override
    public String toString() {
        return "(x: " + x + ", y: " + y + ")";
    }
}
