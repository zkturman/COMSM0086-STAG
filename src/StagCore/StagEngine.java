package StagCore;

import StagEntities.StagPlayer;
import StagExceptions.StagException;

public class StagEngine {

    private StagGame currentGame;
    private String incomingCommand;
    private StagPlayer commandPlayer;
//    private String entityFile;
//    private String actionFile;
    private StagGraphParser entityBuilder;
    private StagJSONParser actionBuilder;



    public void processMessage(String message){}

    public void buildGame(String entityFile, String actionFile) throws StagException {
//        this.entityFile = entityFile;
//        this.actionFile = actionFile;
        currentGame = new StagGame();
        entityBuilder = new StagGraphParser(entityFile);
        actionBuilder = new StagJSONParser(actionFile);
        currentGame.setCustomActions(actionBuilder.generateActions());
        currentGame.setStartLocation(entityBuilder.generateLocations());
        currentGame.setUnplaced(entityBuilder.generateUnplaced());
    }
}
