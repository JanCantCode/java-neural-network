package org.example.mcpvp.mcworld;

public class Vector {
    private double x;
    private double y;
    private double z;
    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector multiply(double multiplier) {
        return new Vector( this.x * multiplier, this.y * multiplier, this.z * multiplier);
    }

    public Vector add(Vector v) {
        return new Vector(this.x + v.getX(), this.y + v.getY(), this.z + v.getZ());
    }


    public double lengthSquared() {
        return (this.x * this.x + this.y * this.y + this.z * this.z);
    }

    public Vector normalize() {
        double x = this.getX();
        double y = this.getY();
        double z = this.getZ();
        double len = Math.sqrt(this.lengthSquared());
        if (len != 0) {
            x /= len;
            y /= len;
            z /= len;
        }
        return new Vector(x, y, z);
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

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    @Override
    public String toString() {
        return this.getX()+" "+this.getY()+" "+this.getZ();
    }

}
