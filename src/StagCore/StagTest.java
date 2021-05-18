package StagCore;

import StagActions.StagActionHandler;
import StagActions.StagActionPerformer;
import StagActions.StagBasicAction;
import StagActions.StagGenericAction;
import StagEntities.StagItemLinker;
import StagEntities.StagLocation;
import StagEntities.StagLocationConnector;
import StagEntities.StagLocationGenerator;
import StagExceptions.StagException;

public class StagTest {
    public static void main(String[] args) {
        try {
            StagGenericAction.test();
            StagGraphParser.test();
            StagJSONParser.test();
            StagLocationGenerator.test();
            StagGame.test();
            StagItemLinker.test();
            StagBasicAction.test();
            //TODO the following tests
            StagLocation.test();
            StagUtility.test();
            StagLocationConnector.test();
            StagActionHandler.test();
            StagActionPerformer.test();

        }
        catch (StagException se){
            System.out.println("An exception was thrown during testing");
            se.printStackTrace();
        }
        System.out.println("Testing passed successfully.");
    }
}
