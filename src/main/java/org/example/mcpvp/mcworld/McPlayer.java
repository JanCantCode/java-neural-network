package org.example.mcpvp.mcworld;

import org.example.mcpvp.MathHelper;

public class McPlayer {
    private Vector position = new Vector(0, 50, 0);
    private Vector velocity = new Vector(0, 0, 0);
    private boolean onGround;
    private float yaw;
    private float pitch;
    public McPlayer() {

    }

    public Vector getPos() {
        return this.position;
    }

    public void tick() {

        boolean pressingForward = true;
        boolean pressingRight = false;
        boolean pressingLeft = false;
        boolean pressingBackwards = false;

        float forward = getMovementMultiplier(pressingForward, pressingBackwards);
        float sideWays = getMovementMultiplier(pressingLeft, pressingRight);

        Vector v = movementInputToVelocity(new Vector(sideWays, 0, forward), 0.05f, yaw);


        this.move(v, 0.05f);
        this.position.add(this.velocity);
    }

    public static float getMovementMultiplier(boolean positive, boolean negative) {
        if (positive == negative) {
            return 0.0F;
        } else {
            return positive ? 1.0F : -1.0F;
        }
    }



    public void move(Vector direction, float speed) {
        Vector velocity = this.velocity;
        boolean falling = velocity.getY() <= 0d;
        double gravity = falling ? 0.01d : 0.08d;

        float groundFriction = 0.6f;
        float friction = this.onGround ? groundFriction * 0.91f : 0.91f;

        this.calcMovement(direction, speed, groundFriction);

        velocity.setX(velocity.getX() * (double) friction);
        velocity.setY((velocity.getY() - gravity) * 0.9800000190734863d);
        velocity.setZ(velocity.getZ() * (double) friction);
    }

    public void calcMovement(Vector direction, float speed, float friction) {
        double dirLengthSq = direction.lengthSquared();
        if (dirLengthSq >= 1e-7d) {
            if (dirLengthSq > 1d) {
                direction.normalize();
            }

            direction.multiply(speed);
            if (!this.onGround) {
                direction.multiply(0.1f);
            } else {
                direction.multiply(0.21600002f / (friction * friction * friction));
            }

            this.velocity.add(direction);
        }

        this.doMovement();
    }

    public void doMovement() {
        Vector velocity = this.velocity;
        Vector collVel = this.collide(this.position, velocity);
        double collidedMovementLengthSq = collVel.lengthSquared();
        if (collidedMovementLengthSq > 1e-7d) {
            this.position.add(collVel);
        }

        boolean collisionX = !MathHelper.equals(velocity.getX(), collVel.getX());
        boolean collisionY = !MathHelper.equals(velocity.getY(), collVel.getY());
        boolean collisionZ = !MathHelper.equals(velocity.getZ(), collVel.getZ());

        this.onGround = collisionY && velocity.getY() < 0d;

        if (collisionX) {
            velocity.setX(0d);
        }
        if (collisionY) {
            velocity.setY(0d);
        }
        if (collisionZ) {
            velocity.setZ(0d);
        }
    }

    public Vector collide(Vector pos, Vector velocity) {
        return new Vector(0,-1,0);
    }


    private static Vector movementInputToVelocity(Vector movementInput, float speed, float yaw) {
        double d = movementInput.lengthSquared();
        if (d < 1.0E-7) {
            return new Vector(0, 0, 0);
        } else {
            Vector Vector = (d > 1.0 ? movementInput.normalize() : movementInput).multiply((double)speed);
            float f = (float) MathHelper.sin(yaw * 0.017453292F);
            float g = (float) MathHelper.cos(yaw * 0.017453292F);
            return new Vector(Vector.getX() * (double)g - Vector.getZ() * (double)f, Vector.getY(), Vector.getZ() * (double)g + Vector.getX() * (double)f);
        }
    }
}
