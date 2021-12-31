package codegenerator;

/**
 * Created by mohammad hosein on 6/28/2015.
 */
public class Address {
    private int num;
    private TypeAddress Type;
    private VarType varType;

    public Address(int num, VarType varType, TypeAddress type) {
        this.setNum(num);
        this.setType(type);
        this.setVarType(varType);
    }

    public Address(int num, VarType varType) {
        this.setNum(num);
        this.setType(TypeAddress.Direct);
        this.setVarType(varType);
    }
    public String toString(){
        switch (getType()){
            case Direct:
                return getNum() +"";
            case Indirect:
                return "@"+ getNum();
            case Imidiate:
                return "#"+ getNum();
        }
        return getNum() +"";
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public TypeAddress getType() {
        return Type;
    }

    public void setType(TypeAddress type) {
        Type = type;
    }

    public VarType getVarType() {
        return varType;
    }

    public void setVarType(VarType varType) {
        this.varType = varType;
    }
}
