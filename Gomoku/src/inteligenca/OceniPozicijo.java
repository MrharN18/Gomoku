package inteligenca;

import logika.Igra;
import logika.Igralec;
import logika.Polje;
import logika.Vrsta;

public class OceniPozicijo {
	
	
	public static int oceniPozicijo(Igra igra, Igralec jaz) {
		Inteligenca.evaluationCount++;
		int ocena = 0;
		for (Vrsta v : Igra.VRSTE) {
			ocena = ocena + oceniVrsto(v, igra, jaz);
		}
		return ocena;	
	}
	
	public static int oceniVrsto (Vrsta v, Igra igra, Igralec jaz) {
		Polje[][] plosca = igra.getPlosca();
		boolean zaporedoma = true;
		int zaporedno = 0;
		int count_B = 0;
		int count_W = 0;
		for (int k = 0; k < 5 && (count_B == 0 || count_W == 0); k++) {
			switch (plosca[v.x[k]][v.y[k]]) {
			case B: {count_B += 1; zaporedno += 1;} break;
			case W: {count_W += 1; zaporedno += 1;} break;
			case PRAZNO: {if (zaporedno > 0 && zaporedno < 4) {zaporedoma = !zaporedoma; zaporedno = 0;}} break;
			}
		}
		if (count_B > 0 && count_W > 0) { return 0; }
		return oceni1(count_B,count_W,jaz,zaporedoma);
	}
	
	public static int oceni(int count_B, int count_W, Igralec jaz) {
		int score = 0;
		if (jaz == Igralec.B) {
			switch (count_B - count_W) {
			case 5: score = 1000000;break;
			case 4: score = 5000; break;
			case 3: score = 2000;break;
			case 2: score = 500;break;
			case 1: score = 5;break;
			case -1: score = -5;break;
			case -2: score = -1500;break;
			case -3: score = -6000;break;
			case -4: score = -15000;break;
			case -5: score = -1000000;break;
			}
			
		}
		else {
			switch (count_W - count_B) {
			case 5: score = 1000000;break;
			case 4: score = 5000; break;
			case 3: score = 2000;break;
			case 2: score = 500;break;
			case 1: score = 5;break;
			case -1: score = -5;break;
			case -2: score = -1500;break;
			case -3: score = -6000;break;
			case -4: score = -15000;break;
			case -5: score = -1000000;break;
			}
		}
		return score;
	}
	
	
	public static int oceni1(int count_B, int count_W, Igralec jaz, boolean zaporedoma) {
		int score = 0;
		double a = 2.5;
		double b = 2.3;
		if (jaz == Igralec.B && zaporedoma == true) {
			switch (count_B - count_W) {
			case 5: score = 1000000;break;
			case 4: score = 5000; break;
			case 3: score = 2000;break;
			case 2: score = 700;break;
			case 1: score = 5;break;
			case -1: score = (int) -a*5;break;
			case -2: score = (int) -a*500;break;
			case -3: score = (int) -b*2000;break;
			case -4: score = (int) -a*5000;break;
			case -5: score = (int) 1000000;break;
			}
			
		}
		else if (jaz == Igralec.W && zaporedoma == true) {
			switch (count_W - count_B) {
			case 5: score = 1000000;break;
			case 4: score = 5000; break;
			case 3: score = 2000;break;
			case 2: score = 700;break;
			case 1: score = 5;break;
			case -1: score = (int) -a*5;break;
			case -2: score = (int) -a*500;break;
			case -3: score = (int) -b*2000;break;
			case -4: score = (int) -a*5000;break;
			case -5: score = (int) 1000000;break;
			}
		}
		else if (jaz == Igralec.B) {
			switch (count_B - count_W) {
			case 5: score = 1000000;break;
			case 4: score = 4000; break;
			case 3: score = 1000;break;
			case 2: score = 400;break;
			case 1: score = 5;break;
			case -1: score = (int) -a*5;break;
			case -2: score = (int) -a*400;break;
			case -3: score = (int) -b*1000;break;
			case -4: score = (int) -a*4000;break;
			case -5: score = (int) 1000000;break;
			}
		}
		else {
			switch (count_W - count_B) {
			case 5: score = 1000000;break;
			case 4: score = 4000; break;
			case 3: score = 1000;break;
			case 2: score = 400;break;
			case 1: score = 5;break;
			case -1: score = (int) -a*5;break;
			case -2: score = (int) -a*400;break;
			case -3: score = (int) -b*1000;break;
			case -4: score = (int) -a*4000;break;
			case -5: score = (int) 1000000;break;
			}
		}
		return score;
	}

	public static int oceni2(int count_B, int count_W, Igralec jaz) {
		int score = 0;
		double a = 3;
		double b = 3;
		if (jaz == Igralec.B) {
			switch (count_B - count_W) {
			case 5: score = 1000000;break;
			case 4: score = 7000; break;
			case 3: score = 2500;break;
			case 2: score = 50;break;
			case 1: score = 1;break;
			case -1: score = (int) -1;break;
			case -2: score = (int) -a*50;break;
			case -3: score = (int) -b*2500;break;
			case -4: score = (int) -b*7000;break;
			case -5: score = (int) 1000000;break;
			}
			
		}
		else {
			switch (count_W - count_B) {
			case 5: score = 1000000;break;
			case 4: score = 8000; break;
			case 3: score = 3000;break;
			case 2: score = 50;break;
			case 1: score = 1;break;
			case -1: score = (int) -1;break;
			case -2: score = (int) -a*50;break;
			case -3: score = (int) -b*3000;break;
			case -4: score = (int) -b*8000;break;
			case -5: score = (int) 1000000;break;
			}
		}
		return score;
	}
}	