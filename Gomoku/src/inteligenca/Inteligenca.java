package inteligenca;

import java.util.List;
import logika.Igra;
import logika.Igralec;
import logika.Stanje;
import splosno.Koordinati;
import splosno.KdoIgra;

public class Inteligenca extends KdoIgra {
	
	private static final int ZMAGA = 1000000000; // vrednost zmage
	private static final int ZGUBA = -ZMAGA;  // vrednost izgube
	private static final int NEODLOC = 0;  // vrednost neodločene igre	
	
	private int globina;
	
	public Inteligenca (String ime) {
		super(ime);
		globina = 3;
	}
	
	
	public Koordinati izberiPotezo (Igra igra) {
		Koordinati r = poisciZmago(igra);
		if (r != null) return r;
		
		Koordinati q = poisciPoraz(igra);
		if (q != null) return q;
		
		return alphabetaPoteze(igra, this.globina, ZGUBA, ZMAGA, igra.naPotezi, 0).poteza;
	}
	
	
	public static OcenjenaPoteza alphabetaPoteze(Igra igra, int globina, int alpha, int beta, Igralec jaz, int zavlacuj) {
		int ocena;
		// Če sem računalnik, maksimiramo oceno z začetno oceno ZGUBA
		// Če sem pa človek, minimiziramo oceno z začetno oceno ZMAGA
		if (igra.naPotezi == jaz) {ocena = ZGUBA;} else {ocena = ZMAGA;}
		List<Koordinati> moznePoteze = igra.poteze_omejeno();
		Koordinati kandidat = moznePoteze.get(0); // Možno je, da se ne spremini vrednost kanditata. Zato ne more biti null.
			
		for (Koordinati p: moznePoteze) {
			Igra kopijaIgre = new Igra(igra);
			kopijaIgre.odigraj (p);
			int ocenap;
			switch (kopijaIgre.stanje()) {
			case ZMAGA_B: ocenap = (jaz == Igralec.B ? ZMAGA : ZGUBA); break;
			case ZMAGA_W: ocenap = (jaz == Igralec.W ? ZMAGA : ZGUBA); break;
			case NEODLOCENO: ocenap = NEODLOC; break;
			default:
				// Nekdo je na potezi
				if (globina == 1) ocenap = OceniPozicijo.oceniPozicijo(kopijaIgre, jaz);
				else ocenap = alphabetaPoteze (kopijaIgre, globina-1, alpha, beta, jaz, zavlacuj + 1).ocena;
			}
			if (igra.naPotezi == jaz) { // Maksimiramo oceno
				if (ocenap > ocena) { // mora biti > namesto >=
					ocena = ocenap + zavlacuj;
					kandidat = p;
					alpha = Math.max(alpha,ocena);
				}
			} else { // igra.naPotezi() != jaz, torej minimiziramo oceno
				if (ocenap < ocena) { // mora biti < namesto <=
					ocena = ocenap - zavlacuj;
					kandidat = p;
					beta = Math.min(beta, ocena);					
				}	
			}
			if (alpha >= beta) // Izstopimo iz "for loop", saj ostale poteze ne pomagajo
				return new OcenjenaPoteza (kandidat, ocena);
		}
		return new OcenjenaPoteza (kandidat, ocena);
	}
	
	public static Koordinati poisciZmago(Igra igra) {
		List<Koordinati> moznePoteze = igra.poteze_omejeno();
		for (Koordinati p : moznePoteze) {
			Igra kopijaIgre = new Igra(igra);
			kopijaIgre.odigraj(p);
			if (igra.naPotezi == Igralec.B && kopijaIgre.stanje() == Stanje.ZMAGA_B) return p;
			else if (igra.naPotezi == Igralec.W && kopijaIgre.stanje() == Stanje.ZMAGA_W) return p;
		}
		return null;
	}
	
	public static Koordinati poisciPoraz(Igra igra) {
		Igra kopijaIgre = new Igra(igra);
		kopijaIgre.naPotezi = kopijaIgre.naPotezi.nasprotnik();
		Koordinati p = poisciZmago(kopijaIgre);
		return p;
	}
	
}

