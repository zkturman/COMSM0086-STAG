package StagEntities;

import java.util.HashMap;

public class StagPlayer {
    StagLocation currentLocation;
    HashMap<String, String> inventory;
    int health = 3;

    public HashMap<String, String> getInventory() {
        return inventory;
    }

    public void reduceHealth() {}
    private void killPlayer() {}
    private void revivePlayer() {}
}
