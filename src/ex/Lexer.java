package ex;

import java.util.ArrayList;
import java.util.List;

/*
 * Transforms a string into a list of Tokens
 */
public class Lexer {

	public Lexer() {

	
	}
	
	public List<Token> lexe(String s){
		
		SourceReader sr = new SourceReader(s);
		
		return lexer(sr);
	}
	

	
	static Integer transitions[][] = {
			//             espace lettre chiffre   [    ]     autre
			/*  0 */    {      0,     1,      2,   3,   4,     null      },
			/*  1 */    {    201,     1,      1, 201, 201,     null      },
			/*  2 */    {    202,   202,      2, 202, 202,     null      },
			/*  3 */    {    203,   203,    203, 203, 203,     null      },
			/*  4 */    {    204,   204,    204, 204, 204,     null      }


			// 201 accepte identifiant ou mot clé   (goBack : oui)
			// 202 accepte entier                   (goBack : oui)
			// 203 accepte [                        (goBack : oui)
			// 204 accepte ]                        (goBack : oui)

	};

	static final int ETAT_INITIAL = 0;

	private int indiceSymbole(Character c) {
		if (c == null) return 0;
		if (Character.isWhitespace(c)) return 0;
		if (Character.isLetter(c)) return 1;
		if (Character.isDigit(c)) return 2;
		if (c == '[') return 3;
		if (c == ']') return 4;
		return 5;
	}

	private ArrayList<Token> lexer(SourceReader sr) {
		ArrayList<Token> tokens = new ArrayList<Token>();
		String buf="";
		int etat = ETAT_INITIAL;
		while (true) {
			Character c = sr.lectureSymbole();
			Integer e = transitions[etat][indiceSymbole(c)];
			if (e == null) {
				System.out.println(" pas de transition depuis état " + etat + " avec symbole " + c);
				return new ArrayList<Token>(); // renvoie une liste vide
			}
			if (e >= 100) {
				switch (e) {
				case 201:
					// System.out.println("Accepte identifiant " + buf);
					tokens.add(HandleKeywordOrIdentifier(buf));
					sr.goBack();
					break;
					
				case 202:
					// System.out.println("Accepte intVal " + buf);
					tokens.add(new Token(TokenClass.intVal, buf));
					sr.goBack();
					break;
					
				case 203:
					// System.out.println("Accepte [");
					tokens.add(new Token(TokenClass.blockStart));
					sr.goBack();
					break;
					
				case 204:
					// System.out.println("Accepte ]");
					tokens.add(new Token(TokenClass.blockEnd));
					sr.goBack();
					break;
				} 
				
				etat = 0;
				buf = "";
			} else {
				etat = e;
				if (etat>0) buf = buf + c;
			}
			if (c==null) break;
		}
		return tokens;
	}

	
	private Token HandleKeywordOrIdentifier(String s) {
		switch(s) {
		case "procedure": return new Token(TokenClass.kProc);
		case "call": return new Token(TokenClass.kCall);
		case "repeat": return new Token(TokenClass.kRepeat);
		case "forward": return new Token(TokenClass.kForward);
		case "left": return new Token(TokenClass.kLeft);
		case "right": return new Token(TokenClass.kRight);
		case "color": return new Token(TokenClass.kColor);	
		default: return new Token(TokenClass.ident, s);
		}
	}
	
	
	public static String exemple() {
		StringBuilder sb = new StringBuilder();
		sb.append("procedure p1 [");
			sb.append("forward 10");
			sb.append("right 90");
		sb.append("]");
		
		sb.append("procedure p2 [");
			sb.append("color 11");
			sb.append("right 90");
			sb.append("forward 15");
			sb.append("color 1");
			
			sb.append("repeat 4 [");
				sb.append("call p1");
			sb.append("]");
			
			sb.append("right 180");
			sb.append("color 11");
			sb.append("forward 15");
			sb.append("right 90");
		sb.append("]");
			
		sb.append("color 11");
		sb.append("left 90");
		sb.append("forward 40");
		sb.append("right 90");
		
		sb.append("repeat 10 [");
			sb.append("color 3");
			sb.append("forward 40");
			sb.append("right 36");
			sb.append("call p2");
		sb.append("]");
		
		
		return sb.toString();
	}
}
