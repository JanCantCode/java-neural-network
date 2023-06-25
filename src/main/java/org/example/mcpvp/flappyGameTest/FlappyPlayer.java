package org.example.mcpvp.flappyGameTest;

import org.example.NeuralNetwork;

import java.awt.*;

public class FlappyPlayer {
    public int distanceFromNearestGoal;
    private final int size = 50;
    public final int x = 100;
    private int y = 200;
    private double motion = 0;
    private int jumpTimer = 0;
    NeuralNetwork controller;
    private final FlappyGame game;
    private final int sceneHeight = 400;
    public boolean dead = false;
    public int score = 0;
    private boolean renderNetwork = false;
    public FlappyPlayer(FlappyGame game, boolean renderNetwork) {
        this.renderNetwork = renderNetwork;
        this.game = game;
    }

    public void render(Graphics g) {
        g.setColor(Color.CYAN);
        g.fillRect(this.x, this.y, this.size, this.size);
        g.setColor(Color.BLACK);
        g.drawString(String.valueOf(this.score), this.x + (this.size / 2), this.y + (this.size / 2));
    }

    public void tick() {
        score++;
        if (this.controller != null) {
            makeDecision();
            if (this.renderNetwork) {
                this.controller.render();
            }
        }

        jumpTimer -= 1;
        motion += 1.5;
        motion *= 0.9;

        this.y += motion;

        if (this.y > this.sceneHeight || -20 > this.y) {
            dead = true;
        }

        FlappyObstacle nearestObstacle = this.game.getNearestObstacle(this);
        this.distanceFromNearestGoal = Math.abs(this.y - nearestObstacle.getGoalHeight());

        if (isPointCollidingWithObstacle(this.x, this.y, this.size, this.size, nearestObstacle)) {
            this.dead = true;
        }
    }

    public boolean isPointCollidingWithObstacle(int pointX, int pointY, int width, int height, FlappyObstacle obstacle) {
        int topPartStartX = obstacle.getX();
        int topPartEndX = obstacle.getX() + obstacle.getWidth();
        int topPartStartY = 0;
        int topPartEndY = obstacle.getTopPartStop();

        int bottomPartStartX = obstacle.getX();
        int bottomPartEndX = obstacle.getX() + obstacle.getWidth();
        int bottomPartStartY = obstacle.getBottomPartBegin();
        int bottomPartEndY = this.sceneHeight;

        if (pointX + width >= topPartStartX && pointX <= topPartEndX && pointY + height >= topPartStartY && pointY <= topPartEndY) {
            return true;
        }

        if (pointX + width >= bottomPartStartX && pointX <= bottomPartEndX && pointY + height >= bottomPartStartY && pointY <= bottomPartEndY) {
            return true;
        }

        return false;
    }


    public void jump() {
        if (0 >= jumpTimer) {
            motion = -15.0;
            jumpTimer = 10;
        }
    }

    public void link(NeuralNetwork n) {
        this.controller = n;
    }

    public void makeDecision() {
        FlappyObstacle nearest = this.game.getNearestObstacle(this);
        double yDifference = (nearest.getHoleHeight() - this.y) / 100.0;
        double xDifference = this.game.distanceToPlayer(nearest, this) / 500; // normalize
        double[] inputs = new double[]{yDifference, xDifference};

        this.controller.predict(inputs);
        if (this.controller.getOutputs()[0] > this.controller.getOutputs()[1]) {
            this.jump();
        }
    }

    public NeuralNetwork getController() {
        return this.controller;
    }

    public double getScore() {
        double timeWeight = 0.3;
        double distanceWeight = 0.7;

        return (timeWeight * this.score) + (distanceWeight * (1.0 / this.distanceFromNearestGoal));
    }
}
