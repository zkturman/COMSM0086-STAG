package StagActions;

import StagCore.StagGame;
import StagEntities.*;
import StagExceptions.StagException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * StagActionHandler's sole purpose is to find the right action to execute.
 */
public class StagActionHandler {

    private StagPlayer commandPlayer;
    private final ArrayList<StagGenericAction> gameActions;
    private final StagGame currentGame;
    private StagGenericAction actionToExecute;
    private StagLocation currentLocation;

    public void setCommandPlayer(StagPlayer commandPlayer) {
        this.commandPlayer = commandPlayer;
        currentLocation = commandPlayer.getCurrentLocation();
    }

    public StagActionHandler(StagGame game) {
        gameActions = game.getCustomActions();
        currentGame = game;
    }

     public String interpretCommand(String command) throws StagException {
        String returnMessage = "This action cannot be performed.";
        //check if basic command, then perform
        StagBasicAction basicAction = new StagBasicAction();
        if (basicAction.containsTrigger(command)){
            basicAction.performAction(commandPlayer);
            returnMessage = basicAction.getReturnMessage();
        }

        //search for custom command triggers and subjects
        ArrayList<StagGenericAction> customActions = findPossibleActions(command);
        determineAction(customActions);

        //execute custom command
        StagActionPerformer actionPerformer = new StagActionPerformer(actionToExecute);
        if(actionPerformer.foundAction()){
            actionPerformer.prepareAction(commandPlayer, currentGame);
            actionPerformer.performAction();
            returnMessage = actionPerformer.getReturnMessage();
        }

        //clear previous actions
        actionToExecute = null;

        //custom return messages would overwrite built-in ones
        return returnMessage;
    }

    private ArrayList<StagGenericAction> findPossibleActions(String command) {
        ArrayList<StagGenericAction> matchedActions = new ArrayList<>();
        //find actions with valid triggers and one subject
        for (StagGenericAction action : gameActions){
            if (isPossibleAction(action, command)){
                //set action if it can be executed
                matchedActions.add(action);
            }
        }
        return matchedActions;
    }

    private boolean isPossibleAction(StagGenericAction actionToTry, String command) {
        return actionToTry.canTryAction(command) && findSubject(command);
    }

    private boolean findSubject(String command){
        boolean found = false;
        Set<String> allSubjects = getMasterMap().keySet();
        for (String subject : allSubjects){
            Matcher matchSubject = createPattern(subject).matcher(command);
            if (matchSubject.find()) {
                found = true;
            }
        }
        return found;
    }

    private HashMap<String, String> getMasterMap(){
        HashMap<String, String> searchMap = new HashMap<>();

        //entities are unique, so put all in one hashmap
        searchMap.putAll(currentLocation.getArtefacts());
        searchMap.putAll(currentLocation.getCharacters());
        searchMap.putAll(currentLocation.getFurniture());
        searchMap.putAll(commandPlayer.getInventory());

        //also check the current location name
        StagLocation currentLocation = commandPlayer.getCurrentLocation();
        searchMap.put(currentLocation.getLocationName(), currentLocation.getDescription());

        return searchMap;
    }

    private Pattern createPattern(String trigger){
        return Pattern.compile("(\\s+|^)" + trigger + "(\\s+|$)");
    }

    private void determineAction(ArrayList<StagGenericAction> possibleActions) {
        for(StagGenericAction action : possibleActions){
            if (allSubjectPresent(action)){
                //if multiple actions can be performed, only the last one will work!
                actionToExecute = action;
            }
        }
    }

    private boolean allSubjectPresent(StagGenericAction actionToTest){
        boolean pass = true;
        for (String subject : actionToTest.getSubjectObjects()){
            //conveniently works for this situation too
            if (!findSubject(subject)){
                pass = false;
            }
        }
        return pass;
    }

    public static void test(String actionFile, String entityFile) throws StagException{
        StagGame testGame = new StagGame();
        testGame.generateActions(actionFile);
        testGame.generateLocations(entityFile);
        StagPlayer testPlayer = new StagPlayer("test");
        testPlayer.setCurrentLocation(testGame.getStartLocation());
        StagActionHandler testHandler = new StagActionHandler(testGame);
        testHandler.setCommandPlayer(testPlayer);

        //test findSubject
        assert testHandler.findSubject("door");
        assert !testHandler.findSubject("doora");
        assert testHandler.findSubject("open door");
        assert testHandler.findSubject("please door");
        assert testHandler.findSubject("open door please");
        assert testHandler.findSubject("     door      ");
        assert testHandler.findSubject(". door .");
        assert testHandler.findSubject("start");
        assert testHandler.findSubject("potion");
        assert !testHandler.findSubject("key");
    }
}
