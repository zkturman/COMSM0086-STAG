package StagCore;

import StagActions.StagActionHandler;
import StagActions.StagBasicAction;
import StagActions.StagGenericAction;
import StagEntities.StagItemLinker;
import StagEntities.StagLocationGenerator;
import StagExceptions.StagException;

/**
 * The sole purpose of this class is to run testing for the program.
 */
public class StagTest {

    private StagEngine testEngine;

    public static void main(String[] args) {
        String basicActionFile = "src/basic-actions.json";
        String basicEntityFile = "src/basic-entities.dot";
        String extendedActionFile = "src/extended-actions.json";
        String extendedEntityFile = "src/extended-entities.dot";

        try {
            StagGame.test(basicActionFile, basicEntityFile);

            //location-related testing
            StagGraphParser.test(basicEntityFile);
            StagLocationGenerator.test(basicEntityFile);
            StagItemLinker.test();

            //action-related testing
            StagJSONParser.test(basicActionFile);
            StagActionHandler.test(basicActionFile, basicEntityFile);
            StagGenericAction.test();
            StagBasicAction.test();

            //full testing of commands
            StagTest e2eTesting = new StagTest();
            e2eTesting.blackBox(extendedActionFile, extendedEntityFile);

        }
        catch (StagException se){
            System.out.println("An exception was thrown during testing");
            se.printStackTrace();
        }
        System.out.println("Testing passed successfully.");
    }

    public void blackBox(String actionFile, String entityFile) throws StagException {
        //setup game and engine
        StagGame testGame = new StagGame();
        testGame.generateLocations(entityFile);
        testGame.generateActions(actionFile);
        testEngine = new StagEngine(testGame);

        //add players to the game
        testEngine.processMessage("player1: test");
        testEngine.processMessage("player2: test");
        assert testEngine.getReturnMessage().contains("This action cannot be performed.");

        //test various player-entered actions
        testLook();
        testGoto();
        testHealth();
        testGetInv();
        testDrop();
        testCustom();
        testSummon();
    }

    private void testLook() throws StagException {
        //test look functionality
        testEngine.processMessage("player1: look");
        assert testEngine.getReturnMessage().contains("The player player2");
        assert testEngine.getReturnMessage().contains("potion");
        assert testEngine.getReturnMessage().contains("A bottle of magic potion");
        assert !testEngine.getReturnMessage().contains("cellar");

        testEngine.processMessage("player1: LOOK");
        assert testEngine.getReturnMessage().contains("The player player2");
        assert testEngine.getReturnMessage().contains("potion");
        assert testEngine.getReturnMessage().contains("A bottle of magic potion");
        assert !testEngine.getReturnMessage().contains("cellar");

        testEngine.processMessage("player1:      look     ");
        assert testEngine.getReturnMessage().contains("The player player2");
        assert testEngine.getReturnMessage().contains("potion");
        assert testEngine.getReturnMessage().contains("A bottle of magic potion");
        assert !testEngine.getReturnMessage().contains("cellar");

        testEngine.processMessage("player1:look");
        assert testEngine.getReturnMessage().contains("The player player2");
        assert testEngine.getReturnMessage().contains("potion");
        assert testEngine.getReturnMessage().contains("A bottle of magic potion");
        assert !testEngine.getReturnMessage().contains("cellar");

        testEngine.processMessage("player1: please look");
        assert testEngine.getReturnMessage().contains("The player player2");
        assert testEngine.getReturnMessage().contains("potion");
        assert testEngine.getReturnMessage().contains("A bottle of magic potion");
        assert !testEngine.getReturnMessage().contains("cellar");

        testEngine.processMessage("player1: please look here");
        assert testEngine.getReturnMessage().contains("The player player2");
        assert testEngine.getReturnMessage().contains("potion");
        assert testEngine.getReturnMessage().contains("A bottle of magic potion");
        assert !testEngine.getReturnMessage().contains("cellar");
    }

    private void testGoto() throws StagException {
        testEngine.processMessage("player2: goto forest");
        assert testEngine.getReturnMessage().contains("A deep dark forest");
        assert testEngine.getReturnMessage().contains("cabin");
        testEngine.processMessage("player2: goto riverbank");
        assert testEngine.getReturnMessage().contains("A grassy riverbank");
        assert !testEngine.getReturnMessage().contains("cabin");
        testEngine.processMessage("player2: goto forest");
        testEngine.processMessage("player2: goto cabin");
    }

    private void testHealth() throws StagException {
        testEngine.processMessage("player2: health");
        assert testEngine.getReturnMessage().contains("Current health: 3.");

        //setup so health can be lost
        testEngine.processMessage("player2: goto forest");
        testEngine.processMessage("player2: get key");
        testEngine.processMessage("player2: goto cabin");
        testEngine.processMessage("player2: open trapdoor");
        testEngine.processMessage("player2: goto cellar");

        //check health loss and death
        testEngine.processMessage("player2: attack elf");
        assert testEngine.getReturnMessage().contains("he fights back and you lose some health");
        testEngine.processMessage("player2: health");
        assert testEngine.getReturnMessage().contains("Current health: 2.");
        testEngine.processMessage("player2: attack elf");
        testEngine.processMessage("player2: health");
        assert testEngine.getReturnMessage().contains("Current health: 1.");
        testEngine.processMessage("player2: goto cabin");
        testEngine.processMessage("player2: get potion");
        testEngine.processMessage("player2: get axe");
        testEngine.processMessage("player2: goto cellar");
        testEngine.processMessage("player2: drink potion");
        testEngine.processMessage("player2: health");
        assert testEngine.getReturnMessage().contains("Current health: 2.");
        testEngine.processMessage("player2: attack elf");
        testEngine.processMessage("player2: attack elf");
        testEngine.processMessage("player2: look");
        assert testEngine.getReturnMessage().contains("A log cabin in the woods");
        testEngine.processMessage("player2: inv");
        assert !testEngine.getReturnMessage().contains("axe");
        testEngine.processMessage("player2: goto cellar");
        assert testEngine.getReturnMessage().contains("axe");
    }

