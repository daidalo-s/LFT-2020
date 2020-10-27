public class EsercizioUno_Cinque {
	/*
	Progettare un DFA che riconosca Cognome+Matricola del turno T2 e T3
	A-K ---> Pari
	L-Z ---> Dispari
	Non si menzionano spazi o cose del genere quindi stonks
	*/
	public static boolean scan(String s)
    {
	int state = 0;
	int i = 0;

	while (state >= 0 && i < s.length()) {
	    final char ch = s.charAt(i++);

	    switch (state) {
	    case 0:
	    //se A-K vado a 1
		if ((int)ch >= 65 && (int)ch <= 75)
		    state = 1;
		//se L-Z vado a 2
		else if ((int)ch >= 76 && (int)ch <= 90)
		    state = 2;
		else
		    state = -1;
		break;

	    case 1:
	    //rimango qua con le lettere minuscole
		if ((int)ch >= 97 && (int)ch <= 122)
		    state = 1;
		//se numero pari vado a 3
		else if (ch == '0' || ch == '2' || ch == '4' || ch == '6' || ch == '8')
		    state = 3;
		//se numero dispari vado a 5
		else if (ch == '1' || ch == '3' || ch == '5' || ch == '7' || ch == '9')
			state = 5;
		else
		    state = -1;
		break;

	    case 2:
	    //rimango qua con le lettere minuscole
		if ((int)ch >= 97 && (int)ch <= 122)
		    state = 2;
		//se numero dispari vado a 4
		else if (ch == '1' || ch == '3' || ch == '5' || ch == '7' || ch == '9')
		    state = 4;
		//se ho un numero pari vado a 6
		else if (ch == '0' || ch == '2' || ch == '4' || ch == '6' || ch == '8')
		    state = 6;
		else
		    state = -1;
		break;

	    case 3:
	    //se pari rimango qua
		if (ch == '0' || ch == '2' || ch == '4' || ch == '6' || ch == '8')
		    state = 3;
		//se dispari vado a 5
		else if (ch == '1' || ch == '3' || ch == '5' || ch == '7' || ch == '9')
			state = 5;
		else
		    state = -1;
		break;

		case 4:
		//se dispari rimango qua
		if (ch == '1' || ch == '3' || ch == '5' || ch == '7' || ch == '9')
		    state = 4;
		//se pari vado a 6
		else if (ch == '0' || ch == '2' || ch == '4' || ch == '6' || ch == '8')
		    state = 6;
		else
		    state = -1;
		break;

		case 5:
		//se dispari rimango qua
		if (ch == '1' || ch == '3' || ch == '5' || ch == '7' || ch == '9')
		    state = 5;
		//se pari torno in 3
		else if (ch == '0' || ch == '2' || ch == '4' || ch == '6' || ch == '8')
		    state = 3;
		else
		    state = -1;
		break;

		case 6:
		//se pari rimango qua
		if (ch == '0' || ch == '2' || ch == '4' || ch == '6' || ch == '8')
		    state = 6;
		//se dispari torno a 4
		else if (ch == '1' || ch == '3' || ch == '5' || ch == '7' || ch == '9')
		    state = 4;
		else
		    state = -1;
		break;
	    }
	}
	return state == 3 || state == 4;
    }

    public static void main(String[] args)
    {
	System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }

}