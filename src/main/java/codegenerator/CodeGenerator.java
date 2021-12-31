package codegenerator;

import log.Log;
import errorhandler.ErrorHandler;
import scanner.token.Token;
import semantic.symbol.Symbol;
import semantic.symbol.SymbolTable;
import semantic.symbol.SymbolType;

import java.util.Stack;

/**
 * Created by Alireza on 6/27/2015.
 */
public class CodeGenerator {
    private Memory memory = new Memory();
    private Stack<Address> ss = new Stack<Address>();
    private Stack<String> symbolStack = new Stack<>();
    private Stack<String> callStack = new Stack<>();
    private SymbolTable symbolTable;

    public CodeGenerator() {
        setSymbolTable(new SymbolTable(getMemory()));
        //TODO
    }
    public void printMemory()
    {
        getMemory().pintCodeBlock();
    }
    public void semanticFunction(int func, Token next) {
        Log.print("codegenerator : " + func);
        switch (func) {
            case 0:
                return;
            case 1:
                checkID();
                break;
            case 2:
                pid(next);
                break;
            case 3:
                fpid();
                break;
            case 4:
                kpid(next);
                break;
            case 5:
                intpid(next);
                break;
            case 6:
                startCall();
                break;
            case 7:
                call();
                break;
            case 8:
                arg();
                break;
            case 9:
                assign();
                break;
            case 10:
                add();
                break;
            case 11:
                sub();
                break;
            case 12:
                mult();
                break;
            case 13:
                label();
                break;
            case 14:
                save();
                break;
            case 15:
                whileLoop();
                break;
            case 16:
                jpfSave();
                break;
            case 17:
                jpHere();
                break;
            case 18:
                print();
                break;
            case 19:
                equal();
                break;
            case 20:
                lessThan();
                break;
            case 21:
                and();
                break;
            case 22:
                not();
                break;
            case 23:
                defClass();
                break;
            case 24:
                defMethod();
                break;
            case 25:
                popClass();
                break;
            case 26:
                extend();
                break;
            case 27:
                defField();
                break;
            case 28:
                defVar();
                break;
            case 29:
                methodReturn();
                break;
            case 30:
                defParam();
                break;
            case 31:
                lastTypeBool();
                break;
            case 32:
                lastTypeInt();
                break;
            case 33:
                defMain();
                break;
        }
    }

    private void defMain() {
        //ss.pop();
        getMemory().add3AddressCode(getSs().pop().getNum(), Operation.JP, new Address(getMemory().getCurrentCodeBlockAddress(), VarType.Address), null, null);
        String methodName = "main";
        String className = getSymbolStack().pop();

        getSymbolTable().addMethod(className, methodName, getMemory().getCurrentCodeBlockAddress());

        getSymbolStack().push(className);
        getSymbolStack().push(methodName);
    }

    //    public void spid(Token next){
//        symbolStack.push(next.value);
//    }
    public void checkID() {
        getSymbolStack().pop();
        getSs().peek();//TODO : error
    }

    public void pid(Token next) {
        if (getSymbolStack().size() > 1) {
            String methodName = getSymbolStack().pop();
            String className = getSymbolStack().pop();
            try {

                Symbol s = getSymbolTable().get(className, methodName, next.getValue());
                VarType t = VarType.Int;
                switch (s.getType()) {
                    case Bool:
                        t = VarType.Bool;
                        break;
                    case Int:
                        t = VarType.Int;
                        break;
                }
                getSs().push(new Address(s.getAddress(), t));


            } catch (Exception e) {
                getSs().push(new Address(0, VarType.Non));
            }
            getSymbolStack().push(className);
            getSymbolStack().push(methodName);
        } else {
            getSs().push(new Address(0, VarType.Non));
        }
        getSymbolStack().push(next.getValue());
    }

    public void fpid() {
        getSs().pop();
        getSs().pop();

        Symbol s = getSymbolTable().get(getSymbolStack().pop(), getSymbolStack().pop());
        VarType t = VarType.Int;
        switch (s.getType()) {
            case Bool:
                t = VarType.Bool;
                break;
            case Int:
                t = VarType.Int;
                break;
        }
        getSs().push(new Address(s.getAddress(), t));

    }

    public void kpid(Token next) {
        getSs().push(getSymbolTable().get(next.getValue()));
    }

    public void intpid(Token next) {
        getSs().push(new Address(Integer.parseInt(next.getValue()), VarType.Int, TypeAddress.Imidiate));
    }

    public void startCall() {
        //TODO: method ok
        getSs().pop();
        getSs().pop();
        String methodName = getSymbolStack().pop();
        String className = getSymbolStack().pop();
        getSymbolTable().startCall(className, methodName);
        getCallStack().push(className);
        getCallStack().push(methodName);

        //symbolStack.push(methodName);
    }

