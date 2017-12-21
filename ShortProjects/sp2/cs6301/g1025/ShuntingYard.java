/**
 * Shunting Yard Algorithm
 *
 * @author Antriksh
 * Ver 1.0: 2017/09/05
 * Ver 1.1: 2017/09/06: Added evaluation of postfix expressions
 */

package cs6301.g1025;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class ShuntingYard {
    private Stack<Character> stack = new Stack<Character>();
    private Queue<Character> q = new LinkedList<Character>();
    public String prefix = "";
    public String infix;

    public ShuntingYard() {
    }

    public String toString() {
        return prefix;
    }

    /**
     * Defining operator precedence
     * <p>
     * Parenthesized expressions (...)
     * Unary operator: factorial (!)
     * Exponentiation (^), right associative.
     * Product (*), division (/).  These operators are left associative.
     * Sum (+), and difference (-).  These operators are left associative.
     *
     * @param op: Operator
     * @return Integer value of precedence level.
     */
    int precedenceLevel(Character op) {
        switch (op) {
            case '+':
            case '-':
                return 0;
            case '*':
            case '/':
                return 1;
            case '^':
                return 2;
            case '!':
                return 3;
            case '(':
                return -1;
            default:
                throw new IllegalArgumentException("Operator unknown: " + op);
        }
    }

    /**
     * Defining Operator Associativity
     * <p>
     * Exponentiation (^), right associative.
     * Product (*), division (/).  These operators are left associative.
     * Sum (+), and difference (-).  These operators are left associative.
     *
     * @param op: Operator
     * @return String, "left" or "right" associative
     */
    String associativity(Character op) {
        switch (op) {
            case '+':
            case '-':
            case '*':
            case '/':
                return "left";
            case '^':
                return "right";
            default:
                throw new IllegalArgumentException("Operator unknown: " + op);
        }
    }

    /**
     * Operator Precedence Check
     *
     * @param top:   stack top item
     * @param token: token read from input
     * @return top has higher precedence than token: true or false
     */
    boolean precedent(Character top, Character token) {
        int precedenceTop = precedenceLevel(top);
        int precedenceToken = precedenceLevel(token);

        if (top == token) {
            if (associativity(top) == "left")
                return true;
            else
                return false;
        } else if (precedenceTop == precedenceToken) {
            if (associativity(top) == "left")
                return true;
            else
                return false;
        }
        return (precedenceTop > precedenceToken);
    }

    /**
     * Operator check
     * Check if the input token is an operator.
     *
     * @param token: input token
     * @return true or false
     */
    boolean isOperator(Character token) {
        switch (token) {
            case '+':
            case '-':
            case '*':
            case '/':
            case '^':
            case '!':
                return true;
            default:
                return false;
        }
    }

    /**
     * Shunting Yard Algorithm
     * Converts infix expressions to postfix expressions using the operator-precedence algorithm
     * or the Shunting-Yard algorithm.
     *
     * @param infix: Input infix expression.
     * @return String "postfix" expression.
     */
    public String shuntingYard(String infix) {
        prefix = "";
        for (int i = 0; i < infix.length(); i++) {
            Character token = (Character) infix.charAt(i);
            if (token == ' ')
                continue;
            if (token >= '0' && token <= '9') {
                q.add(token);
            } else if (token == '(') {
                stack.push(token);
            } else if (token == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    q.add(stack.pop());
                }
                if (stack.isEmpty())
                    throw new IllegalArgumentException("Parenthesis do not match Error");
                else if (stack.peek() == '(')
                    stack.pop();
            } else if (isOperator(token)) {
                if (!stack.isEmpty()) {
                    Character top = stack.peek();
                    if (precedent(top, token)) {
                        q.add(stack.pop());
                    }
                }
                stack.push(token);
            }
        }
        while (!stack.isEmpty() && isOperator(stack.peek())) {
            q.add(stack.pop());
        }
        for (Character s : q) {
            prefix += s.toString() + " ";
        }
        return prefix;
    }

    /**
     * Items Required
     * Returning items required to perform the operation using the input operator
     *
     * @param op: input operator
     * @return Integer 2/1
     * @throws IllegalArgumentException
     */
    Integer itemsRequired(Character op) {
        switch (op) {
            case '+':
            case '-':
            case '*':
            case '/':
            case '^':
                return 2;
            case '!':
                return 1;
            default:
                throw new IllegalArgumentException("Operator not found.");
        }
    }

    /**
     * Evaluate Operator
     * Performs operator input on two Integers
     *
     * @param op     input operator
     * @param first  first Integer popped
     * @param second second Integer popped
     * @return Integer after performing operation
     * @throws IllegalArgumentException
     */
    Integer evalOperator(Character op, Integer first, Integer second) {
        switch (op) {
            case '+':
                return first + second;
            case '-':
                return first - second;
            case '*':
                return first * second;
            case '/':
                return first / second;
            case '^':
                return (int) Math.pow(first, second);
            default:
                throw new IllegalArgumentException("Operator not found.");
        }
    }

    /**
     * Calculating factorial
     *
     * @param a input integer
     * @return Integer value of factorial
     */
    Integer factorial(Integer a) {
        Integer fact = 1;
        for (int i = 2; i < a; i++) {
            fact = fact * i;
        }
        return fact;
    }

    /**
     * Evaluates operators that require only single Integer
     *
     * @param op    Operator
     * @param first Integer
     * @return Integer value after performing operation
     * @throws IllegalArgumentException
     */
    Integer evalOperator(Character op, Integer first) {
        switch (op) {
            case '!':
                return factorial(first);
            default:
                throw new IllegalArgumentException("Operator not found.");
        }
    }

    /**
     * Evaluates postfix expressions using stack data structure
     *
     * @param postfix String postfix expression input
     * @return Integer value
     */
    public Integer evaluate(String postfix) {
        Stack<Integer> stack = new Stack<Integer>();

        for (int i = 0; i < postfix.length(); i++) {
            Character token = postfix.charAt(i);
            if (token == ' ')
                continue;
            if (token >= '0' && token <= '9') {
                Integer num = Character.getNumericValue(token);
                stack.push(num);
            } else if (isOperator(token)) {
                if (itemsRequired(token) == 2) {
                    Integer item1 = stack.pop();
                    Integer item2 = stack.pop();
                    stack.push(evalOperator(token, item1, item2));
                } else {
                    Integer item = stack.pop();
                    stack.push(evalOperator(token, item));
                }
            }
        }

        return stack.pop();
    }

    public static void main(String[] args) {
        ShuntingYard s = new ShuntingYard();
        System.out.println("Infix: 3 * 4 * 5 * 6");
        String postfix = s.shuntingYard("3 * 4 * 5 * 6");
        System.out.println("Postfix: " + postfix);
        System.out.println("Evaluation: " + s.evaluate(postfix));

        s = new ShuntingYard();
        System.out.println("Infix: 3 + 4 * 2 / ( 1 - 5 ) ^ 2 ^ 3");
        postfix = s.shuntingYard("3 + 4 * 2 / ( 1 - 5 ) ^ 2 ^ 3");
        System.out.println("Postfix: " + postfix);
        System.out.println("Evaluation: " + s.evaluate(postfix));

        s = new ShuntingYard();
        System.out.println("Infix: 3 + 4 * 2 * ( 5! ) + 2 / ( 1 - 5 ) ^ 2 ^ 3");
        postfix = s.shuntingYard("3 + 4 * 2 * ( 5! ) + 2 / ( 1 - 5 ) ^ 2 ^ 3");
        System.out.println("Postfix: " + postfix);
        System.out.println("Evaluation: " + s.evaluate(postfix));

        s = new ShuntingYard();
        System.out.println("Infix: 3 * 5^2 - 6^4 + ( 9! ) + 3!");
        postfix = s.shuntingYard("3 * 5^2 - 6^4 + ( 9! ) + 3!");
        System.out.println("Postfix: " + postfix);
        System.out.println("Evaluation: " + s.evaluate(postfix));
    }

}
