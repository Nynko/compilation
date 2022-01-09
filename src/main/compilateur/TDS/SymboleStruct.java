package compilateur.TDS;

public class SymboleStruct extends SymboleVar {
    private SymboleStructContent struct;

    public SymboleStruct(SymboleStructContent struct, String name) {
        super(name);
        this.struct = struct;
    }

    public SymboleStructContent getStruct() {
        return this.struct;
    }

    // @Override
    // public boolean isInitalized() {
    //     for (Symbole s : this.struct.getTds().getListeSymboles().values()) {
    //         if (s instanceof SymboleInt sv && !sv.isInitalized()) {
    //             return false;
    //         }
    //     }
    //     return true;
    // }
}
