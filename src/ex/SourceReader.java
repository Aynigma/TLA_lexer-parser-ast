package ex;

public class SourceReader {
	private String contenu;
	private int index=0;

	public SourceReader(String contenu) {
		this.contenu = contenu;
	}

	/*
	 * Renvoie un caractère ou null si fin de fichier
	 */
	public Character lectureSymbole() {
		if (index >= contenu.length()) return null;
		char c = contenu.charAt(index);
		index++;
		return c;
	}

	public void goBack() {
		if (index == 0) {
			System.out.println("Appel de goBack interdit");
		} else {
			index--;
		}
	}
}
