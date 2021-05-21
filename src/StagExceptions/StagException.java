package StagExceptions;

/**
 * The sole purpose of StagExceptions are to report errors encountered when processing files and commands.
 * These are specific to STAG. Child classes are only added to provide additional information on the error.
 */
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
