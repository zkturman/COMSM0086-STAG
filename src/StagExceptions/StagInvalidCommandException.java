package StagExceptions;

public class StagInvalidCommandException extends StagException {
    public StagInvalidCommandException(String errorMessage){
        super(errorMessage);
    }
}
