package StagCore;

import StagActions.StagActionHandler;
import StagEntities.StagPlayer;
import StagExceptions.StagException;
import StagExceptions.StagMalformedCommandException;

import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The sole purpose of this class is to maintain game state
 */
public class StagEngine {

    private StagGame currentGame;
    private StagActionHandler actionHandler;
    private HashMap<String, StagPlayer> players;
    private String incomingCommand;
    private String returnMessage;

    public StagEngine(StagGame currentGame) {
        this.currentGame = currentGame;
        actionHandler = new StagActionHandler(this.currentGame);
        players = new HashMap<>();
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    public void processMessage(String message) throws StagException {
        //get command from message
        StagPlayer commandIssuer = findPlayer(message);
        //use a separate class to verify command has action, then execute
        actionHandler.setCommandPlayer(commandIssuer);
        //get valid command return message, otherwise StagException will print error.
        returnMessage = actionHandler.interpretCommand(incomingCommand);
    }

    private StagPlayer findPlayer(String message) throws StagException {
        Pattern playerPat = Pattern.compile("^.*:");
        Matcher playerFinder = playerPat.matcher(message);
        if (!playerFinder.find()){
            throw new StagMalformedCommandException("Player name not found.");
        }
        String playerName = playerFinder.group();
        incomingCommand = message.substring(playerFinder.end()).toLowerCase();
        //remove colon from player name
        playerName = playerName.substring(0, playerName.length() - 1);
        return getPlayer(playerName);
    }

    private StagPlayer getPlayer(String playerName){
        StagPlayer commandIssuer = players.get(playerName);
        if (isNewPlayer(commandIssuer)){
            commandIssuer = addPlayer(playerName);
        }
        return commandIssuer;
    }

    private boolean isNewPlayer(StagPlayer playerObject){
        return (playerObject == null);
    }

    public StagPlayer addPlayer(String playerName) {
        StagPlayer newPlayer = new StagPlayer(playerName);
        players.put(playerName, newPlayer);
        newPlayer.setCurrentLocation(currentGame.getStartLocation());
        return newPlayer;
    };
}
