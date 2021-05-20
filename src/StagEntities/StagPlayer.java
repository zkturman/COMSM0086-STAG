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
        currentLocation.getPlayers().put(name, this);
    }

    public void addToInv(String key, String description){
        inventory.put(key, description);
    }

    public void increaseHealth(){
        health++;
    }

    public int reduceHealth() {
        health--;
        return health;
    }

    public int getHealth(){
        return this.health;
    }

    public void revivePlayer() {
        health = 3;
    }
}
