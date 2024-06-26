package compilateur.ARMGenerator;

public class StringAggregator {

    private StringBuilder stringBuilder ;

    public StringAggregator(){
        stringBuilder = new StringBuilder();
    }

    public void appendString(String str){
        stringBuilder.append(str);
    }

    public void appendLine(String str){
        stringBuilder.append(str);
        stringBuilder.append("\n");
    }

    public void appendFormattedLine(String str, Object ...obj) {
        stringBuilder.append(String.format(str, obj));
        stringBuilder.append("\n");
    }

    public void appendLine(){
        stringBuilder.append("\n");
    }

    public void printString(){
        System.out.println(stringBuilder.toString());
    }
    
    public String getString(){
        return stringBuilder.toString();
    }
}