    public void call() {
        //TODO: method ok
        String methodName = getCallStack().pop();
        String className = getCallStack().pop();
        try {
            getSymbolTable().getNextParam(className, methodName);
            ErrorHandler.printError("The few argument pass for method");
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
            VarType t = VarType.Int;
            switch (getSymbolTable().getMethodReturnType(className, methodName))
            {
                case Int:
                    t = VarType.Int;
                    break;
                case Bool:
                    t = VarType.Bool;
                    break;
            }
            Address temp = new Address(getMemory().getTemp(),t);
            getSs().push(temp);
            getMemory().add3AddressCode(Operation.ASSIGN, new Address(temp.getNum(), VarType.Address, TypeAddress.Imidiate), new Address(getSymbolTable().getMethodReturnAddress(className, methodName), VarType.Address), null);
            getMemory().add3AddressCode(Operation.ASSIGN, new Address(getMemory().getCurrentCodeBlockAddress() + 2, VarType.Address, TypeAddress.Imidiate), new Address(getSymbolTable().getMethodCallerAddress(className, methodName), VarType.Address), null);
            getMemory().add3AddressCode(Operation.JP, new Address(getSymbolTable().getMethodAddress(className, methodName), VarType.Address), null, null);

            //symbolStack.pop();


    }

    public void arg() {
        //TODO: method ok

        String methodName = getCallStack().pop();
//        String className = symbolStack.pop();
        try {
            Symbol s = getSymbolTable().getNextParam(getCallStack().peek(), methodName);
            VarType t = VarType.Int;
            switch (s.getType()) {
                case Bool:
                    t = VarType.Bool;
                    break;
                case Int:
                    t = VarType.Int;
                    break;
            }
            Address param = getSs().pop();
            if (param.getVarType() != t) {
                ErrorHandler.printError("The argument type isn't match");
            }
            getMemory().add3AddressCode(Operation.ASSIGN, param, new Address(s.getAddress(), t), null);

//        symbolStack.push(className);

        } catch (IndexOutOfBoundsException e) {
            ErrorHandler.printError("Too many arguments pass for method");
        }
        getCallStack().push(methodName);

    }

    public void assign() {

            Address s1 = getSs().pop();
            Address s2 = getSs().pop();
//        try {
            if (s1.getVarType() != s2.getVarType()) {
                ErrorHandler.printError("The type of operands in assign is different ");
            }
//        }catch (NullPointerException d)
//        {
//            d.printStackTrace();
//        }
            getMemory().add3AddressCode(Operation.ASSIGN, s1, s2, null);

    }

    public void add() {
        Address temp = new Address(getMemory().getTemp(), VarType.Int);
        Address s2 = getSs().pop();
        Address s1 = getSs().pop();

        if (s1.getVarType() != VarType.Int || s2.getVarType() != VarType.Int) {
            ErrorHandler.printError("In add two operands must be integer");
        }
        getMemory().add3AddressCode(Operation.ADD, s1, s2, temp);
        getSs().push(temp);
    }

    public void sub() {
        Address temp = new Address(getMemory().getTemp(), VarType.Int);
        Address s2 = getSs().pop();
        Address s1 = getSs().pop();
        if (s1.getVarType() != VarType.Int || s2.getVarType() != VarType.Int) {
            ErrorHandler.printError("In sub two operands must be integer");
        }
        getMemory().add3AddressCode(Operation.SUB, s1, s2, temp);
        getSs().push(temp);
    }

    public void mult() {
        Address temp = new Address(getMemory().getTemp(), VarType.Int);
        Address s2 = getSs().pop();
        Address s1 = getSs().pop();
        if (s1.getVarType() != VarType.Int || s2.getVarType() != VarType.Int) {
            ErrorHandler.printError("In mult two operands must be integer");
        }
        getMemory().add3AddressCode(Operation.MULT, s1, s2, temp);
//        memory.saveMemory();
        getSs().push(temp);
    }

    public void label() {
        getSs().push(new Address(getMemory().getCurrentCodeBlockAddress(), VarType.Address));
    }

    public void save() {
        getSs().push(new Address(getMemory().saveMemory(), VarType.Address));
    }

    public void whileLoop() {
        getMemory().add3AddressCode(getSs().pop().getNum(), Operation.JPF, getSs().pop(), new Address(getMemory().getCurrentCodeBlockAddress() + 1, VarType.Address), null);
        getMemory().add3AddressCode(Operation.JP, getSs().pop(), null, null);
    }

    public void jpfSave() {
        Address save = new Address(getMemory().saveMemory(), VarType.Address);
        getMemory().add3AddressCode(getSs().pop().getNum(), Operation.JPF, getSs().pop(), new Address(getMemory().getCurrentCodeBlockAddress(), VarType.Address), null);
        getSs().push(save);
    }

    public void jpHere() {
        getMemory().add3AddressCode(getSs().pop().getNum(), Operation.JP, new Address(getMemory().getCurrentCodeBlockAddress(), VarType.Address), null, null);
    }

    public void print() {
        getMemory().add3AddressCode(Operation.PRINT, getSs().pop(), null, null);
    }

    public void equal() {
        Address temp = new Address(getMemory().getTemp(), VarType.Bool);
        Address s2 = getSs().pop();
        Address s1 = getSs().pop();
        if (s1.getVarType() != s2.getVarType()) {
            ErrorHandler.printError("The type of operands in equal operator is different");
        }
        getMemory().add3AddressCode(Operation.EQ, s1, s2, temp);
        getSs().push(temp);
    }

    public void lessThan() {
        Address temp = new Address(getMemory().getTemp(), VarType.Bool);
        Address s2 = getSs().pop();
        Address s1 = getSs().pop();
        if (s1.getVarType() != VarType.Int || s2.getVarType() != VarType.Int) {
            ErrorHandler.printError("The type of operands in less than operator is different");
        }
        getMemory().add3AddressCode(Operation.LT, s1, s2, temp);
        getSs().push(temp);
    }

    public void and() {
        Address temp = new Address(getMemory().getTemp(), VarType.Bool);
        Address s2 = getSs().pop();
        Address s1 = getSs().pop();
        if (s1.getVarType() != VarType.Bool || s2.getVarType() != VarType.Bool) {
            ErrorHandler.printError("In and operator the operands must be boolean");
        }
        getMemory().add3AddressCode(Operation.AND, s1, s2, temp);
        getSs().push(temp);

    }

    public void not() {
        Address temp = new Address(getMemory().getTemp(), VarType.Bool);
        Address s2 = getSs().pop();
        Address s1 = getSs().pop();
        if (s1.getVarType() != VarType.Bool) {
            ErrorHandler.printError("In not operator the operand must be boolean");
        }
        getMemory().add3AddressCode(Operation.NOT, s1, s2, temp);
        getSs().push(temp);

    }

    public void defClass() {
        getSs().pop();
        getSymbolTable().addClass(getSymbolStack().peek());
    }

    public void defMethod() {
        getSs().pop();
        String methodName = getSymbolStack().pop();
        String className = getSymbolStack().pop();

        getSymbolTable().addMethod(className, methodName, getMemory().getCurrentCodeBlockAddress());

        getSymbolStack().push(className);
        getSymbolStack().push(methodName);

    }

    public void popClass() {
        getSymbolStack().pop();
    }

    public void extend() {
        getSs().pop();
        getSymbolTable().setSuperClass(getSymbolStack().pop(), getSymbolStack().peek());
    }

    public void defField() {
        getSs().pop();
        getSymbolTable().addField(getSymbolStack().pop(), getSymbolStack().peek());
    }

    public void defVar() {
        getSs().pop();

        String var = getSymbolStack().pop();
        String methodName = getSymbolStack().pop();
        String className = getSymbolStack().pop();

        getSymbolTable().addMethodLocalVariable(className, methodName, var);

        getSymbolStack().push(className);
        getSymbolStack().push(methodName);
    }

    public void methodReturn() {
        //TODO : call ok

        String methodName = getSymbolStack().pop();
        Address s = getSs().pop();
        SymbolType t = getSymbolTable().getMethodReturnType(getSymbolStack().peek(), methodName);
        VarType temp = VarType.Int;
        switch (t) {
            case Int:
                break;
            case Bool:
                temp = VarType.Bool;
        }
        if (s.getVarType() != temp) {
            ErrorHandler.printError("The type of method and return address was not match");
        }
        getMemory().add3AddressCode(Operation.ASSIGN, s, new Address(getSymbolTable().getMethodReturnAddress(getSymbolStack().peek(), methodName), VarType.Address, TypeAddress.Indirect), null);
        getMemory().add3AddressCode(Operation.JP, new Address(getSymbolTable().getMethodCallerAddress(getSymbolStack().peek(), methodName), VarType.Address), null, null);

        //symbolStack.pop();

    }

    public void defParam() {
        //TODO : call Ok
        getSs().pop();
        String param = getSymbolStack().pop();
        String methodName = getSymbolStack().pop();
        String className = getSymbolStack().pop();

        getSymbolTable().addMethodParameter(className, methodName, param);

        getSymbolStack().push(className);
        getSymbolStack().push(methodName);
    }

    public void lastTypeBool() {
        getSymbolTable().setLastType(SymbolType.Bool);
    }

    public void lastTypeInt() {
        getSymbolTable().setLastType(SymbolType.Int);
    }

    public Memory getMemory() {
        return memory;
    }

    public void setMemory(Memory memory) {
        this.memory = memory;
    }

    public Stack<Address> getSs() {
        return ss;
    }

    public void setSs(Stack<Address> ss) {
        this.ss = ss;
    }

    public Stack<String> getSymbolStack() {
        return symbolStack;
    }

    public void setSymbolStack(Stack<String> symbolStack) {
        this.symbolStack = symbolStack;
    }

    public Stack<String> getCallStack() {
        return callStack;
    }

    public void setCallStack(Stack<String> callStack) {
        this.callStack = callStack;
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public void setSymbolTable(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }
}
