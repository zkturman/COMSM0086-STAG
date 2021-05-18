package StagExceptions;

public abstract class StagException extends Exception{
    protected String errorMessage;

    public StagException(){};

    public StagException(String errorMessage){};

    public String getErrorString(){
        return this.errorMessage;
    }
}
