package compilateur.utils;
import java.util.ArrayList;


public class ErrorAggregator {
    private ArrayList<CompilerErrorException> errors = new ArrayList<CompilerErrorException>();

    public void addError(CompilerErrorException e) {
        this.errors.add(e);
    }

    public void printErrors() {
        for(CompilerErrorException err : this.errors) {
            System.out.println(String.format("Line %d : %s",err.getLine(), err.getMessage()));
        }
    }

    public boolean noError() {
        return this.errors.size() == 0;
    }
}
