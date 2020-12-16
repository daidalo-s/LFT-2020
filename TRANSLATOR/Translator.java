import java.io.*;
//import java.lang.Parser;

public class Translator {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;
    
    SymbolTable st = new SymbolTable();
    CodeGenerator code = new CodeGenerator();
    int count=0;

    public Translator(Lexer l, BufferedReader br) {
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
    <prog> ::= <statlist> EOF
    <statlist> ::= <stat> <statlistp>
    <statlistp> ::= ; <stat> <statlistp> | eps
    <stat> ::= = ID <expr>
            |  print ( <exprlist> )
            |  read (ID) 
            |  cond <whenlist> else <stat>
            |  while ( <bexpr> ) <stat>
            |  { <statlist> }
    <whenlist> ::= <whenitem> <whenlistp>
    <whenlistp> ::= <whenitem> <whenlistp> | eps
    <whenitem> ::= when ( <bexpr> ) do <stat>
    <bexpr> ::= RELOP <expr> <expr>
    <expr> ::= + ( <exprlist> ) || - <expr> <expr>
            |  * ( <exprlist> ) || / <expr> <expr>
            |  NUM | ID
    <exprlist> ::= <expr> <exprlistp>
    <exprlistp> ::= <expr> <exprlistp> | eps
    */

    //  <prog> ::= <statlist> EOF
    // =,print,read,cond,while,{ 
    /* COMPLETO */
    public void prog() {
        switch (look.tag) {
            case '=':
            case Tag.PRINT:
            case Tag.READ:
            case Tag.COND:
            case Tag.WHILE:
            case '{':
            // ... completare ...
                int lnext_prog = code.newLabel();
                System.out.println(lnext_prog);
                statlist(lnext_prog); //salto a statlist
                code.emitLabel(lnext_prog);
                match(Tag.EOF);
                try {
        	       code.toJasmin();
                }
                catch(java.io.IOException e) {
        	       System.out.println("IO error\n");
                }
                break;

            default:
                error("Errore in prog");
        }
    }

    //<statlist> ::= <stat> <statlistp>
    // =,print,read,cond,while,{ 
    // arrivo qui partendo da prog prendendo come lable 1
    // torno qui dopo il match della { in stat 
    /* COMPLETO */
    public void statlist(int lnext_prog) {
        //System.out.println(lnext_prog); 
        switch (look.tag) {
            case '=':
            case Tag.PRINT:
            case Tag.READ:
            case Tag.COND:
            case Tag.WHILE:
            case '{':
                //int lnext_stat = code.newLabel();
                int lnext_statlist = code.newLabel();
                stat(lnext_statlist);
                code.emitLabel(lnext_statlist); 
                //incremento una lable e la passo a statlistp
                statlistp(lnext_prog);
                //code.emitLabel(lnext_prog); a cui passo l'etichetta
                //passata da 
                break;

            default: 
                error ("Errore in statlist"); 
        }

    }

    //<statlistp> ::= ; <stat> <statlistp> | eps
    // ;
    // $,}
    // lnext_statlist è il valore che mi arriva da statlist
    /* COMPLETO */
    public void statlistp (int lnext_prog) {
        //fuori dallo switch così sono sicuro di averla per le epsilon produzioni
        //int lnext_statlistp = lnext_statlist;
        //lnext_statlistp = code.newLabel();
        switch (look.tag) {
            case ';':
                //
                //int lnext_statlistp = lnext_statlist;
                //lnext_statlistp = code.newLabel();
                match(Token.semicolon.tag);
                int lnext_statlistp = code.newLabel();
                //dopo aver riconosciuto il ; carico il goto sullo stack
                //code.emit(OpCode.GOto, lnext_statlist);
                //carico la label sullo stack
                //code.emitLabel(lnext_statlist);
                //chiamo stat passando la lable
                stat(lnext_statlistp);
                code.emitLabel(lnext_statlistp);
                //che etichetta per statlistp? sempre la stessa?
                //la incremento di 1
                //lnext_statlist = code.newLabel(); 
                statlistp(lnext_prog);
                break;

            //volendo posso riabilitare i goto, devo solo pensare a quale
            //etichetta passargli    
            case Tag.EOF:
                //ci metto il goto a una lable successiva a statlist
                //code.emit(OpCode.GOto, lnext_statlistp);
                break;

            case '}':
                //code.emit(OpCode.GOto, lnext_statlistp);
                break;

            default:
                error("Errore in statlistp");        
        }
    }

    /*
    <stat> ::= = ID <expr>
             | print ( <exprlist> )
             | read (ID) 
             | cond <whenlist> else <stat>
             | while ( <bexpr> ) <stat>
             | { <statlist> }
    */
    // =
    // print
    // read
    // cond
    // while 
    // { 
    //lnext_statlist è l'etichetta "attuale"
    public void stat(int lnext) {
        switch(look.tag) {
	    // ... completare ...
            //la parte fatta da loro non la tocco
            case '=':
                match(Token.assign.tag);
                if (look.tag==Tag.ID) {
                    int id_addr = st.lookupAddress(((Word)look).lexeme);
                    if (id_addr==-1) {
                        id_addr = count;
                        st.insert(((Word)look).lexeme,count++);
                    }
                    match(Tag.ID);
                    // gli passo la lable che ho così continua ad incrementarla così che
                    // se poi chiamo exprlistp posso gestire l'epsilon produzione
                    expr(); 
                    code.emit(OpCode.istore,id_addr);
                    //goto 
                } else error("Error in grammar (stat) after read( with " + look);                    
                break; //torno a statlist

            case Tag.PRINT:
                match(Tag.PRINT);
                match(Token.lpt.tag);
                // gli passo sempre la solita label 
                exprlist();
                code.emit(OpCode.invokestatic,1);
                match(Token.rpt.tag);
                break;

            case Tag.READ:
                match(Tag.READ);
                match('(');
                if (look.tag==Tag.ID) {
                    int id_addr = st.lookupAddress(((Word)look).lexeme);
                    if (id_addr==-1) {
                        id_addr = count;
                        st.insert(((Word)look).lexeme,count++);
                    }                    
                    match(Tag.ID);
                    match(')');
                    code.emit(OpCode.invokestatic,0);
                    code.emit(OpCode.istore,id_addr);   
                }
                else
                    error("Error in grammar (stat) after read( with " + look);
                break;
            
            case Tag.COND:
                match(Tag.COND);
                int btrue = code.newLabel();
                int bfalse = code.newLabel();
                // cosa passo a whenlist?
                // l'incremento lo faccio gestire a whenlist
                whenlist(btrue, bfalse);
                match(Tag.ELSE);
                code.emitLabel(btrue);
                int lnext_stat = lnext;
                stat(lnext_stat);
                code.emit(OpCode.GOto, lnext_stat);
                //eheh e adesso?
                break;
            
            case Tag.WHILE:
                match(Tag.WHILE);
                match(Token.lpt.tag);
                // devo passargli due parametri
                bexpr(lnext, lnext); //salto a bexpr
                //torno da bexpr dopo aver caricato if_icmpgt L4
                match(Token.rpt.tag); 
                stat(lnext);
                break;
            //a posto
            case '{':
                match(Token.lpg.tag);
                // gli passo l'etichetta "attuale"
                statlist(lnext); //chiamo statlist passando la lable
                match(Token.rpg.tag);
                break;

            default:
                error("Errore in stat");
        }
    }

    // <whenlist> ::= <whenitem> <whenlistp>
    //when
    //non faccio nulla
    // lnext_stat mi arriva da stat da cond 
    public void whenlist (int btrue, int bfalse) {
        switch (look.tag) {
            case Tag.WHEN:
                //devo passargli true o false?
                whenitem(btrue);
                //devo passargli true o false?
                whenlistp(btrue);
                break;

            default:
                error("Errore in whenlist");
        }
    }

    // <whenlistp> ::= <whenitem> <whenlistp> | eps
    // when
    // else
    
    public void whenlistp (int lnext_stat) {
        //incremento la lable
        //int lnext_whenlistp = lnext_stat;
        //lnext_whenlistp = code.newLabel();
        
        switch(look.tag) {
            case Tag.WHEN:
                whenitem(lnext_stat);
                //incremento la label di uno
                lnext_stat = code.newLabel();
                whenlistp(lnext_stat);
                break;

            //volendo le posso riabilitare
            case Tag.ELSE:
                //code.emit(OpCode.GOto, lnext_whenlistp);
                break;

            default:
                error("Errore in whenlistp");
        }
    }

    // <whenitem> ::= when ( <bexpr> ) do <stat>
    // when
    public void whenitem (int lnext_prog) { 
        
        //int lnext_whenitem = lnext_whenlist;
        //lnext_whenitem = code.newLabel();

        switch (look.tag) {

            case Tag.WHEN:
                match(Tag.WHEN);
                match(Token.lpt.tag);
                int btrue = code.newLabel();
                int bfalse = code.newLabel();
                //devo incrementare la label?
                // lnext_whenlist vale sempre lnext_stat
                bexpr(btrue, bfalse);
                match(Token.rpt.tag);
                match(Tag.DO);
                code.emitLabel(btrue);
                int lnext_whenitem = lnext_prog;
                //carico il goto
                //code.emit(OpCode.GOto, lnext_whenitem);
                //gli passo l'etichetta "vecchia"
                stat(lnext_whenitem);
                code.emit(OpCode.GOto, lnext_whenitem);
                //e ora?
                break;
                
            default:
                error("Errore in whenitem");
        }
    }

    // <bexpr> ::= RELOP <expr> <expr>
    // RELOP
    // 
    public void bexpr (int btrue, int bfalse) {
        switch (look.tag) {
            case Tag.RELOP:
                
                switch (((Word)look).lexeme){
                    //voglio capire quale caso
                    //non sono sicuro del valore che passo a expr
                    case "<":
                        match(Tag.RELOP);
                        expr();
                        expr();
                        //newlable?
                        code.emit(OpCode.if_icmplt, btrue);
                        code.emit(OpCode.GOto, bfalse);
                        break;
                    case ">":
                        match(Tag.RELOP);
                        expr();
                        expr();
                        //newlable?
                        code.emit(OpCode.if_icmpgt, btrue);
                        code.emit(OpCode.GOto, bfalse);
                        break;
                    case "==":
                        match(Tag.RELOP);
                        expr();
                        expr();
                        //newlable?
                        code.emit(OpCode.if_icmpeq, btrue);
                        code.emit(OpCode.GOto, bfalse);
                        break;
                    case "<=":
                        match(Tag.RELOP);
                        expr();
                        expr();
                        //newlable?
                        code.emit(OpCode.if_icmple, btrue);
                        code.emit(OpCode.GOto, bfalse);
                        break;
                    case "<>":
                        match(Tag.RELOP);
                        expr();
                        expr();
                        //newlable?
                        code.emit(OpCode.if_icmpne, btrue);
                        code.emit(OpCode.GOto, bfalse);
                        break;
                    case ">=":
                        match(Tag.RELOP);
                        expr();
                        expr();
                        //newlable?
                        code.emit(OpCode.if_icmpge, btrue);
                        code.emit(OpCode.GOto, bfalse);
                        break;

                    default:
                        error("Errore in bexpr, il simbolo non corrisponde a nulla");            
                }

                break;

            default:
                error("Errore in bexpr");
        }
    }

    /*
     <expr> ::= + ( <exprlist> ) || - <expr> <expr>
             | * ( <exprlist> ) || / <expr> <expr>
             | NUM | ID
    */
    // +
    // -
    // *
    // /
    // NUM
    // ID
    //arrivo da stat        
    private void expr() {
        switch(look.tag) {
	    // ... completare ...
            case '+':
                match(Token.plus.tag);
                match(Token.lpt.tag);
                exprlist();
                code.emit(OpCode.iadd);
                match(Token.rpt.tag);
                break;

            case '-':
                match(Token.minus.tag);
                expr();
                expr();
                code.emit(OpCode.isub);
                break;

            case '*':
                match(Token.mult.tag);
                match(Token.lpt.tag);
                exprlist();
                code.emit(OpCode.imul);
                match(Token.rpt.tag);
                break;

            case '/':
                match(Token.div.tag);
                expr();
                expr();
                code.emit(OpCode.idiv);
                break;
            
            case Tag.NUM:
                //leggo il numero
                int numero = Integer.parseInt(((NumberTok)look).number);
                match(Tag.NUM);
                //int numero = Integer.parseInt(((NumberTok)look).number);
                code.emit(OpCode.ldc, numero);
                break; //torno a stat
            //arrivo da bexpr
            case Tag.ID:
                //riconoscimento degli ID
                int id_addr = st.lookupAddress(((Word)look).lexeme);
                //code.emit(OpCode.iload, id_addr);
                if (id_addr == -1) {
                        //ci devo mettere un errore se non ho già la variabile
                        error("Variabile non presente in memoria o non inizializzata");
                        //id_addr = count;
                        //st.insert(((Word)look).lexeme, count++);
                    }  
                // il match va prima della iload     
                match(Tag.ID);
                code.emit(OpCode.iload, id_addr);
                break;

            default:
                error("Errore in expr");
        }
    }

    // <exprlist> ::= <expr> <exprlistp>
    // +,-,*,/,NUM,ID
    //non faccio nulla
    public void exprlist () {
        switch (look.tag) {
            case '+':
            case '-':
            case '*':
            case '/':
            case Tag.NUM:
            case Tag.ID:
                expr();
                exprlistp();
                break;

            default: 
                error("Errore in exprlist");
        }
    }

    // <exprlistp> ::= <expr> <exprlistp> | eps
    // +,-,*,/,NUM,ID
    // non faccio nulla
    public void exprlistp () {
        switch (look.tag) {
            case '+':
            case '-':
            case '*':
            case '/':
            case Tag.NUM:
            case Tag.ID:
                expr();
                exprlistp();
                break;

            //volendo posso riabilitarli pensando a quale etichetta
            //passare
            case ')':
                //code.emit(OpCode.GOto, lnext_expr);
                break;

            default: 
                error("Errore in exprlistp");

        }
    }

    public static void main(String[] args){
        Lexer lex = new Lexer();
        String path = "/Users/lorenzo/Projects /LFT-2020/TRANSLATOR/A.lft"; //path del file 
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Translator translator = new Translator(lex, br);
            translator.prog();
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}