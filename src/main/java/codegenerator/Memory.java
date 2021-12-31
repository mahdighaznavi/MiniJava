package codegenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohammad hosein on 6/27/2015.
 */
public class Memory {
    private List<ThreeAddressCode> codeBlock;
    private int lastTempIndex;
    private int lastDataAddress;
    private static final int stratTempMemoryAddress = 500;
    private static final int stratDataMemoryAddress = 200;
    private static final int dataSize = 4;
    private static final int tempSize = 4;

    public Memory() {
        setCodeBlock(new ArrayList<ThreeAddressCode>());
        setLastTempIndex(getStratTempMemoryAddress());
        setLastDataAddress(getStratDataMemoryAddress());
    }

    public static int getStratTempMemoryAddress() {
        return stratTempMemoryAddress;
    }

    public static int getStratDataMemoryAddress() {
        return stratDataMemoryAddress;
    }

    public static int getDataSize() {
        return dataSize;
    }

    public static int getTempSize() {
        return tempSize;
    }

    public int getTemp() {
        setLastTempIndex(getLastTempIndex() + getTempSize());
        return getLastTempIndex() - getTempSize();
    }
    public  int getDateAddress(){
        setLastDataAddress(getLastDataAddress() + getDataSize());
        return getLastDataAddress() - getDataSize();
    }
    public int saveMemory() {
        getCodeBlock().add(new ThreeAddressCode());
        return getCodeBlock().size() - 1;
    }

    public void add3AddressCode(Operation op, Address opr1, Address opr2, Address opr3) {
        getCodeBlock().add(new ThreeAddressCode(op,opr1,opr2,opr3));
    }

    public void add3AddressCode(int i, Operation op, Address opr1, Address opr2, Address opr3) {
        getCodeBlock().remove(i);
        getCodeBlock().add(i, new ThreeAddressCode(op, opr1, opr2,opr3));
    }


    public int getCurrentCodeBlockAddress() {
        return getCodeBlock().size();
    }

    public void pintCodeBlock() {
        System.out.println("Code Block");
        for (int i = 0; i < getCodeBlock().size(); i++) {
            System.out.println(i + " : " + getCodeBlock().get(i).toString());
        }
    }

    public List<ThreeAddressCode> getCodeBlock() {
        return codeBlock;
    }

    public void setCodeBlock(List<ThreeAddressCode> codeBlock) {
        this.codeBlock = codeBlock;
    }

    public int getLastTempIndex() {
        return lastTempIndex;
    }

    public void setLastTempIndex(int lastTempIndex) {
        this.lastTempIndex = lastTempIndex;
    }

    public int getLastDataAddress() {
        return lastDataAddress;
    }

    public void setLastDataAddress(int lastDataAddress) {
        this.lastDataAddress = lastDataAddress;
    }
}

class ThreeAddressCode {
    private Operation operation;
    private Address Operand1;
    private Address Operand2;
    private Address Operand3;

    public ThreeAddressCode() {
        // Default constructor
        super();
    }

    public ThreeAddressCode(Operation op, Address opr1, Address opr2, Address opr3) {
        setOperation(op);
        setOperand1(opr1);
        setOperand2(opr2);
        setOperand3(opr3);
    }

    public String toString()
    {
        if(getOperation() == null) {
            return "";
        }
        StringBuffer res = new StringBuffer("(");
        res.append(getOperation().toString()).append(",");
        if(getOperand1() != null) {
            res.append(getOperand1().toString());
        }
        res.append(",");
        if(getOperand2() != null) {
            res.append(getOperand2().toString());
        }
        res.append(",");
        if(getOperand3() != null) {
            res.append(getOperand3().toString());
        }
        res.append(")");

        return res.toString();
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public Address getOperand1() {
        return Operand1;
    }

    public void setOperand1(Address operand1) {
        Operand1 = operand1;
    }

    public Address getOperand2() {
        return Operand2;
    }

    public void setOperand2(Address operand2) {
        Operand2 = operand2;
    }

    public Address getOperand3() {
        return Operand3;
    }

    public void setOperand3(Address operand3) {
        Operand3 = operand3;
    }
}
