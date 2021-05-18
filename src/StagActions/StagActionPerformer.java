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
    StagLocation unplaced;
    ArrayList<HashMap<String, String>> unplacedObjectsMap;
    boolean newLocation;
    StagLocation currentLocation;

    public StagActionPerformer(StagGenericAction actionToPerform){
        this.actionToPerform = actionToPerform;
    }

    public String getReturnMessage() {
        if (returnMessage == null){
            //something went wrong, but we still need to tell players something
            returnMessage = "An error occurred. This action cannot be performed.";
        }
        return returnMessage;
    }

    public boolean foundAction(){
        return (actionToPerform != null);
    }

    public void prepareAction(StagPlayer player, StagGame game) throws StagException {
        checkNull();
        //this is gross and I probably shouldn't do this
        objectsMap = new ArrayList<>();
        objectsMap.add(player.getInventory());
        objectsMap.add(player.getCurrentLocation().getArtefacts());
        objectsMap.add(player.getCurrentLocation().getFurniture());
        objectsMap.add(player.getCurrentLocation().getCharacters());

        unplaced = game.getUnplacedLocation();
        unplacedObjectsMap = new ArrayList<>();
        unplacedObjectsMap.add(unplaced.getArtefacts());
        unplacedObjectsMap.add(unplaced.getFurniture());
        unplacedObjectsMap.add(unplaced.getCharacters());
        newLocation = false;

        currentLocation = player.getCurrentLocation();
    }

    public void performAction() throws StagException {
        removeConsumed();
        addProduced();
        returnMessage = actionToPerform.getNarration();
    }

    private void removeConsumed() throws StagException {
        checkNull();
        HashSet<String> consumedObjects = actionToPerform.getConsumedObjects();
        //this by default ignores items not in the map, but need to add handling
        for (String key : consumedObjects){
            checkAndRemove(key);
        }
    }

    private void checkAndRemove(String key) throws StagException {
        if (!removeGameObject(key) && !removeLocation(locations, key)){
            throw new StagActionSetupException("Could not find objects to consume in action.");
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

    private void addProduced() throws StagException {
        checkNull();
        HashSet<String> producedObjects = actionToPerform.getProducedObjects();
        //all things except locations should be in unplaced or a location
        checkValidUnplaced(producedObjects);
        //remove objects from unplaced and add to current location
        for (String key : producedObjects) {
            if (locations.containsKey(key)) {
                addNewLocation(locations.get(key));
            } else {
                updateItems(key);
            }
        }
    }

    private void checkValidUnplaced(HashSet<String> producedObjects)throws StagException {
        for (String key : producedObjects) {
            if (!unplacedHasKey(key)) {
                throw new StagActionSetupException("Game is missing produced location or produced objects in unplaced.");
            }
        }
    }

    private boolean unplacedHasKey(String key){
        //if in locations already set to true
        boolean found = locations.containsKey(key);
        newLocation = found;
        for (HashMap<String, String> map : unplacedObjectsMap){
            if (map.containsKey(key)){
                found = true;
            }
        }
        return found;
    }

    private void addNewLocation(StagLocation locationToConnect){
        currentLocation.addNeighbor(locationToConnect);
    }

    private void updateItems(String key){
        if (unplaced.getArtefacts().containsKey(key)){
            currentLocation.getArtefacts().put(key, unplaced.getArtefacts().remove(key));
        }
        if (unplaced.getFurniture().containsKey(key)){
            currentLocation.getFurniture().put(key, unplaced.getFurniture().remove(key));
        }
        if (unplaced.getCharacters().containsKey(key)) {
            currentLocation.getCharacters().put(key, unplaced.getCharacters().remove(key));
        }
    }

    private void checkNull() throws StagException {
        String errorMessage = "Action to perform is null. Try foundAction method first.";
        if (actionToPerform == null){
            throw new StagNullActionException(errorMessage);
        }
    }

    public static void test(){

    }
}
