// INTERPRETER.java
// Maximillian Kirchgesner
// ID: 27238431
// uciNetID: mkirchge

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintStream;
import java.util.ArrayList;

public class INTERPRETER {

    static String studentName = "Maximillian Kirchgesner";
    static String studentID = "27238431";
    static String uciNetID = "mkirchge";

    public static void main(String[] args) {
        INTERPRETER i = new INTERPRETER(args[0]);
        i.runProgram();
    }

    class SyntaxException extends Exception {
        public SyntaxException() {
        }
    }

    private LineNumberReader codeIn;
    private LineNumberReader inputIn;
    private FileOutputStream outFile;
    private PrintStream outStream;

    private static final int DATA_SEG_SIZE = 100;
    private ArrayList<String> C;
    private int[] D;
    private int PC;
    private String IR;
    private boolean run_bit;

    private int curIRIndex = 0;

    public INTERPRETER(String sourceFile) {
        try {
            inputIn = new LineNumberReader(new FileReader("input.txt"));
            inputIn.setLineNumber(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Init: Errors accessing input.txt");
            System.exit(-2);
        }
        try {
            outFile = new FileOutputStream(sourceFile + ".out");
            outStream = new PrintStream(outFile);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Init: Errors accessing " + sourceFile + ".out");
            System.exit(-2);
        }
        // Initialize the SIMPLESEM processor state
        try {
            // Initialize the Code segment
            C = new ArrayList<String>();
            codeIn = new LineNumberReader(new FileReader(sourceFile));
            codeIn.setLineNumber(1);
            while (codeIn.ready()) {
                C.add(codeIn.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Init: Errors accessing source file " + sourceFile);
            System.exit(-2);
        }
        // Initialize the Data segment
        D = new int[DATA_SEG_SIZE];
        for (int i = 0; i < DATA_SEG_SIZE; i++) {
            D[i] = 0;
        }
        PC = 0; // Every SIMPLESEM program begins at instruction 0
        IR = null;
        run_bit = true; // Enable the processor
    }

    public void runProgram() {
        // TODO FETCH-INCREMENT-EXECUTE CYCLE
        while (run_bit) {
            fetch();
            incrementPC();
            execute();
        }
        printDataSeg();
    }

    private void printDataSeg() {
        outStream.println("Data Segment Contents");
        for (int i = 0; i < DATA_SEG_SIZE; i++) {
            outStream.println(i + ": " + D[i]);
        }
    }

    private void fetch() {
        // The IR is updated with the instruction pointed at by PC
        // IR = C[PC]
        IR = C.get(PC);
        curIRIndex = 0;
    }

    private void incrementPC() {
        // The PC is incremented to *point* to the next instruction in C
        // PC = PC + 1
        PC++;
    }

    private void execute() {
        // The semantics of the instruction in the IR change the state of the SIMPLESEM processor
        try {
            parseStatement();
        } catch (SyntaxException se) {
            System.out.println(se);
        }
    }

    //Output: used in the case of: set write, source
    private void write(int source) {
        outStream.println(source);
    }

    //Input: used in the case of: set destination, read
    private int read() {
        int value = Integer.MIN_VALUE;
        try {
            value = new Integer((inputIn.readLine())).intValue();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * Checks and returns if the character c is found at the current
     * position in IR. If c is found, advance to the next
     * (non-whitespace) character.
     */
    private boolean accept(char c) {
        if (curIRIndex >= IR.length())
            return false;
        if (IR.charAt(curIRIndex) == c) {
            curIRIndex++;
            skipWhitespace();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks and returns if the string s is found at the current
     * position in IR. If s is found, advance to the next
     * (non-whitespace) character.
     */
    private boolean accept(String s) {
        if (curIRIndex >= IR.length())
            return false;
        if (curIRIndex + s.length() <= IR.length() && s.equals(IR.substring(curIRIndex, curIRIndex + s.length()))) {
            curIRIndex += s.length();
            skipWhitespace();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks if the character c is found at the current position in
     * IR. Throws a syntax error if c is not found at the current
     * position.
     */
    private void expect(char c) throws SyntaxException {
        if (!accept(c))
            throw new SyntaxException();
    }

    /**
     * Checks if the string s is found at the current position in
     * IR. Throws a syntax error if s is not found at the current
     * position.
     */
    private void expect(String s) throws SyntaxException {
        if (!accept(s))
            throw new SyntaxException();
    }

    private void skipWhitespace() {
        while (curIRIndex < IR.length() && Character.isWhitespace(IR.charAt(curIRIndex)))
            curIRIndex++;
    }

    private void parseStatement() throws SyntaxException {
        System.err.println("Statement");

        if (accept("halt")) {
            run_bit = false;
            return;
        } else if (accept("set"))
            parseSet();
        else if (accept("jumpt"))
            parseJumpt();
        else if (accept("jump"))
            parseJump();
    }

    private void parseSet() throws SyntaxException {
        System.err.println("Set");
        // set destination*, source**
        // D[destination] = source
        int set_lhs = 0;
        int set_rhs = 0;
        boolean to_write = false;

        if (!accept("write")) {
            set_lhs = parseExpr();
        } else {
            to_write = true;
        }

        expect(',');

        if (!accept("read")) {
            set_rhs = parseExpr();
            if (to_write == false) {
                D[set_lhs] = set_rhs;
            }
        } else {
            D[set_lhs] = read();
        }

        if (to_write) {
            write(set_rhs);
        }
    }

    private int parseExpr() throws SyntaxException {
        System.err.println("Expr");

        int expr_lhs = 0;
        int expr_rhs = 0;

        expr_lhs = parseTerm();

        while (true) {
            if (accept('+')){
                expr_rhs = parseTerm();
                expr_lhs = expr_lhs + expr_rhs;
            }
            else if (accept('-')){
                expr_rhs = parseTerm();
                expr_lhs = expr_lhs - expr_rhs;
            }
            else {
                break;
            }
        }

        return expr_lhs;
    }

    private int parseTerm() throws SyntaxException {
        System.err.println("Term");

        int term_lhs = 0;
        int term_rhs = 0;

        term_lhs = parseFactor();

        while (true) {
            if (accept('*')){
                term_rhs = parseTerm();
                term_lhs = term_lhs * term_rhs;
            }
            else if (accept('/')){
                term_rhs = parseTerm();
                term_lhs = term_lhs / term_rhs;
            }
            else if (accept('%')){
                term_rhs = parseTerm();
                term_lhs = term_lhs % term_rhs;
            }
            else {
                break;
            }
        }

        return term_lhs;
    }

    private int parseFactor() throws SyntaxException {
        System.err.println("Factor");

        int factor = 0;

        if (accept("D[")) {
            int s = parseExpr();
            expect(']');
            factor = D[s];
        } else if (accept('(')) {
            factor = parseExpr();
            expect(')');
        } else {
            factor = parseNumber();
        }
        return factor;
    }

    private int parseNumber() throws SyntaxException {
        System.err.println("Number");

        String num_string = "";

        if (curIRIndex >= IR.length())
            throw new SyntaxException();
        if (IR.charAt(curIRIndex) == '0') {
            curIRIndex++;
            skipWhitespace();
            return 0;
        } else if (Character.isDigit(IR.charAt(curIRIndex))) {
            while (curIRIndex < IR.length() && Character.isDigit(IR.charAt(curIRIndex))) {
                num_string += IR.charAt(curIRIndex);
                curIRIndex++;
            }
            skipWhitespace();

        } else {
            throw new SyntaxException();
        }
        int num = Integer.parseInt(num_string);
        return num;
    }

    private void parseJump() throws SyntaxException {
        System.err.println("Jump");
        //jump destination
        // PC = destination
        int destination = parseExpr();
        PC = destination;
    }

    //<Jumpt>-> jumpt <Expr>, <Expr> ( != | == | > | < | >= | <= ) <Expr>
    private void parseJumpt() throws SyntaxException {
        System.err.println("Jumpt");
        // if(boolean-condition)
        //     PC = destination

        // first parseExpr() is destination
        int destination = 0;
        destination = parseExpr();
        expect(',');
        // second parseExpr() is lhs
        int lhs = parseExpr();

        boolean change_PC = false;

        int rhs = 0;
        if (accept("!=")){
            rhs = parseExpr();
            if (lhs != rhs){
                change_PC = true;
            }
        }
        else if (accept("==")){
            rhs = parseExpr();
            if (lhs == rhs){
                change_PC = true;
            }
        }
        else if (accept(">=")){
            rhs = parseExpr();
            if (lhs >= rhs){
                change_PC = true;
            }
        }
        else if (accept("<=")){
            rhs = parseExpr();
            if (lhs <= rhs){
                change_PC = true;
            }
        }
        else if (accept(">")){
            rhs = parseExpr();
            if (lhs > rhs){
                change_PC = true;
            }
        }
        else if (accept("<")){
            rhs = parseExpr();
            if (lhs < rhs){
                change_PC = true;
            }
        } else {
            throw new SyntaxException();
        }

        if(change_PC){
            PC = destination;
        }
    }

}