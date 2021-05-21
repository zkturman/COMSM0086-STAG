package StagEntities;

import java.util.HashMap;

/**
 * The sole purpose of this class is to maintain the state of a player
 */
public class StagPlayer {

    private StagLocation currentLocation;
    private final HashMap<String, String> inventory;
    private int health = 3;
    private final String name;

    public StagPlayer(String name){
        this.name = name;
        inventory = new HashMap<>();
    }

    public StagLocation getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(StagLocation currentLocation) {
        this.currentLocation = currentLocation;
        currentLocation.getPlayers().put(name, this);
    }

    public HashMap<String, String> getInventory() {
        return inventory;
    }

    public void addToInv(String key, String description){
        inventory.put(key, description);
    }

    public int getHealth(){
        return this.health;
    }

    public void increaseHealth(){
        health++;
    }

    public int reduceHealth() {
        health--;
        return health;
    }

    public void revivePlayer() {
        health = 3;
    }
}
