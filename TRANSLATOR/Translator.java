import java.io.*;

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

    /* COMPLETO */
    public void prog() {
        switch (look.tag) {
            case '=':
            case Tag.PRINT:
            case Tag.READ:
            case Tag.COND:
            case Tag.WHILE:
            case '{':
                int prog_next = code.newLabel();
                statlist();
                code.emitLabel(prog_next); 
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

    public void statlist() {

        switch (look.tag) {
            case '=':
            case Tag.PRINT:
            case Tag.READ:
            case Tag.COND:
            case Tag.WHILE:
            case '{':
                stat();
                statlistp();
                break;

            default: 
                error ("Errore in statlist"); 
        }

    }

    public void stat() {
        switch(look.tag) {
            case '=':
                match(Token.assign.tag);
                if (look.tag==Tag.ID) {
                    int id_addr = st.lookupAddress(((Word)look).lexeme);
                    if (id_addr==-1) {
                        id_addr = count;
                        st.insert(((Word)look).lexeme,count++);
                    }
                    match(Tag.ID);
                    expr(); 
                    code.emit(OpCode.istore,id_addr);
                } else error("Error in grammar (stat) after read( with " + look);                    
                break; 

            case Tag.PRINT:
                match(Tag.PRINT);
                match(Token.lpt.tag);
                // 0 indica l'istruzione print
                exprlist(0);
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
                int cond_next = code.newLabel();
                int whenlist_true = code.newLabel();
                whenlist(whenlist_true, cond_next);
                match(Tag.ELSE);
                stat();
                code.emitLabel(cond_next);
                break;
            
            case Tag.WHILE:
                match(Tag.WHILE);
                match(Token.lpt.tag);
                int inizio = code.newLabel();
                code.emitLabel(inizio);
                int bexpr_true = code.newLabel();
                int bexpr_false = code.newLabel();
                bexpr(bexpr_true, bexpr_false);
                match(Token.rpt.tag);
                code.emitLabel(bexpr_true);
                stat();
                code.emit(OpCode.GOto, inizio);
                code.emitLabel(bexpr_false);
                break;
            
            case '{':
                match(Token.lpg.tag);
                statlist(); 
                match(Token.rpg.tag);
                break;

            default:
                error("Errore in stat");
        }
    }

    public void statlistp () {

        switch (look.tag) {
            case ';':
                match(Token.semicolon.tag);
                stat();
                statlistp();
                break;
  
            case Tag.EOF:
                break;

            case '}':
                break;

            default:
                error("Errore in statlistp");        
        }
    }
 
    public void whenlist (int whenlist_true, int cond_next) {
        
        switch (look.tag) {
            
            case Tag.WHEN:
                int whenlist_false = code.newLabel();
                whenitem(whenlist_true, whenlist_false, cond_next);
                whenlistp(cond_next);
                break;

            default:
                error("Errore in whenlist");
        }
    }

    public void whenitem (int whenlist_true, int whenlist_false, int cond_next) { 
    
        switch (look.tag) {

            case Tag.WHEN:
                match(Tag.WHEN);
                match(Token.lpt.tag);
                bexpr(whenlist_true, whenlist_false);
                match(Token.rpt.tag);
                match(Tag.DO);
                code.emitLabel(whenlist_true);
                stat();
                code.emit(OpCode.GOto, cond_next);
                code.emitLabel(whenlist_false);
                break;
                
            default:
                error("Errore in whenitem");
        }
    }

     public void whenlistp (int cond_next) {
    
        switch(look.tag) {

            case Tag.WHEN:
                int nuovo_true = code.newLabel();
                int nuovo_false = code.newLabel();
                whenitem(nuovo_true, nuovo_false, cond_next);
                whenlistp(cond_next);
                break;

            case Tag.ELSE:
                break;

            default:
                error("Errore in whenlistp");
        }
    }
    public void bexpr (int bexpr_true, int bexpr_false) {
        switch (look.tag) {
            
            case Tag.RELOP:
                
                switch (((Word)look).lexeme){
                    case "<":
                        match(Tag.RELOP);
                        expr();
                        expr();
                        code.emit(OpCode.if_icmplt, bexpr_true);
                        code.emit(OpCode.GOto, bexpr_false);
                        break;
                    case ">":
                        match(Tag.RELOP);
                        expr();
                        expr();
                        code.emit(OpCode.if_icmpgt, bexpr_true);
                        code.emit(OpCode.GOto, bexpr_false);
                        break;
                    case "==":
                        match(Tag.RELOP);
                        expr();
                        expr();
                        code.emit(OpCode.if_icmpeq, bexpr_true);
                        code.emit(OpCode.GOto, bexpr_false);
                        break;
                    case "<=":
                        match(Tag.RELOP);
                        expr();
                        expr();
                        code.emit(OpCode.if_icmple, bexpr_true);
                        code.emit(OpCode.GOto, bexpr_false);
                        break;
                    case "<>":
                        match(Tag.RELOP);
                        expr();
                        expr();
                        code.emit(OpCode.if_icmpne, bexpr_true);
                        code.emit(OpCode.GOto, bexpr_false);
                        break;
                    case ">=":
                        match(Tag.RELOP);
                        expr();
                        expr();
                        code.emit(OpCode.if_icmpge, bexpr_true);
                        code.emit(OpCode.GOto, bexpr_false);
                        break;

                    default:
                        error("Errore in bexpr, il simbolo non corrisponde a nulla");            
                }

                break;

            default:
                error("Errore in bexpr");
        }
    }

    private void expr() {
        switch(look.tag) {
            case '+':
                //qualcosa che indichi la somma
                match(Token.plus.tag);
                match(Token.lpt.tag);
                exprlist(1);
                match(Token.rpt.tag);
                break;

            case '-':
                match(Token.minus.tag);
                expr();
                expr();
                code.emit(OpCode.isub);
                break;

            case '*':
                //qualcosa che indichi la moltiplicazione
                match(Token.mult.tag);
                match(Token.lpt.tag);
                exprlist(2);
                match(Token.rpt.tag);
                break;

            case '/':
                match(Token.div.tag);
                expr();
                expr();
                code.emit(OpCode.idiv);
                break;
            
            case Tag.NUM:
                int numero = Integer.parseInt(((NumberTok)look).number);
                match(Tag.NUM);
                code.emit(OpCode.ldc, numero);
                break; 
         
            case Tag.ID:
                //riconoscimento degli ID
                int id_addr = st.lookupAddress(((Word)look).lexeme);
                if (id_addr == -1) {
                        //ci devo mettere un errore se non ho già la variabile
                        error("Variabile non presente in memoria o non inizializzata");
                    }     
                match(Tag.ID);
                code.emit(OpCode.iload, id_addr);
                break;

            default:
                error("Errore in expr");
        }
    }


    public void exprlist (int op) {
        switch (look.tag) {
            case '+':
            case '-':
            case '*':
            case '/':
            case Tag.NUM:
            case Tag.ID:
                expr();
                exprlistp(op);
                break;

            default: 
                error("Errore in exprlist");
        }
    }

    public void exprlistp (int op) {
        switch (look.tag) {
            case '+':
            case '-':
            case '*':
            case '/':
            case Tag.NUM:
            case Tag.ID:
                if (op == 0) {
                    code.emit(OpCode.invokestatic, 1);
                }
                expr();
                // qui
                switch (op){
                    case 0:
                        break;
                    case 1:
                        code.emit(OpCode.iadd);
                        break;
                    case 2:
                        code.emit(OpCode.imul);
                        break;
                    default:
                        error("Boh");
                }
                exprlistp(op);
                break;

            case ')':
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
