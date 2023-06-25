package org.example.mcpvp.mcworld;

import java.util.ArrayList;
import java.util.Arrays;

public class McWorld {
    private final ArrayList<McPlayer> players = new ArrayList<>();
    public McWorld() {

    }

    public ArrayList<McPlayer> getPlayers() {
        return this.players;
    }

    public void tick() {
        for (McPlayer player : this.players) {
            player.tick();
        }
    }

    public void init() {

    }

    public void addPlayer(McPlayer player) {
        this.players.add(player);
    }
}
