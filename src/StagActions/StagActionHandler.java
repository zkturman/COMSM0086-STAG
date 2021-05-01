package StagActions;

import StagEntities.*;
import StagExceptions.StagException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * StagActionHandler's sole purpose is to execute an action in a location.
 */
public class StagActionHandler {

    StagPlayer commandPlayer;
    StagGenericAction gameActions;
    StagLocation currentLocation;
    String command;
    String foundTrigger;

    public StagActionHandler() {}

    public void interpretCommand() throws StagException {
        //get player name from command
        //search command for triggers
        while(!findAction()){

        }


        //execute command

    }

    private boolean findAction() throws StagException {
        //for each valid trigger, see if there are available subjects
        foundTrigger = findTrigger();
        return (foundTrigger != null && findSubject());
    }

    private String findTrigger() throws StagException {
        return null;
    }

    private boolean findSubject() throws StagException {
        HashMap<String, String> searchMap;

        //entities are unique, so put all in one hashmap
        searchMap = currentLocation.getArtefacts();
        searchMap.putAll(currentLocation.getCharacters());
        searchMap.putAll(currentLocation.getFurniture());
        searchMap.putAll(commandPlayer.getInventory());

        //also check the current location name

        return true;
    }

    private boolean findSubjectInInv(){
        return true;
    }
    public static void test() throws StagException{

    }


}
