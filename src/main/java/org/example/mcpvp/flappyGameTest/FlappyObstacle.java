package org.example.mcpvp.flappyGameTest;

import java.awt.*;

public class FlappyObstacle {
    private final int sceneWidth = 800;
    private final int sceneHeight = 400;
    private final int holeHeight;
    private final int holeWidth;
    private final int topPartStop;
    private final int bottomPartBegin;
    private int x;
    private int speed = 10;
    public FlappyObstacle(int holeHeight, int holeWidth) {
        this.holeHeight = holeHeight;
        this.holeWidth = holeWidth;

        this.topPartStop = this.holeHeight - this.holeWidth;
        this.bottomPartBegin = this.holeHeight + this.holeWidth;

        this.x = 800;
    }

    public void tick() {
        this.x -= 10;
    }

    public boolean outOfBounds() {
        return 0 > this.x;
    }

    public void render(Graphics g) {
        g.setColor(Color.GREEN);
        renderCube(this.x - 5, 0, this.x + 5, this.topPartStop, g);
        g.setColor(Color.RED);
        renderCube(this.x - 5, this.sceneHeight, this.x + 5, this.bottomPartBegin, g);
    }

    public int[] getTopPart() {
        return new int[]{this.x - 5, 0, this.x + 5, this.topPartStop};
    }

    public int[] getBottomPart() {
        return new int[]{this.x - 5, this.sceneHeight, this.x + 5, this.bottomPartBegin};
    }

    public void renderCube(int x1, int y1, int x2, int y2, Graphics g) {
        int width = Math.abs(x2 - x1);
        int height = Math.abs(y2 - y1);

        int x = Math.min(x1, x2);
        int y = Math.min(y1, y2);

        g.fillRect(x, y, width, height);
    }

    public int getX() {
        return this.x;
    }

    public int getHoleHeight() {
        return this.holeHeight;
    }

    public int getTopPartStop() {
        return this.topPartStop;
    }

    public int getBottomPartBegin() {
        return this.bottomPartBegin;
    }

    public int getWidth() {
        return 10;
    }

    public int getGoalHeight() {
        return this.holeHeight;
    }
}
