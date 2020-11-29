import java.io.*; 

public class Valutatore {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Valutatore(Lexer l, BufferedReader br) { 
	   lex = l; 
	   pbr = br;
	   move(); 
    }
   
    void move() {
	    // come in Esercizio 3.1
        look = lex.lexical_scan(pbr);
        System.out.println("token = " + look);
    }

    void error(String s) { 
	    // come in Esercizio 3.1
        throw new Error("near line " + lex.line + ": " + s);
    }

    void match(int t) {
	    // come in Esercizio 3.1
        if (look.tag == t) {
           if (look.tag != Tag.EOF) move();
        } else error("syntax error");
    }
    /*
    <start> ::= <expr>EOF {print(expr.val)}
    <expr> ::= <term> {exprp.i = term.val}
               <exprp> {expr.val = exprp.val}
    <exprp> ::= +<term> {exprp1.i = exprp.i+term.val} <exprp1> {exprp.val = exprp1.val}
                -<term> {exprp1.i = exprp.i-term.val} <exprp1> {exprp.val = exprp1.val}
                eps {exprp.val = exprp.i}
    <term> ::= <fact> {termp.i = fact.val} <termp> {term.val = termp.val}
    <termp> ::= *<fact> {termp1.i = termp.i*fact.val} <termp1> {termp.val = termp1.val}
                /<fact> {termp1.i = termp.i/fact.val} <termp1> {termp.val = termp1.val}
                eps {termp.val = termp.i}
    <fact> ::= (<expr>) {fact.val = expr.val} | NUM {fact.val = NUM.value}
    */
    public void start() { 
	    int expr_val = 0;
        /*
        <start> ::= <expr>EOF {print(expr.val)}
        */
        switch(look.tag) {
            
            case '(':
               expr_val = expr();
               match(Tag.EOF);
               System.out.println(expr_val);
               break;
            
            case Tag.NUM:
               expr_val = expr();
               match(Tag.EOF);
               System.out.println(expr_val);
               break;

            default:
                error("Error found in start method"); 
        }
    }

    private int expr() { 
	    int term_val, exprp_val = 0;
        /*
        <expr> ::= <term> {exprp.i = term.val}
               <exprp> {expr.val = exprp.val}
        */
        switch(look.tag) {
            
            case '(':
                term_val = term();
                exprp_val = exprp(term_val);
                break;

            case Tag.NUM:
                term_val = term();
                exprp_val = exprp(term_val);
                break;

            default: 
                error("Error found in expr method");
        }

        return exprp_val;
    }

    private int exprp(int exprp_i) {
	    int term_val, exprp_val = 0;
        /*
        <exprp> ::= +<term> {exprp1.i = exprp.i+term.val} <exprp1> {exprp.val = exprp1.val}
                -<term> {exprp1.i = exprp.i-term.val} <exprp1> {exprp.val = exprp1.val}
                eps {exprp.val = exprp.i}
        */
        switch (look.tag) {
            
            case '+' : 
                match(Token.plus.tag);
                term_val = term();
                exprp_val = exprp(exprp_i + term_val);
                break;

            case '-':
                match(Token.minus.tag);
                term_val = term();
                exprp_val = exprp(exprp_i - term_val);
                break; 

            case ')':
                exprp_val = exprp_i;
                break;

            case Tag.EOF:
                exprp_val = exprp_i;
                break;

            default:
                error("Error found in exprp method");
        }
	    return exprp_val;
    }

    private int term() {
        int fact_val, termp_val = 0;
        /*
        <term> ::= <fact> {termp.i = fact.val} <termp> {term.val = termp.val}
        */
        switch(look.tag) {
            
            case '(':
                fact_val = fact();
                termp_val = termp(fact_val);
                break;

            case Tag.NUM:
                fact_val = fact();
                termp_val = termp(fact_val);
                break;

            default:
                error("Error found in term method");
        } 
        return termp_val;
    }
    
    private int termp(int termp_i) {
        int fact_val, termp_val = 0;
        /*
         <termp> ::= *<fact> {termp1.i = termp.i*fact.val} <termp1> {termp.val = termp1.val}
                /<fact> {termp1.i = termp.i/fact.val} <termp1> {termp.val = termp1.val}
                eps {termp.val = termp.i}
        */
        switch(look.tag){

            case '*':
                match(Token.mult.tag);
                fact_val = fact();
                termp_val = termp(termp_i * fact_val);
                break;

            case '/':
                match(Token.div.tag);
                fact_val = fact();
                termp_val = termp(termp_i / fact_val);
                break;

            case '+':
                termp_val = termp_i;
                break;

            case '-':
                termp_val = termp_i;
                break; 

            case ')':
                termp_val = termp_i;
                break;
                 
            case Tag.EOF:
                termp_val = termp_i;
                break; 

            default:
                error("Error found in termp method");
        }
        return termp_val;
    }
    
    private int fact() {
        int fact_val = 0;
        /*
        <fact> ::= (<expr>) {fact.val = expr.val} | NUM {fact.val = NUM.value}
        */
        switch(look.tag){

            case '(':
                match(Token.lpt.tag);
                fact_val = expr();
                match(Token.rpt.tag);
                break;

            case Tag.NUM:
                fact_val = Integer.parseInt(((NumberTok)look).number);
                match(Tag.NUM);
                break;

            default:
                error("Error found in fact method");
        }  
        return fact_val;	  
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "/Users/lorenzo/Projects /LFT-2020/VALUTATORE1/testvalutatore.txt"; 
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Valutatore valutatore = new Valutatore(lex, br);
            valutatore.start();
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}