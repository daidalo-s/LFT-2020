public class EsercizioUno_Dieci {
	
	public static boolean scan(String s)
    {
	int state = 0;
	int i = 0;

	while (state >= 0 && i < s.length()) {
	    final char ch = s.charAt(i++);

	    switch (state) {
	    case 0:
	    //se a o * rimango qua
		if (ch == 'a' || ch == '*')
		    state = 0;
		//se / vado a 1
		else if (ch == '/')
		    state = 1;
		else
		    state = -1;
		break;

	    case 1:
	    //se / rimango qua
		if (ch == '/')
		    state = 1;
		//se * vado a 2
		else if (ch == '*')
		    state = 2;
		//se a torno a zero
		else if (ch == 'a')
			state = 0;
		else
		    state = -1;
		break;

	    case 2:
	    //se a o / rimango qua
		if (ch == 'a' || ch == '/')
		    state = 2;
		//se * vado a 3
		else if (ch == '*')
		    state = 3;
		else
		    state = -1;
		break;

	    case 3:
	    //se * rimango qua
		if (ch == '*')
		    state = 3;
		//se / vado a 0
		else if (ch == '/')
			state = 0;
		//con a torno a due
		else if (ch == 'a')
			state = 2;
		else
		    state = -1;
		break;
	    }
	}
	return state == 0 || state == 1;
    }

    public static void main(String[] args)
    {
	System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }

}