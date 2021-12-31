package parser;

import scanner.token.Token;

import java.util.*;

/**
 * Created by mohammad hosein on 6/25/2015.
 */
public class ParseTable {
    private List<Map<Token,Action>> actionTable;
    private List<Map<NonTerminal,Integer>> gotoTable;
    public ParseTable(String jsonTable) throws Exception {
        jsonTable = jsonTable.substring(2,jsonTable.length()-2);
        String[] rows = jsonTable.split("\\],\\[");
        Map<Integer, Token> terminals = new HashMap<Integer, Token>();
        Map<Integer,NonTerminal> nonTerminals = new HashMap<Integer, NonTerminal>();
        rows[0] = rows[0].substring(1,rows[0].length()-1);
        String[] cols = rows[0].split("\",\"");
        for (int i = 1; i < cols.length; i++) {
            if(cols[i].startsWith("Goto")){
                String temp = cols[i].substring(5);
                try {
                    nonTerminals.put(i, NonTerminal.valueOf(temp));
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                terminals.put(i, new Token(Token.getTyepFormString(cols[i]), cols[i]));
            }
        }
        setActionTable(new ArrayList<Map<Token, Action>>());
        setGotoTable(new ArrayList<Map<NonTerminal, Integer>>());
        for (int i = 1; i <rows.length ; i++) {
            rows[i] = rows[i].substring(1,rows[i].length()-1);
            cols = rows[i].split("\",\"");
            getActionTable().add(new HashMap<Token, Action>());
            getGotoTable().add(new HashMap<>());
            for (int j = 1; j <cols.length ; j++) {
                if(!cols[j].equals(""))
                {
                    if(cols[j].equals("acc")){
                        getActionTable().get(getActionTable().size()-1).put(terminals.get(j),new Action(Act.accept,0));
                    }else
                    if(terminals.containsKey(j))
                    {
//                        try {
                        Token t = terminals.get(j);
                        Action a = new Action(cols[j].charAt(0) == 'r' ? Act.reduce : Act.shift, Integer.parseInt(cols[j].substring(1)));
                            getActionTable().get(getActionTable().size() - 1).put(t, a);
//                        }catch (StringIndexOutOfBoundsException e){
//                            e.printStackTrace();
//                        }
                    }
                    else if(nonTerminals.containsKey(j)){
                        getGotoTable().get(getGotoTable().size()-1).put(nonTerminals.get(j),Integer.parseInt(cols[j]));
                    }
                    else {
                        throw new Exception();
                    }
                }
            }
        }
    }

    public int getGotoTable(int currentState, NonTerminal variable )
    {
//        try {
            return getGotoTable().get(currentState).get(variable);
//        }catch (NullPointerException dd)
//        {
//            dd.printStackTrace();
//        }
//        return 0;
    }
    public Action getActionTable(int currentState ,Token terminal)
    {
        return getActionTable().get(currentState).get(terminal);
    }

    public List<Map<Token, Action>> getActionTable() {
        return actionTable;
    }

    public void setActionTable(List<Map<Token, Action>> actionTable) {
        this.actionTable = actionTable;
    }

    public List<Map<NonTerminal, Integer>> getGotoTable() {
        return gotoTable;
    }

    public void setGotoTable(List<Map<NonTerminal, Integer>> gotoTable) {
        this.gotoTable = gotoTable;
    }
}
