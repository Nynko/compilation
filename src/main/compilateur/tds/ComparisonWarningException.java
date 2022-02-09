package compilateur.tds;

public class ComparisonWarningException extends SemanticWarningException {

    public ComparisonWarningException(int line, String leftType, String rightType) {
        super(line, "Comparison between '"+ leftType + "' and '" + rightType + "'");
    }

}
