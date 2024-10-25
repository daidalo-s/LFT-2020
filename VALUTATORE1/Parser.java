import java.io.*;

public class Parser {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Parser(Lexer l, BufferedReader br) {
        lex = l;
        pbr = br;
        move();
    }

    void move() {
        look = lex.lexical_scan(pbr);
        System.out.println("token = " + look);
    }

    void error(String s) {
	   throw new Error("near line " + lex.line + ": " + s);
    }

    void match(int t) {
	   if (look.tag == t) {
	       if (look.tag != Tag.EOF) move();
	   } else error("syntax error");
    }
    /*
    <start> ::= <expr>EOF
    <expr> ::= <term><exprp>
    <exprp> ::= +<term><exprp>
                 -<term><exprp>
                 eps
    <term> ::= <fact><termp>
    <termp> ::= *<fact><termp>
                /<fact><termp>
                eps
    <fact> ::= (<expr>)| NUM 
    */
    public void start() {

        switch(look.tag) {
            
            case '(':
               expr();
               match(Tag.EOF);
               break;
            
            case Tag.NUM:
	           expr();
	           match(Tag.EOF);
               break;

            default:
                error("Error found in start method"); 
        }
    }

    private void expr() {

        switch(look.tag) {
            
            case '(':
                term();
                exprp();
                break;

            case Tag.NUM:
                term();
                exprp();
                break;

            default: 
                error("Error found in expr method");
        }
    }

    private void exprp() {
	    
        switch (look.tag) {
            
            case '+' : 
                match(Token.plus.tag);
                term();
                exprp();
                break;

            case '-':
                match(Token.minus.tag);
                term();
                exprp();
                break; 

            case ')':
                break;

            case Tag.EOF:
                break;

            default:
                error("Error found in exprp method");
        }
    }

    private void term() {

        switch(look.tag) {
            
            case '(':
                fact();
                termp();
                break;

            case Tag.NUM:
                fact();
                termp();
                break;

            default:
                error("Error found in term method");

        }
    }

    private void termp() {

        switch(look.tag){

            case '*':
                match(Token.mult.tag);
                fact();
                termp();
                break;

            case '/':
                match(Token.div.tag);
                fact();
                termp();
                break;

            case '+':
                break;

            case '-':
                break; 

            case ')':
                break;
                 
            case Tag.EOF:
                break; 

            default:
                error("Error found in termp method");
        }
    }

    private void fact() {

        switch(look.tag){

            case '(':
                match(Token.lpt.tag);
                expr();
                match(Token.rpt.tag);
                break;

            case Tag.NUM:
                match(Tag.NUM);
                break;

            default:
                error("Error found in fact method");
        }
    }
		
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "/Users/lorenzo/Projects /LFT-2020/PARSER1/testparser.txt"; 
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser parser = new Parser(lex, br);
            parser.start();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}