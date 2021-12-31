package semantic.symbol;

import codegenerator.VarType;

public class Int extends SymbolType {

    @Override
    public VarType getAssociatedVarType() {
        return VarType.Int;
    }
}
