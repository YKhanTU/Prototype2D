package dgk.prototype.util;

import java.lang.Math;

public class Vec2D {

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

    public void multiply(double mult) {
        this.x *= mult;
        this.y *= mult;
    }

    public void divide(double div) {
        this.x /= div;
        this.y /= div;
    }

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

    @Override
    public String toString() {
        return "(x: " + x + ", y: " + y + ")";
    }
}
