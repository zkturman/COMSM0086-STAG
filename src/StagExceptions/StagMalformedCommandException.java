package StagExceptions;

public class StagMalformedCommandException extends StagException{
    public StagMalformedCommandException(String errorMessage){
        super(errorMessage);
    }
    @Override
    public String getErrorString() {
        return this.errorMessage;
    }
}
