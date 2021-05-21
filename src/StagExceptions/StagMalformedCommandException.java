package StagExceptions;

public class StagMalformedCommandException extends StagException{

    public StagMalformedCommandException(String errorMessage){
        super(errorMessage);
    }
}
