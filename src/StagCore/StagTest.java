package StagCore;

import StagActions.StagActionHandler;
import StagActions.StagBasicAction;
import StagActions.StagGenericAction;
import StagEntities.StagItemLinker;
import StagEntities.StagLocation;
import StagEntities.StagLocationConnector;
import StagEntities.StagLocationGenerator;
import StagExceptions.StagException;

public class StagTest {
    private StagEngine testEngine;

    public static void main(String[] args) {
        try {
            StagGenericAction.test();
            StagGraphParser.test();
            StagJSONParser.test();
            StagLocationGenerator.test();
            StagGame.test();
            StagItemLinker.test();
            StagBasicAction.test();
            StagTest e2eTesting = new StagTest();
            e2eTesting.blackBox();
            //TODO the following tests
            StagLocation.test();
            StagUtility.test();
            StagLocationConnector.test();
            StagActionHandler.test();

        }
        catch (StagException se){
            System.out.println("An exception was thrown during testing");
            se.printStackTrace();
        }
        System.out.println("Testing passed successfully.");
    }

    public void blackBox() throws StagException {
        StagGame testGame = new StagGame();
        testGame.generateLocations("src/extended-entities.dot");
        testGame.generateActions("src/extended-actions.json");
        testEngine = new StagEngine(testGame);

        //add players to the game
        testEngine.processMessage("player1: test");
        testEngine.processMessage("player2: test");
        assert testEngine.getReturnMessage().contains("This action cannot be performed.");

        testLook();
        testGoto();
        testHealth();
        testGetInv();
        testDrop();
        //testCustom
        //testSummon



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

    }

    private void testSummon() throws StagException{

    }
}
