package StagCore;

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
            //TODO the following tests
            StagLocation.test();
            StagUtility.test();
            StagLocationConnector.test();

        }
        catch (StagException se){
            System.out.println("An exception was thrown during testing");
            se.printStackTrace();
        }
        System.out.println("Testing passed successfully.");
    }
}
