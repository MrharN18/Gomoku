import java.io.IOException;

import vodja.Vodja;
import gui.Okno;

public class Gomoku {

	public static void main(String[] args) throws IOException {
		Okno okno = new Okno(15);
		okno.pack();
		okno.setVisible(true);
		Vodja.okno = okno;
	}

}
