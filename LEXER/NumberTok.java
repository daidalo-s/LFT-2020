public class NumberTok extends Token {
	// ... completare ...
	public String number = "";
    public NumberTok(int t , String n) { super(t); number=n; }
    public String toString() { return "<256, " + number + ">";}
}