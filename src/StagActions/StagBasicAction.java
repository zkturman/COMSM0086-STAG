package StagActions;

import StagEntities.StagLocation;
import StagEntities.StagPlayer;
import StagExceptions.StagException;
import StagExceptions.StagMalformedCommandException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is responsible for performing built-in actions. Class
 * uses Reflection, so some methods are used even if marked unused.
 */
@SuppressWarnings("unused")
public class StagBasicAction {

    private final HashMap<String, String> actionMapping;
    private String actionToBePerformed;
    private String returnMessage;
    private String incomingCommand;

    public StagBasicAction(){
        actionMapping = new HashMap<>();
        actionMapping.put("look", "stagLook");
        actionMapping.put("goto", "stagGoto");
        actionMapping.put("inv", "stagInventory");
        actionMapping.put("inventory", "stagInventory");
        actionMapping.put("get", "stagGet");
        actionMapping.put("drop", "stagDrop");
        actionMapping.put("health", "stagHealth");
        returnMessage = "";
    }

    public String getReturnMessage(){
        return this.returnMessage;
    }

    public void performAction(StagPlayer player) throws StagException {
        String methodName = actionMapping.get(actionToBePerformed);
        //Use reflection to perform the basic commands based on actionMapping
        try{
            Method actionMethod = this.getClass().getDeclaredMethod(methodName, StagPlayer.class);
            actionMethod.invoke(this, player);
        }
        catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException exception){
            exception.printStackTrace();
            throw new StagMalformedCommandException("An invalid basic command was attempted.");
        }
    }

    public void stagLook(StagPlayer commandPlayer){
        StringBuilder returnString = new StringBuilder(returnMessage);
        StagLocation playerLocation = commandPlayer.getCurrentLocation();
        //current location description
        returnString.append(playerLocation.getDescription());

        returnString.append("\nHere you see the following: \n");
        //object descriptions
        entityBuilder(playerLocation.getFurniture(), returnString);
        entityBuilder(playerLocation.getCharacters(), returnString);
        entityBuilder(playerLocation.getArtefacts(), returnString);
        Set<String> players = playerLocation.getPlayers().keySet();
        for (String player : players){
            if (commandPlayer != playerLocation.getPlayers().get(player)){
                returnString.append("The player ");
                returnString.append(player);
                returnString.append("\n");
            }
        }

        //connected locations
        returnString.append("Places you can travel to: \n");
        connectionBuilder(playerLocation.getNeighborLocations(), returnString);
        returnMessage = returnString.toString();
    }

    private void entityBuilder(HashMap<String, String> gameObjects, StringBuilder returnString){
        Set<String> entities = gameObjects.keySet();
        for (String key : entities){
            returnString.append(key);
            returnString.append(", ");
            //include description
            returnString.append(gameObjects.get(key));
            returnString.append("\n");
        }
    }

    private void connectionBuilder(HashMap<String, StagLocation> neighbors, StringBuilder returnString){
        for (String location : neighbors.keySet()){
            returnString.append(location);
            returnString.append("\n");
        }
    }

    public void stagGoto(StagPlayer player){
        //moves player to a new location if possible
        StagLocation newLocation = checkGoto(player.getCurrentLocation().getNeighborLocations());
        if (newLocation != null){
            player.setCurrentLocation(newLocation);
        }
        else{
            returnMessage = "Could not go to specified location.\n";
        }
        stagLook(player);
    }

    private StagLocation checkGoto(HashMap<String, StagLocation> neighbors){
        Set<String> locationNames =neighbors.keySet();
        StagLocation newLocation = null;
        for (String neighbor : locationNames){
            Matcher matchLocation = createPattern(neighbor).matcher(incomingCommand);
            if (matchLocation.find()){
                newLocation = neighbors.get(neighbor);
            }
        }
        return newLocation;
    }

    public void stagInventory(StagPlayer player){
        //lists artefacts in players inventory
        StringBuilder returnString = new StringBuilder();
        entityBuilder(player.getInventory(), returnString);
        returnMessage = returnString.toString();
    }

    public void stagGet(StagPlayer player){
        //adds specified artefact to players inventory
        HashMap<String, String> locationGoodies = player.getCurrentLocation().getArtefacts();
        String artefactToGet = canGetDrop(locationGoodies);
        if (artefactToGet != null){
            String artefactDesc = player.getCurrentLocation().getArtefacts().remove(artefactToGet);
            player.addToInv(artefactToGet, artefactDesc);
            returnMessage = "You gained the " + artefactToGet + ".\n";
        }
        else{
            returnMessage = "You couldn't pick this up.";
        }
    }

    public void stagDrop(StagPlayer player){
        //removes specified artefact to players inventory and adds it to location
        HashMap<String, String> inventory = player.getInventory();
        String artefactToDrop = canGetDrop(inventory);
        if (artefactToDrop != null){
            String artefactDesc = player.getInventory().remove(artefactToDrop);
            player.getCurrentLocation().getArtefacts().put(artefactToDrop, artefactDesc);
            returnMessage = "You dropped the " + artefactToDrop + ".\n";
        }
        else{
            returnMessage = "You couldn't drop this item.";
        }
    }

    private String canGetDrop(HashMap<String, String> artefacts){
        Set<String> artefactNames = artefacts.keySet();
        String foundArtefact = null;
        for (String artefact : artefactNames){
            Matcher matchArtefact = createPattern(artefact).matcher(incomingCommand);
            //If multiple artefacts are mentions, only the last valid one will be affected
            if (matchArtefact.find()){
                foundArtefact = artefact;
            }
        }
        return foundArtefact;
    }

    public void stagHealth(StagPlayer player){
        returnMessage = "Current health: " + player.getHealth() + ".\n";
    }

    public boolean containsTrigger(String incomingMessage) {
        Set<String> triggerSet = actionMapping.keySet();
        boolean found = false;
        for(String trigger : triggerSet){
            Matcher matchTrigger = createPattern(trigger).matcher(incomingMessage);
            if (matchTrigger.find()) {
                actionToBePerformed = incomingMessage.substring(matchTrigger.start(), matchTrigger.end())
                        .trim().toLowerCase();
                found = true;
                incomingCommand = incomingMessage;
            }
        }
        return found;
    }

    private Pattern createPattern(String trigger){
        return Pattern.compile("(\\s+|^)" + trigger + "(\\s+|$)", Pattern.CASE_INSENSITIVE);
    }

    @SuppressWarnings("AssertWithSideEffects")
    public static void test() {
        StagBasicAction testAction = new StagBasicAction();
        //causes side effects, but for the purposes of this test, it's fine
        assert testAction.containsTrigger("goto");
        assert testAction.containsTrigger("   goto");
        assert testAction.containsTrigger("goto   ");
        assert testAction.containsTrigger("   goto   ");
        assert testAction.containsTrigger("please goto this location");
        assert testAction.containsTrigger("goto this location");
        assert !testAction.containsTrigger("   goto;");
        assert testAction.containsTrigger("look");
        assert testAction.containsTrigger("inv");
        assert testAction.containsTrigger("inventory");
        assert testAction.containsTrigger("goto");
        assert testAction.containsTrigger("goto");
        assert testAction.containsTrigger("goto");
    }
}
