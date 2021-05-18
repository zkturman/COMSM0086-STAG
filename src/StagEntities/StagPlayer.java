package StagEntities;

import java.util.HashMap;

public class StagPlayer {
    StagLocation currentLocation;
    HashMap<String, String> inventory;
    int health = 3;
    String name;
    public StagPlayer(String name){
        this.name = name;
        inventory = new HashMap<>();
    }

    public HashMap<String, String> getInventory() {
        return inventory;
    }

    public StagLocation getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(StagLocation currentLocation) {
        this.currentLocation = currentLocation;
    }

    public void addToInv(String key, String description){

    }

    public void reduceHealth() {}
    private void killPlayer() {}
    private void revivePlayer() {}
}
