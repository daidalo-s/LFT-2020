public class EsercizioUno_Due {
	/*
	Progettare e implementare un DFA che riconosca il linguaggio degli identificatori 
	in un linguaggio in stile Java: un identificatore e` una sequenza non vuota di lettere, 
	numeri, ed il simbolo di “underscore” _ che non comincia con un numero e che non puo` 
	essere composto solo dal simbolo _. Compilare e testare il suo funzionamento su un insieme 
	significativo di esempi. 
	Esempi di stringhe accettate: “x”, “flag1”, “x2y2”, “x 1”, “lft lab”, “ temp”, “x 1 y 2”, “x ”,“ 5”
	Esempi di stringhe non accettate: “5”, “221B”, “123”, “9 to 5”, “ ”
	*/
	public static boolean scan(String s)
    {
	int state = 0;
	int i = 0;

	while (state >= 0 && i < s.length()) {
	    final char ch = s.charAt(i++);

	    switch (state) {
	    case 0:
	    //lettere maiuscole/minuscole
		if (ch >= 65 && ch <= 90 || ch >= 97 && ch <= 122)
		    state = 2;
		//underscore
		else if (ch == 95)
		    state = 1;
		else 
			state = -1;
		break;

		case 1:
		//lettere maiuscole e minuscole 
		if (ch >= 65 && ch <= 90 || ch >= 97 && ch <= 122)  
		    state = 2;
		//numeri
		else if (ch >= 48 && ch <= 57)
		    state = 2;
		//underscore
		else if (ch == 95)
			state = 1;
		else 
			state = -1;
		break;

	    case 2:
	    //lettere maiuscole e minuscole
		if (ch >= 65 && ch <= 90 || ch >= 97 && ch <= 122) 
		    state = 2;
		//numeri
		else if (ch == 32 || ch >= 48 && ch <= 57)
		    state = 2;
		else if (ch == 95)
			state = 2;
		else
			state = -1;
		break;
		}
	}
	return state == 2;
    }

    public static void main(String[] args)
    {
	System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
	
}