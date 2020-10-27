public class EsercizioUno_Otto {
	
	public static boolean scan(String s)
    {
	int state = 0;
	int i = 0;

	while (state >= 0 && i < s.length()) {
	    final char ch = s.charAt(i++);

	    switch (state) {
	    case 0:
	    //se L vado a 1
		if (ch == 'L')
		    state = 1;
		//se Sigma-L vado a 8
		else if (ch >= 33 && ch <= 126 && ch != 76)
		    state = 8;
		else
		    state = -1;
		break;

	    case 1:
	    //se o vado a 2
		if (ch == 'o')
		    state = 2;
		//se Sigma-o vado a 9
		else if (ch >= 33 && ch <= 126 && ch != 111)
		    state = 9;
		else
		    state = -1;
		break;

	    case 2:
	    //se r vado a 3
		if (ch == 'r')
		    state = 3;
		//se Sigma-r vado a 10
		else if (ch >= 33 && ch <= 126 && ch != 114)
		    state = 10;
		else
		    state = -1;
		break;

	    case 3:
	    //se e vado a 4
		if (ch == 'e')
		    state = 4;
		//se Sigma-e vado a 11
		else if (ch >= 33 && ch <= 126 && ch != 101)
			state = 11;
		else
		    state = -1;
		break;

		case 4:
		//se n vado a 5
		if (ch == 'n')
		    state = 5;
		//se Sigma-n vado a 12
		else if (ch >= 33 && ch <= 126 && ch != 110)
			state = 12;
		else
		    state = -1;
		break;

		case 5:
		//se z vado a 6
		if (ch == 'z')
		    state = 6;
		//se Sigma-z vado a 13
		else if (ch >= 33 && ch <= 126 && ch != 122)
			state = 13;
		else
		    state = -1;
		break;

		case 6:
		//Sigma e vado a 7
		if (ch >= 33 && ch <= 126)
		    state = 7;
		else
		    state = -1;
		break;

		case 7:
		//stato finale non faccio nulla
		break;

		case 8:
		//se o vado a 9
		if (ch == 'o')
		    state = 9;
		else
		    state = -1;
		break;

		case 9:
		//se r vado a 10
		if (ch == 'r')
		    state = 10;
		else
		    state = -1;
		break;

		case 10:
		//se e vado a 11
		if (ch == 'e')
		    state = 11;
		else
		    state = -1;
		break;

		case 11:
		//se n vado a 12
		if (ch == 'n')
		    state = 12;
		else
		    state = -1;
		break;

		case 12:
		//se z vado a 13
		if (ch == 'z')
		    state = 13;
		else
		    state = -1;
		break;

		case 13:
		//se o vado a 7
		if (ch == 'o')
		    state = 7;
		else
		    state = -1;
		break;
	    }
	}
	return state == 7;
    }

    public static void main(String[] args)
    {
	System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}