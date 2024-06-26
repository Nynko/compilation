package compilateur.tds;

public class SymboleStruct extends SymboleVar {
    private SymboleStructContent struct;

    public SymboleStruct(SymboleStructContent struct, String name) {
        super(name);
        this.struct = struct;
    }

    public SymboleStructContent getStruct() {
        return this.struct;
    }

    public String getType() {
        return this.struct.getType() + "_*";
    }
}
