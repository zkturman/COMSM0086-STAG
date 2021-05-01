package StagCore;

import StagActions.StagActionHandler;
import StagExceptions.StagException;

/**
 * The sole purpose of this class is to maintain game state
 */
public class StagEngine {

    private StagGame currentGame;
    private String incomingCommand;

    public StagEngine() {}

    public void processMessage(String message){
        StagActionHandler actionInterpreter = new StagActionHandler();
    }

    public void setCurrentGame(StagGame currentGame) {
        this.currentGame = currentGame;
    }

    public void addPlayer() { };
}
