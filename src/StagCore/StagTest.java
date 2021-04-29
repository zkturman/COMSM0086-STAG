package StagCore;

import StagActions.StagGenericAction;
import StagEntities.StagItemLinker;
import StagEntities.StagLocation;
import StagEntities.StagLocationGenerator;
import StagExceptions.StagException;

public class StagTest {
    public static void main(String[] args) {
        try {
            StagGenericAction.test();
            StagGraphParser.test();
            //TODO the following tests
            StagJSONParser.test();
            StagLocationGenerator.test();
            StagGame.test();
            StagItemLinker.test();
            StagLocation.test();
            StagUtility.test();

        }
        catch (StagException se){
            System.out.println("An exception was thrown during testing");
            se.printStackTrace();
        }
    }
}
