package StagCore;

import StagExceptions.StagException;
import StagExceptions.StagNullPointerException;

public class StagUtility {

    public static void checkNull(Object obj) throws StagException{
        if (obj == null){
            throw new StagNullPointerException("Unexpected null object encountered.");
        }
    }
}
