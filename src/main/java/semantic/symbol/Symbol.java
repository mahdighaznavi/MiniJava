package semantic.symbol;

/**
 * Created by mohammad hosein on 6/28/2015.
 */
public class Symbol{
    private SymbolType type;
    private int address;
    public Symbol(SymbolType type , int address)
    {
        this.setType(type);
        this.setAddress(address);
    }

    public SymbolType getType() {
        return type;
    }

    public void setType(SymbolType type) {
        this.type = type;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }
}
