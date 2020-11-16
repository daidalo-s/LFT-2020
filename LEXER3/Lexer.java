import java.io.*; 
import java.util.*;

public class Lexer {

    public static int line = 1;
    private char peek = ' ';
    
    private void readch(BufferedReader br) {
        try {
            peek = (char) br.read();
        } catch (IOException exc) {
            peek = (char) -1; // ERROR
        }
    }

    public Token lexical_scan(BufferedReader br) {
        while (peek == ' ' || peek == '\t' || peek == '\n'  || peek == '\r') {
            if (peek == '\n') line++;
            readch(br);
        }
        
        switch (peek) {
            case '!':
                peek = ' ';
                return Token.not;

            case '(':
                peek = ' ';
                return Token.lpt;

            case ')':
                peek = ' ';
                return Token.rpt;
            
            case '{':
                peek = ' ';
                return Token.lpg;

            case '}':
                peek = ' ';
                return Token.rpg;

            case '+':
                peek = ' ';
                return Token.plus;

            case '-':
                peek = ' ';
                return Token.minus;

            case '*':
                peek = ' ';
                return Token.mult;

            case '/':
                peek = ' ';
                return Token.div;

            case ';':
                peek = ' ';
                return Token.semicolon;   

            case '&':
                readch(br);
                if (peek == '&') {
                    peek = ' ';
                    return Word.and;
                } else {
                    System.err.println("Erroneous character"
                            + " after & : "  + peek );
                    return null;
                }

            case '|':
                readch(br);
                if (peek == '|') {
                    peek = ' ';
                    return Word.or;
                } else {
                    System.err.println("Erroneous character"
                            + " after | : "  + peek );
                    return null;
                }

            case '<':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.le;
                } else if (peek == '>') {
                    peek = ' ';
                    return Word.ne;
                } else {
                    return Word.lt;
                } 

            case '>':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.ge;
                } else {
                    return Word.gt;
                }

            case '=':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.eq;
                } else {
                    //peek = ' ';
                    return Token.assign;
                }

            case (char)-1:
                return new Token(Tag.EOF);

            default:
                if (Character.isLetter(peek) || peek == '_') {
                // ... gestire il caso degli identificatori e delle parole chiave //
                    String s = "";
                    do {
                        s += peek;
                        readch(br);
                    } while (Character.isLetter(peek) || Character.isDigit(peek) || peek == '_');
                    
                    //controllo underscore
                    for (int i=0; i<s.length() && s.charAt(i) == '_'; i++){
                        if (i == s.length()-1){
                            System.err.println("Identifiers cant have only underscore");
                            return null;
                        }
                    }

                    switch (s) {
                        case "cond":
                            return Word.cond;

                        case "when":
                            return Word.when;

                        case "then":
                            return Word.then;

                        case "else":
                            return Word.elsetok;

                        case "while":
                            return Word.whiletok;

                        case "do":
                            return Word.dotok;

                        case "seq":
                            return Word.seq;

                        case "print":
                            return Word.print;

                        case "read":
                            return Word.read;

                        default:
                            return new Word(Tag.ID, s);
                    }

                } else if (Character.isDigit(peek)) {
                    // ... gestire il caso dei numeri ... //
                    String n = "";
                    do {
                        n += peek;
                        readch(br);
                    } while (Character.isDigit(peek));
                    //problema degli zeri
                    char test=' ';
                    try {
                        if (n.charAt(0) == '0') test = n.charAt(1);
                        if (test >= '0' && test <= '9'){
                            System.err.println("Numeric constants cant start with zero");
                            return null;
                        } 
                    } catch (StringIndexOutOfBoundsException errore) {/*faccio nulla*/}
                    return new NumberTok(Tag.NUM, n);
                } else {
                        System.err.println("Erroneous character: " + peek );
                        return null;
                }
         }
    }
		
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "/Users/lorenzo/Projects /LFT-2020/LEXER2/test.txt"; 
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Token tok;
            do {
                tok = lex.lexical_scan(br);
                System.out.println("Scan: " + tok);
            } while (tok.tag != Tag.EOF);
            br.close();
        } catch (IOException e) {e.printStackTrace();}    
    }

}
