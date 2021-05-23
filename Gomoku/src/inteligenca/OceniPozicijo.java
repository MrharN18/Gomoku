package inteligenca;

import logika.Igra;
import logika.Igralec;
import logika.Polje;
import logika.Vrsta;

public class OceniPozicijo {
	
	// Metoda oceniPozicijo za igro TicTacToe
	
	
	public static int oceniPozicijo(Igra igra, Igralec jaz) {
		int ocena = 0;
		for (Vrsta v : Igra.VRSTE) {
			ocena = ocena + oceniVrsto(v, igra, jaz);
		}
		return ocena;	
	}
	
	public static int oceniVrsto (Vrsta v, Igra igra, Igralec jaz) {
		Polje[][] plosca = igra.getPlosca();
		int count_B = 0;
		int count_W = 0;
		for (int k = 0; k < 5 && (count_B == 0 || count_W == 0); k++) {
			switch (plosca[v.x[k]][v.y[k]]) {
			case B: count_B += 1; break;
			case W: count_W += 1; break;
			case PRAZNO: break;
			}
		}
		if (count_B > 0 && count_W > 0) { return 0; }
		return oceni(count_B,count_W,jaz);
	}
	
	
	public static int oceni(int count_B, int count_W, Igralec jaz) {
		int score = 0;
		if (jaz == Igralec.B) {
			switch (count_B - count_W) {
			case 5: score = 1000000;break;
			case 4: score = 10000; break;
			case 3: score = 50;break;
			case 2: score = 20;break;
			case 1: score = 5;break;
			case -1: score = -5;break;
			case -2: score = -20;break;
			case -3: score = -50;break;
			case -4: score = -10000;break;
			case -5: score = -1000000;break;
			}
			
		}
		else {
			switch (count_W - count_B) {
			case 5: score = 1000000;break;
			case 4: score = 10000;break;
			case 3: score = 50;break;
			case 2: score = 20;break;
			case 1: score = 5;break;
			case -1: score = -5;break;
			case -2: score = -20;break;
			case -3: score = -50;break;
			case -4: score = -10000;break;
			case -5: score = -1000000;break;
			}
		}
		return score;
	}
}	
		
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
