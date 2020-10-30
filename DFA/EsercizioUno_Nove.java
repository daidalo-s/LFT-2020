public class EsercizioUno_Nove {
	
	public static boolean scan(String s)
    {
	int state = 0;
	int i = 0;

	while (state >= 0 && i < s.length()) {
	    final char ch = s.charAt(i++);

	    switch (state) {
	    case 0:
	    //se / vado in 1
		if (ch == '/')
		    state = 1;
		else
		    state = -1;
		break;

	    case 1:
	    //se * vado in 2
		if (ch == '*')
		    state = 2;
		else
		    state = -1;
		break;

	    case 2:
	    //se a oppure / rimango in 2
		if (ch == 'a' || ch == '/' )
		    state = 2;
		//se * passo a 3
		else if (ch == '*')
		    state = 3;
		else
		    state = -1;
		break;

	    case 3:
	    //se a vado a 2
		if (ch == 'a')
		    state = 2;
		//se / vado a 4
		else if (ch == '/' )
			state = 4;
		//se * rimango in 3
		else if (ch == '*')
			state = 3;
		else
		    state = -1;
		break;

		case 4:
		//se leggo qualcosa qua la stringa è per forza sbagliata
		if (ch == '/' || ch == '*' || ch == 'a')
			state = 5;
		else 
			state = -1;
		break;

		case 5:
		//non faccio nulla
		break;
	    }
	}
	return state == 4;
    }

    public static void main(String[] args)
    {
	System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}    