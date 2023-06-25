package org.example.mcpvp.flappyGameTest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;

public class FlappyGame implements KeyListener {
    Frame frame = new Frame();
    Canvas canvas = new Canvas();
    boolean running = false;
    int currentTick = 0;
    int lastHeight = 200;
    ArrayList<FlappyObstacle> obstacles = new ArrayList<>();
    ArrayList<FlappyPlayer> players = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        FlappyGame game = new FlappyGame();
        game.start();
    }

    public void start() throws InterruptedException {
        canvas.setSize(800, 400);
        frame.setSize(800, 400);

        frame.add(canvas);
        frame.setVisible(true);
        Graphics g = canvas.getGraphics(); // CANVAS DURCH FRAME ERSETZEN WENN ES NICHT GEHT DU IDIOT
        this.running = true;

        frame.addKeyListener(new org.example.mcpvp.flappyGameTest.KeyListener());
        while (this.running) {
            this.tick();
            this.render(g);
            Thread.sleep(50);
        }

        frame.setVisible(false);
    }

    public void tick() {
        if (currentTick >= 500) {
            this.running = false;
            this.players.clear();
        }
        if (this.players.size() == 0) {
            this.running = false;
        }

        if (currentTick % 45 == 0) {
            addObstacle();
        }
        currentTick++;
        Iterator<FlappyObstacle> obstacleIterator = obstacles.iterator();
        while (obstacleIterator.hasNext()) {
            FlappyObstacle obstacle = obstacleIterator.next();
            obstacle.tick();
            if (obstacle.outOfBounds()) {
                obstacleIterator.remove();
            }
        }

        ArrayList<FlappyPlayer> playersToRemove = new ArrayList<>();

        for (FlappyPlayer player : players) {
            player.tick();
            if (player.dead) {
                playersToRemove.add(player);
            }
        }

        players.removeAll(playersToRemove);

    }

    public void addObstacle() {
        this.obstacles.add(new FlappyObstacle(random(80, 320), 75));
    }

    public FlappyPlayer addPlayer(boolean render) {
        FlappyPlayer player = new FlappyPlayer(this, render);
        this.players.add(player);
        return player;
    }

    public FlappyObstacle getNearestObstacle(FlappyPlayer player) {
        FlappyObstacle closest = null;
        Iterator<FlappyObstacle> iterator = this.obstacles.iterator();
        while (iterator.hasNext()) {
            FlappyObstacle obstacle = iterator.next();
            if (closest == null) {
                closest = obstacle;
            }

            if (distanceToPlayer(closest, player) > distanceToPlayer(obstacle, player)) {
                closest = obstacle;
            }
        }

        return closest;
    }

    public double distanceToPlayer(FlappyObstacle obstacle, FlappyPlayer p) {
        if (p.x >= obstacle.getX()) {
            return 100000;
        }
        return obstacle.getX() - p.x;
    }
    public void render(Graphics graphics) {
        graphics.clearRect(0, 0, 800, 400);
        graphics.setColor(Color.BLACK);
        graphics.drawString(String.valueOf(this.players.size()), 750, 30);

        for (FlappyObstacle obstacle : obstacles) {
            obstacle.render(graphics);
        }

        for (FlappyPlayer player : players) {
            player.render(graphics);
        }
    }

    public static int random(int min, int max) {
        return (int) (Math.random() * (max - min) + min);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            System.out.println("jump");
            for (FlappyPlayer player : this.players) {
                player.jump();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
