public class EsercizioUno_Sei {
	
	public static boolean scan(String s)
    {
	int state = 0;
	int i = 0;

	while (state >= 0 && i < s.length()) {
	    final char ch = s.charAt(i++);

	    switch (state) {
	    case 0:
	    //se a vado a 1
		if (ch == 'a')
		    state = 1;
		//se b vado a due
		else if (ch == 'b')
		    state = 2;
		else
		    state = -1;
		break;

	    case 1:
	    //qualsiasi lettera trovi continuo a rimanere qua
		if (ch == 'a' || ch =='b')
		    state = 1;
		else
		    state = -1;
		break;

	    case 2:
	    //se ho b vado a 3
		if (ch == 'b')
		    state = 3;
		//se ho a vado a 1
		else if (ch == 'a')
		    state = 1;
		else
		    state = -1;
		break;

	    case 3:
	    //se ho a torno a 1
		if (ch == 'a')
		    state = 1;
		else
		    state = -1;
		break;
	    }
	}
	return state == 1;
    }

    public static void main(String[] args)
    {
	System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }

}