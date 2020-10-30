public class EsercizioUno_Sette {
	
	public static boolean scan(String s)
    {
	int state = 0;
	int i = 0;

	while (state >= 0 && i < s.length()) {
	    final char ch = s.charAt(i++);

	    switch (state) {
	    case 0:
	    //se b rimango qua
		if (ch == 'b')
		    state = 0;
		//se a vado a 1
		else if (ch == 'a')
		    state = 1;
		else
		    state = -1;
		break;

	    case 1:
	    //se a rimango qua
		if (ch == 'a')
		    state = 1;
		//se b vado a 2
		else if (ch == 'b')
		    state = 2;
		else
		    state = -1;
		break;

	    case 2:
	    //se a torno a 1
		if (ch == 'a')
		    state = 1;
		//se b vado a 3
		else if (ch == 'b')
		    state = 3;
		else
		    state = -1;
		break;

	    case 3:
	    //se a torno a 1
		if (ch == 'a')
		    state = 1;
		//se b torno a 0
		else if (ch == 'b')
			state = 0;
		else
		    state = -1;
		break;
	    }
	}
	return state == 1 || state == 2 || state == 3;
    }

    public static void main(String[] args)
    {
	System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }

}