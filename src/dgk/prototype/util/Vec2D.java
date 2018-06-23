package dgk.prototype.util;

import java.lang.Math;

public class Vec2D {

    // Vec2D a = new Vec2D(10, 10);
    // Vec2D b = new Vec2D(99, 12);
    // b = a;
    // b = reference of a
    // any time a changes, b stays the same value as a.
    /*

    int a = 10;
    int b = 5;

    b = a;
    a = 3;
    System.out.println(b) ---> 3


     */

    public double x;
    public double y;

    public Vec2D() {
        this(0, 0);
    }

    public Vec2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public void setX(double newX) {
        this.x = newX;
    }

    public void setY(double newY) {
        this.y = newY;
    }

    public double getMagnitude() {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }

    public double getDotProduct(Vec2D other) {
        return (this.x * other.getX() + this.y * other.getY());
    }

    public void add(Vec2D other) {
        this.x += other.getX();
        this.y += other.getY();
    }

    public Vec2D subtract(Vec2D other) {
        return new Vec2D(this.x - other.getX(), this.y - other.getY());
    }

    /**
     * Multiplies the Vec2D by a scalar.
     * @param mult
     */
    public void multiply(double mult) {
        this.x *= mult;
        this.y *= mult;
    }

    /**
     * Divides the Vec2D by a scalar.
     * @param div
     */
    public void divide(double div) {
        this.x /= div;
        this.y /= div;
    }

    /**
     * Normalizes the vector, so its value is reduced to 1. This is done
     * by dividing the vector by its own magnitude / length.
     */
    public void normalize() {
        this.divide(getMagnitude());
    }

    /**
     * Return the actual angle / direction of the vector.
     * @return angle of this vector in degrees.
     */
    public double getAngle() {
        return Math.atan2(getY(), getX()) * (180 / Math.PI);
    }

    public double getAngleBetween(Vec2D other) {
        double angle = Math.toDegrees(Math.atan2(other.y - y, other.x - x));

        if(angle < 0) {
            angle += 360;
        }

        return angle;
    }

    /**
     * Returns the distance between two Vectors with the Point-Distance Formula.
     * @return The square root of the addition of the differences (x2 - x1) and (y2 - y1)
     */
    public double getDistance(Vec2D other) {
        return (Math.sqrt(Math.pow(other.x - this.x, 2) + Math.pow(other.y - this.y, 2)));
    }

    /**
     * Gets a deep copy of the object, so we don't have to deal with the reference variable
     * haunting our dreams every night. :D
     * @return
     */
    public Vec2D getCopy() {
        try {
            return (Vec2D) this.clone();
        }catch(CloneNotSupportedException cnse) {
            cnse.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Vec2D)) {
            return false;
        }

        Vec2D other = (Vec2D) o;

        return (this.getX() == other.getX() &&
                this.getAngle() == other.getAngle() &&
                this.getMagnitude() == other.getMagnitude());
    }

    @Override
    public String toString() {
        return "(x: " + x + ", y: " + y + ")";
    }


}
