package StagExceptions;

public abstract class StagException extends Exception{
    protected String errorMessage;

    public StagException(){}

    public StagException(String errorMessage){
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage(){
        return this.errorMessage;
    }
}
