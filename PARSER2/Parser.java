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

    public void prog() {
	    /*
        =,print,read,cond,while,{
        */    
        switch(look.tag) {
            //<statlist>EOF
            case '=':
                statlist();
                match(Tag.EOF);
                break;
            //<statlist>EOF
            case Tag.PRINT:
                statlist();
                match(Tag.EOF);
                break;
            //<statlist>EOF    
            case Tag.READ:
                statlist();
                match(Tag.EOF);
                break;
            //<statlist>EOF    p
            case Tag.COND:
                statlist();
                match(Tag.EOF);
                break;
            //<statlist>EOF    
            case Tag.WHILE:
                statlist();
                match(Tag.EOF);
                break;
            //<statlist>EOF    
            case '{':
                statlist();
                match(Tag.EOF);
                break;
            
            default:
                error("Error found in prog method");
        }
    }

    private void statlist() {
	   /*
       =,print,read,cond,while,{
       */ 
       switch(look.tag) {
            //<stat><statlistp>
            case '=':
                stat();
                statlistp();
                break;
            //<stat><statlistp>
            case Tag.PRINT:
                stat();
                statlistp();
                break;
            //<stat><statlistp>    
            case Tag.READ:
                stat();
                statlistp();
                break;
            //<stat><statlistp>    
            case Tag.COND:
                stat();
                statlistp();
                break;
            //<stat><statlistp>    
            case Tag.WHILE:
                stat();
                statlistp();
                break;
            //<stat><statlistp>    
            case '{':
                stat();
                statlistp();
                break;

            default:
                error("Error found in startlist method"); 
       }
    }

    private void statlistp() {
	   /*
       ;
       $,} 
       */
       switch(look.tag) {
            // ; <stat><statlistp>
            case ';':
                match(Token.semicolon.tag);
                stat();
                statlistp();
                break;
            //epsilon    
            case Tag.EOF: 
                break;
            //epsilon    
            case '}':
                break;
            
            default:
                error("Error found in startlistp method");

        }
    }

    private void stat() {
        /*
        =
        print
        read
        cond
        while
        { 
        */
        switch(look.tag) {
            //= ID <expr>
            case '=':
                match(Token.assign.tag);
                match(Tag.ID);
                expr();
                break;
            //print (<exprlist>)    
            case Tag.PRINT:
                match(Tag.PRINT);
                match(Token.lpt.tag);
                exprlist();
                match(Token.rpt.tag);
                break;
            //read (ID)    
            case Tag.READ:
                match(Tag.READ);
                match(Token.lpt.tag);
                match(Tag.ID);
                match(Token.rpt.tag);
                break;
            //cond <whenlist> else <stat>    
            case Tag.COND:
                match(Tag.COND);
                whenlist();
                match(Tag.ELSE);
                stat();
                break;
            //while (<bexpr>) <stat>    
            case Tag.WHILE:
                match(Tag.WHILE);
                match(Token.lpt.tag);
                bexpr();
                match(Token.rpt.tag);
                stat();
                break;
            //{<statlist>}    
            case '{':
                match(Token.lpg.tag);
                statlist();
                match(Token.rpg.tag);
                break;

            default:
                error("Error found in stat method");
        }
    }

    private void whenlist() {
        /*
        when
        */
        switch(look.tag) {
            //<whenitem><whenlistp>
            case Tag.WHEN:
                whenitem();
                whenlistp();
                break;

            default:
                error("Error found in whenlist method");
        }
    }

    private void whenlistp() {
        /*
        when
        else
        */
        switch(look.tag) {
            //<whenitem><whenlistp>
            case Tag.WHEN:
                whenitem();
                whenlistp();
                break;
            //epsilon
            case Tag.ELSE:
                break;

            default:
                error("Error found in whenlistp method");
        }
    }

    private void whenitem() {
        /*
        when
        */
        switch(look.tag) {
            //when ( <bexpr> ) do <stat>
            case Tag.WHEN:
                match(Tag.WHEN);
                match(Token.lpt.tag);
                bexpr();
                match(Token.rpt.tag);
                match(Tag.DO);
                stat();
                break;

            default:
                error("Error found in whenitem method");
        }
    }
	
    private void bexpr() {
        /*
        RELOP
        */
        switch(look.tag) {
            //RELOP <expr><expr>
            case Tag.RELOP:
                match(Tag.RELOP);
                expr();
                expr();
                break;

            default:
                error("Error found in bexpr method");
        }
    }	

    private void expr() {
        /*
        +
        -
        *
        /
        NUM
        ID
        */
        switch(look.tag) {
            //+ ( <exprlist> )
            case '+':
                match(Token.plus.tag);
                match(Token.lpt.tag);
                exprlist();
                match(Token.rpt.tag);
                break;
            //- <expr><expr>
            case '-':
                match(Token.minus.tag);
                expr();
                expr();
                break;
            //* ( <exprlist> )
            case '*':
                match(Token.mult.tag);
                match(Token.lpt.tag);
                exprlist();
                match(Token.rpt.tag);
                break;
            // / <expr><expr>
            case '/':
                match(Token.div.tag);
                expr();
                expr();
                break;
            //NUM
            case Tag.NUM:
                match(Tag.NUM);
                break;
            //ID
            case Tag.ID:
                match(Tag.ID);
                break;

            default:
                error("Error found in expr method");

        }
    }

    private void exprlist() {
        /*
        +,-,*,/,NUM,ID
        */
        switch(look.tag) {
            //<expr><exprlistp>
            case '+':
                expr();
                exprlistp();
                break;
            //<expr><exprlistp>    
            case '-':
                expr();
                exprlistp();
                break;
            //<expr><exprlistp>    
            case '*':
                expr();
                exprlistp();
                break;
            //<expr><exprlistp>
            case '/':
                expr();
                exprlistp();
                break;
            //<expr><exprlistp>
            case Tag.NUM:
                expr();
                exprlistp();
                break;
            //<expr><exprlistp>
            case Tag.ID:
                expr();
                exprlistp();
                break;

            default:
                error("Error found in exprlist method");
        }
    }

    private void exprlistp() {
        /*
        +,-,*,/,NUM,ID
        )
        */
        switch(look.tag) {
            //<expr><exprlistp>
            case '+':
                expr();
                exprlistp();
                break;
            //<expr><exprlistp>    
            case '-':
                expr();
                exprlistp();
                break;
            //<expr><exprlistp>    
            case '*':
                expr();
                exprlistp();
                break;
            //<expr><exprlistp>    
            case '/':
                expr();
                exprlistp();
                break;
            //<expr><exprlistp>
            case Tag.NUM:
                expr();
                exprlistp();
                break;
            //<expr><exprlistp>
            case Tag.ID:
                expr();
                exprlistp();
                break;
            //epsilon
            case ')':
                break;
            
            default:
                error("Error found in exprlistp method");
        }
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "/Users/lorenzo/Projects /LFT-2020/PARSER2/testparser.txt"; 
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser parser = new Parser(lex, br);
            parser.prog();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}