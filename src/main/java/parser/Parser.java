package parser;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import log.Log;
import codegenerator.CodeGenerator;
import errorhandler.ErrorHandler;
import scanner.LexicalAnalyzer;
import scanner.token.Token;


public class Parser {
  private List<Rule> rules;
  private Stack<Integer> parsStack;
  private ParseTable parseTable;
  private CodeGenerator cg;

  public Parser() {
    setParsStack(new Stack<Integer>());
    getParsStack().push(0);
    try {
      setParseTable(new ParseTable(Files.readAllLines(Paths.get("src/main/resources/parseTable")).get(0)));
    } catch (Exception e) {
      e.printStackTrace();
    }
    setRules(new ArrayList<Rule>());
    try {
      for (String stringRule : Files.readAllLines(Paths.get("src/main/resources/Rules"))) {
        getRules().add(new Rule(stringRule));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    setCg(new CodeGenerator());
  }

  public void startParse(java.util.Scanner sc) {
    LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(sc);
    Token lookAhead = lexicalAnalyzer.getNextToken();
    boolean finish = false;
    Action currentAction;
    while (!finish) {
      try {
        Log.print(/*"lookahead : "+*/ lookAhead.toString() + "\t" + getParsStack().peek());
//                log.print("state : "+ parsStack.peek());
        currentAction = getParseTable().getActionTable(getParsStack().peek(), lookAhead);
        Log.print(currentAction.toString());
        //log.print("");

        switch (currentAction.getAction()) {
          case shift:
            getParsStack().push(currentAction.getNumber());
            lookAhead = lexicalAnalyzer.getNextToken();

            break;
          case reduce:
            Rule rule = getRules().get(currentAction.getNumber());
            for (int i = 0; i < rule.getRHS().size(); i++) {
              getParsStack().pop();
            }

            Log.print(/*"state : " +*/ getParsStack().peek() + "\t" + rule.getLHS());
//                        log.print("LHS : "+rule.LHS);
            getParsStack().push(getParseTable().getGotoTable(getParsStack().peek(), rule.getLHS()));
            Log.print(/*"new State : " + */getParsStack().peek() + "");
//                        log.print("");
            try {
              getCg().semanticFunction(rule.getSemanticAction(), lookAhead);
            } catch (Exception e) {
              Log.print("Code Genetator Error");
            }
            break;
          case accept:
            finish = true;
            break;
        }
        Log.print("");

      } catch (Exception ignored) {

        ignored.printStackTrace();
//                boolean find = false;
//                for (NonTerminal t : NonTerminal.values()) {
//                    if (parseTable.getGotoTable(parsStack.peek(), t) != -1) {
//                        find = true;
//                        parsStack.push(parseTable.getGotoTable(parsStack.peek(), t));
//                        StringBuilder tokenFollow = new StringBuilder();
//                        tokenFollow.append(String.format("|(?<%s>%s)", t.name(), t.pattern));
//                        Matcher matcher = Pattern.compile(tokenFollow.substring(1)).matcher(lookAhead.toString());
//                        while (!matcher.find()) {
//                            lookAhead = lexicalAnalyzer.getNextToken();
//                        }
//                    }
//                }
//                if (!find)
//                    parsStack.pop();
      }


    }
    if (!ErrorHandler.isHasError()) {
      getCg().printMemory();
    }

  }


  public List<Rule> getRules() {
    return rules;
  }

  public void setRules(List<Rule> rules) {
    this.rules = rules;
  }

  public Stack<Integer> getParsStack() {
    return parsStack;
  }

  public void setParsStack(Stack<Integer> parsStack) {
    this.parsStack = parsStack;
  }

  public ParseTable getParseTable() {
    return parseTable;
  }

  public void setParseTable(ParseTable parseTable) {
    this.parseTable = parseTable;
  }

  public CodeGenerator getCg() {
    return cg;
  }

  public void setCg(CodeGenerator cg) {
    this.cg = cg;
  }
}
