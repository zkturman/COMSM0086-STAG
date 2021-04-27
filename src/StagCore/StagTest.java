package StagCore;

import StagActions.StagGenericAction;
import StagExceptions.StagException;

public class StagTest {
    public static void main(String[] args) {
        try {
            StagGenericAction.test();
        }
        catch (StagException se){
            System.out.println("An exception was thrown during testing");
            se.printStackTrace();
        }
    }
}