    private void testGetInv() throws StagException {
        testEngine.processMessage("player2: get");
        assert testEngine.getReturnMessage().contains("You couldn't pick this up.");
        testEngine.processMessage("player2: get axe");
        assert testEngine.getReturnMessage().contains("You gained the axe.");
        testEngine.processMessage("player2: inv");
        assert testEngine.getReturnMessage().contains("axe");
        assert testEngine.getReturnMessage().contains("A razor sharp axe");
        testEngine.processMessage("player2: inventory");
        assert testEngine.getReturnMessage().contains("axe");
        assert testEngine.getReturnMessage().contains("A razor sharp axe");

        //make sure axe is no longer in location
        testEngine.processMessage("player2: look");
        assert !testEngine.getReturnMessage().contains("axe");
    }

    private void testDrop() throws StagException {
        testEngine.processMessage("player2: drop");
        assert testEngine.getReturnMessage().contains("You couldn't drop this item.");
        testEngine.processMessage("player2: drop axe");
        testEngine.processMessage("player2: look");
        assert testEngine.getReturnMessage().contains("axe");
        testEngine.processMessage("player2: inv");
        assert !testEngine.getReturnMessage().contains("axe");
    }

    private void testCustom() throws StagException{
        testEngine.processMessage("player2: get axe");
        testEngine.processMessage("player2: goto cabin");
        testEngine.processMessage("player2: goto forest");
        testEngine.processMessage("player2: chop tree");

        //check narration
        assert testEngine.getReturnMessage().contains("You cut down the tree with the axe");

        //check location for produced object
        testEngine.processMessage("player2: look");
        assert testEngine.getReturnMessage().contains("A heavy wooden log");
        assert !testEngine.getReturnMessage().contains("A tall pine tree");

        //check that produced object is an artefact and that other players can see it
        testEngine.processMessage("player1: goto forest");
        assert testEngine.getReturnMessage().contains("A heavy wooden log");
        assert !testEngine.getReturnMessage().contains("A tall pine tree");
        testEngine.processMessage("player1: get log");
        assert testEngine.getReturnMessage().contains("You gained the log");
    }

    private void testSummon() throws StagException{
        testEngine.processMessage("player1: goto riverbank");
        testEngine.processMessage("player1: get horn");
        assert testEngine.getReturnMessage().contains("You gained the horn");
        testEngine.processMessage("player1: blow horn");
        assert testEngine.getReturnMessage().contains("as if by magic, a lumberjack appears");
        testEngine.processMessage("player1: look");
        assert testEngine.getReturnMessage().contains("lumberjack");
        testEngine.processMessage("player1: goto forest");
        testEngine.processMessage("player1: look");
        assert !testEngine.getReturnMessage().contains("lumberjack");
        testEngine.processMessage("player1: blow horn");
        assert testEngine.getReturnMessage().contains("as if by magic, a lumberjack appears");
        testEngine.processMessage("player1: look");
        assert testEngine.getReturnMessage().contains("lumberjack");
        testEngine.processMessage("player1: goto cabin");

        //check case sensitivity of custom commands
        testEngine.processMessage("player1: Blow horn");
        assert testEngine.getReturnMessage().contains("This action cannot be performed");
        testEngine.processMessage("player1: look");
        assert !testEngine.getReturnMessage().contains("lumberjack");

        //check case sensitivity of custom commands
        testEngine.processMessage("player1: BLOW horn");
        assert testEngine.getReturnMessage().contains("This action cannot be performed");
        testEngine.processMessage("player1: look");
        assert !testEngine.getReturnMessage().contains("lumberjack");

        testEngine.processMessage("player1: blow Horn");
        assert testEngine.getReturnMessage().contains("This action cannot be performed");
        testEngine.processMessage("player1: look");
        assert !testEngine.getReturnMessage().contains("lumberjack");

        testEngine.processMessage("player1: blow HORN");
        assert testEngine.getReturnMessage().contains("This action cannot be performed");
        testEngine.processMessage("player1: look");
        assert !testEngine.getReturnMessage().contains("lumberjack");

        //check spacing works
        testEngine.processMessage("player1:     blow       horn     ");
        assert testEngine.getReturnMessage().contains("as if by magic, a lumberjack appears");
        testEngine.processMessage("player1: look");
        assert testEngine.getReturnMessage().contains("lumberjack");

        //check additional words
        testEngine.processMessage("player1: goto cellar");
        testEngine.processMessage("player1: please blow horn now");
        assert testEngine.getReturnMessage().contains("as if by magic, a lumberjack appears");
        testEngine.processMessage("player1: look");
        assert testEngine.getReturnMessage().contains("lumberjack");
    }
}
