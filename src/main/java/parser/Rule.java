package parser;

import scanner.token.Token;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohammad hosein on 6/25/2015.
 */
public class Rule {
    public Rule(String stringRule) {
        int index = stringRule.indexOf("#");
        if (index != -1) {
            try {
            setSemanticAction(Integer.parseInt(stringRule.substring(index + 1)));
            }catch (NumberFormatException ex){
                setSemanticAction(0);
            }
            stringRule = stringRule.substring(0, index);
        } else {
            setSemanticAction(0);
        }
        String[] splited = stringRule.split("->");
//        try {
            setLHS(NonTerminal.valueOf(splited[0]));
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        setRHS(new ArrayList<GrammarSymbol>());
        if (splited.length > 1) {
            String[] RHSs = splited[1].split(" ");
            for (String s : RHSs){
                try {
                    getRHS().add(new GrammarSymbol(NonTerminal.valueOf(s)));
                } catch (Exception e) {
//                    try{
                        getRHS().add(new GrammarSymbol(new Token(Token.getTyepFormString(s), s)));
//                    }catch (IllegalArgumentException d){
//                        d.printStackTrace();
//                        log.print(s);
//                    }
                }
            }
        }
    }
    private NonTerminal LHS;
    private List<GrammarSymbol> RHS;
    private int semanticAction;

    public NonTerminal getLHS() {
        return LHS;
    }

    public void setLHS(NonTerminal LHS) {
        this.LHS = LHS;
    }

    public List<GrammarSymbol> getRHS() {
        return RHS;
    }

    public void setRHS(List<GrammarSymbol> RHS) {
        this.RHS = RHS;
    }

    public int getSemanticAction() {
        return semanticAction;
    }

    public void setSemanticAction(int semanticAction) {
        this.semanticAction = semanticAction;
    }
}

class GrammarSymbol{
    private boolean isTerminal;
    private NonTerminal nonTerminal;
    private Token terminal;
    public GrammarSymbol(NonTerminal nonTerminal)
    {
        this.setNonTerminal(nonTerminal);
        isTerminal = false;
    }
    public GrammarSymbol(Token terminal)
    {
        this.setTerminal(terminal);
        isTerminal = true;
    }

    public boolean isTerminal() {
        return isTerminal;
    }

    public void setTerminal(boolean terminal) {
        isTerminal = terminal;
    }

    public NonTerminal getNonTerminal() {
        return nonTerminal;
    }

    public void setNonTerminal(NonTerminal nonTerminal) {
        this.nonTerminal = nonTerminal;
    }

    public Token getTerminal() {
        return terminal;
    }

    public void setTerminal(Token terminal) {
        this.terminal = terminal;
    }
}