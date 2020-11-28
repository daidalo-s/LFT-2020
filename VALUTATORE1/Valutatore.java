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
	    int expr_val;

    	// ... completare ...

    	expr_val = expr();
	    match(Tag.EOF);

        System.out.println(expr_val);

	    // ... completare ...
    }

    private int expr() { 
	    int term_val, exprp_val;

	    // ... completare ...

    	term_val = term();
	    exprp_val = exprp(term_val);

	    // ... completare ...
	    return exprp_val;
    }

    private int exprp(int exprp_i) {
	    int term_val, exprp_val;
	    switch (look.tag) {
	        case '+':
                match('+');
                term_val = term();
                exprp_val = exprp(exprp_i + term_val);
                break;

    	// ... completare ...
	    }
    }

    private int term() { 
	   // ... completare ...
    }
    
    private int termp(int termp_i) { 
	   // ... completare ...
    }
    
    private int fact() { 
	   // ... completare ...
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "...path..."; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Valutatore valutatore = new Valutatore(lex, br);
            valutatore.start();
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}