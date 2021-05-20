package StagActions;

import StagCore.StagGame;
import StagEntities.StagLocation;
import StagEntities.StagPlayer;
import StagExceptions.StagException;
import StagExceptions.StagActionSetupException;
import StagExceptions.StagNullActionException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Performs a custom action
 */
public class StagActionPerformer {
    StagGenericAction actionToPerform;
    String returnMessage;
    ArrayList<HashMap<String, String>> objectsMap;
    HashMap<String, StagLocation> locations;
    boolean newLocation;
    StagLocation currentLocation;
    private StagPlayer player;
    boolean playerDied;
    private StagGame game;

    public StagActionPerformer(StagGenericAction actionToPerform){
        this.actionToPerform = actionToPerform;
    }

    public String getReturnMessage() {
        if (returnMessage.equals("")){
            //something went wrong, but we still need to tell players something
            returnMessage = "An error occurred. The action wasn't performed.";
        }
        return returnMessage;
    }

    public boolean foundAction(){
        return (actionToPerform != null);
    }

    public void prepareAction(StagPlayer player, StagGame game) throws StagException {
        checkNull();
        //this is gross and I probably shouldn't do this
        this.player = player;
        this.game = game;
        objectsMap = new ArrayList<>();
        objectsMap.add(player.getInventory());
        objectsMap.add(player.getCurrentLocation().getArtefacts());
        objectsMap.add(player.getCurrentLocation().getFurniture());
        objectsMap.add(player.getCurrentLocation().getCharacters());

        locations = game.getGameLocations();
        newLocation = false;

        currentLocation = player.getCurrentLocation();
    }

    public void performAction() throws StagException {
        removeConsumed();
        addProduced();
        StringBuilder returnString = new StringBuilder();
        returnString.append(actionToPerform.getNarration());
        if (playerDied){
            returnString.append("\nYou died... back to the beginning for you.\n");
            StagBasicAction lookInfo = new StagBasicAction();
            lookInfo.stagLook(player);
            returnString.append(lookInfo.getReturnMessage());
        }
        returnMessage = returnString.toString();
    }

    private void removeConsumed() throws StagException {
        checkNull();
        boolean changed;
        HashSet<String> consumedObjects = actionToPerform.getConsumedObjects();
        //this by default ignores items not in the map, but need to add handling
        for (String key : consumedObjects){
            if (removeLocation(locations, key)){
                changed = true;
            }
            else if (key.equals("health")){
                removeHealth();
                changed = true;
            }
            else{
                changed = removeGameObject(key);
            }
            checkRemoved(changed);
        }
    }

    private boolean removeLocation(HashMap<String, StagLocation> map, String key){
        boolean removed = false;
        if (map.containsKey(key)) {
            removeItem(map, key);
            Set<String> gameLocations = locations.keySet();
            for(String locationName : gameLocations){
                StagLocation location = locations.get(locationName);
                removeItem(location.getNeighborLocations(), key);
            }
            removed = true;
        }
        return removed;
    }

    private void removeHealth(){
        int currentHealth = player.reduceHealth();
        if (currentHealth <= 0){
            dumpPlayerInv();
            player.setCurrentLocation(game.getStartLocation());
            player.revivePlayer();
            playerDied = true;
        }
    }

    private void dumpPlayerInv(){
        Set<String> inventory = player.getInventory().keySet();
        for(String artefact : inventory){
            //remove item from player inv and add to current location
            String description = player.getInventory().get(artefact);
            player.getCurrentLocation().getArtefacts().put(artefact, description);
        }
        player.getInventory().clear();
    }

    private boolean removeGameObject(String key){
        boolean removed = false;
        for(HashMap<String, String> map : objectsMap){
            if (removeItem(map, key)){
                removed = true;
            }
        }
        return removed;
    }

    private boolean removeItem(HashMap<String, ?> map, String key){
        if (map.containsKey(key)){
            map.remove(key);
            return true;
        }
        return false;
    }

    private void checkRemoved(boolean updated) throws StagException {
        if (!updated){
            throw new StagActionSetupException("Could not find objects to consume in action.");
        }
    }

    private void addProduced() throws StagException {
        checkNull();
        boolean changed;
        HashSet<String> producedObjects = actionToPerform.getProducedObjects();
        //remove objects from unplaced and add to current location
        for (String key : producedObjects) {
            if (locations.containsKey(key)) {
                addNewLocation(locations.get(key));
                changed = true;
            }
            else if (key.equals("health")){
                increaseHealth();
                changed = true;
            }
            else {
                changed = searchAndUpdate(key);
            }
            checkUpdate(changed);
        }
    }

    private void addNewLocation(StagLocation locationToConnect){
        currentLocation.addNeighbor(locationToConnect);
    }

    private void increaseHealth(){
        player.increaseHealth();
    }

    private boolean searchAndUpdate(String key){
        //if in locations already set to true
        boolean found = false;
        Set<String> gameLocations = locations.keySet();
        for (String locationName : gameLocations){
            StagLocation location = locations.get(locationName);
            if (checkForProduced(location, key)){
                updateItems(location, key);
                found = true;
            }
        }
        return found;
    }

    private boolean checkForProduced(StagLocation location, String key){
        return location.getArtefacts().containsKey(key)
                || location.getFurniture().containsKey(key) || location.getCharacters().containsKey(key);
    }

    private void updateItems(StagLocation location, String key){
        //this is okay because entities are unique
        if (location.getArtefacts().containsKey(key)){
            currentLocation.getArtefacts().put(key, location.getArtefacts().remove(key));
        }
        if (location.getFurniture().containsKey(key)){
            currentLocation.getFurniture().put(key, location.getFurniture().remove(key));
        }
        if (location.getCharacters().containsKey(key)) {
            currentLocation.getCharacters().put(key, location.getCharacters().remove(key));
        }
    }

    private void checkUpdate(boolean updated) throws StagActionSetupException {
        if (!updated){
            throw new StagActionSetupException(actionToPerform + " contained a produced item that doesn't exist in the game.");
        }
    }

    private void checkNull() throws StagException {
        String errorMessage = "Action to perform is null. Try foundAction method first.";
        if (actionToPerform == null){
            throw new StagNullActionException(errorMessage);
        }
    }
}
