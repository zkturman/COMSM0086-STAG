package StagCore;

import StagExceptions.StagCollectionException;
import StagExceptions.StagException;
import StagExceptions.StagNullPointerException;
import java.util.Collection;

public class StagUtility {

    public static void verifySize (Collection col, int size) throws StagException {
        StagUtility.checkNull(col);
        if (col.size() != size){
            throw new StagCollectionException();
        }
    }

    public static void checkNull(Object obj) throws StagException{
        if (obj == null){
            throw new StagNullPointerException();
        }
    }

    public static void test() {

    }
}
