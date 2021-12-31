package semantic.symbol;

import codegenerator.VarType;

public class Bool extends SymbolType {

    @Override
    public VarType getAssociatedVarType() {
        return VarType.Bool;
    }
}
