package parser;

public class Action {
  private Act action;
  //if action = shift : number is state
  //if action = reduce : number is number of rule
  private int number;

  public Action(Act action, int number) {
    this.setAction(action);
    this.setNumber(number);
  }

  public String toString() {
    switch (getAction()) {
      case accept:
        return "acc";
      case shift:
        return "s" + getNumber();
      case reduce:
        return "r" + getNumber();
    }
    return getAction().toString() + getNumber();
  }

  public Act getAction() {
    return action;
  }

  public void setAction(Act action) {
    this.action = action;
  }

  public int getNumber() {
    return number;
  }

  public void setNumber(int number) {
    this.number = number;
  }
}

enum Act {
  shift,
  reduce,
  accept
}
