public class EsercizioUno_Quattro {
	/*
	Devo implementare un automa che riconosca numeri di matricola+cognome subito
	attaccato rispettando questi criteri:
	Numero pari --> A-K
	Numero dispari --> L-Z
	Il numero di matricola può contenere un numero qualsiasi di cifre, così come il
	cognome.
	Supporta anche trattini e apostrofi
	*/
	public static boolean scan(String s)
    {
	int state = 0;
	int i = 0;

	while (state >= 0 && i < s.length()) {
	    final char ch = s.charAt(i++);

	    switch (state) {
	    case 0:
	    //se spazio rimango sempre in 0
	    if ((int)ch == 32)
	    	state = 0;
	    //se pari vado in 1
		else if (ch == '0' || ch == '2' || ch == '4' || ch == '6' || ch == '8')
		    state = 1;
		//se dispari vado in 2
		else if (ch == '1' || ch == '3' || ch == '5' || ch == '7' || ch == '9')
		    state = 2;
		//qualsiasi altra cosa out
		else
		    state = -1;
		break;

	    case 1:
	    //se numero pari continuo qua
		if (ch == '0' || ch == '2' || ch == '4' || ch == '6' || ch == '8')
		    state = 1;
		//se dispari passo a due
		else if (ch == '1' || ch == '3' || ch == '5' || ch == '7' || ch == '9')
		    state = 2;
		//se ho uno spazio vado a 4
		else if ((int)ch == 32)
			state = 4;
		//se lettera maiuscola vado a 5
		else if ((int)ch >= 65 && (int)ch <= 75)
			state = 5;
		else
		    state = -1;
		break;

	    case 2:
	    //se dispai rimango qua
		if (ch == '1' || ch == '3' || ch == '5' || ch == '7' || ch == '9')
		    state = 2;
		//se pari vado in 1
		else if (ch == '0' || ch == '2' || ch == '4' || ch == '6' || ch == '8')
		    state = 1;
		//se spazio vado in 3
		else if ((int)ch == 32)
			state = 3;
		//se lettera maiuscola vado a 5
		else if ((int)ch >= 76 && (int)ch <= 90)
			state = 5;
		else
		    state = -1;
		break;

	    case 3:
	    //se lettera vado in 5
		if ((int)ch >= 76 && (int)ch <= 90)
		    state = 5;
		//se ho spazi continuo a rimanere qua
		else if ((int)ch == 32)
			state = 3;
		else
		    state = -1;
		break;

		case 4:
		//se lettera maiuscola vado in 5
		if ((int)ch >= 65 && (int)ch <= 75) 
			state = 5;
		//se continuo con gli spazi rimango qua
		else if ((int)ch == 32)
			state = 4;
		else 
			state = -1;
		break;

		case 5:
		//continuo a rimanere qua se lettere minuscole
		if ((int)ch >= 97 && (int)ch <= 122)
			state = 5; 
		//se trovo spazio, apostrofo o trattino vado a 6
		else if ((int)ch == 32 || (int)ch == 39 || (int)ch == 45)
			state = 6;
		else 
			state = -1;
		break;

		case 6:
		//se continuo a trovare spazi rimango qua
		if ((int)ch == 32)
			state = 6;
		//se trovo qualsiasi ettera maiuscola torno a 5
		else if ((int)ch >= 65 && (int)ch <= 90)
			state = 5;
		else 
			state = -1;
		break;
	    }
	}
	return state == 5 || state == 6;
    }

    public static void main(String[] args)
    {
	System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }

}