// uvoz paketov
package inteligenca;

import java.util.List;
import java.util.Random;

import logika.Igra;
import logika.Igralec;
import logika.Stanje;
import splosno.Koordinati;
import splosno.KdoIgra;

public class Inteligenca extends KdoIgra {
	
	private static final int ZMAGA = 1000000; // vrednost zmage
	private static final int ZGUBA = -ZMAGA;  // vrednost izgube
	private static final int NEODLOC = 0;  // vrednost neodločene igre	
	
	
	private static Random random = new Random ();
	
	public Inteligenca (String ime) {
		super(ime);
	}
	
	
	public Koordinati izberiPotezo (Igra igra, String algoritem, int globina, boolean zamik, int dolzinaZamika) {
				
				// poskuša najti takojšnjo zmago
				Koordinati r = poisciZmago(igra);
				if (r != null) return r;
				
				// poskuša najti takojšnji poraz, da ga prepreči
				Koordinati q = poisciPoraz(igra);
				if (q != null) return q;
				
				// pogleda izbran algoritem in igra potezo, ki jo izbere
				switch (algoritem) {
				case "Minimax": {
					List<OcenjenaPoteza> ocenjenePoteze = randomMinimax(igra, globina);
					Koordinati potezaM = vrniPotezo(ocenjenePoteze); return potezaM;
				}
				case "AlfaBeta": {Koordinati potezaA = alphabeta(igra, globina, ZGUBA, ZMAGA, igra.naPotezi, 0).poteza; return potezaA;}
				case "Naiven": Koordinati potezaN = naiven(igra); return potezaN;
				default: return null;
				}
		
	}
			
	// algoritem alfabeta
	public static OcenjenaPoteza alphabeta(Igra igra, int globina, int alpha, int beta, Igralec jaz, int zavlacuj) {
		int ocena;

		if (igra.naPotezi == jaz) {ocena = ZGUBA;} else {ocena = ZMAGA;}
		
		// alfabeta sva izboljšala tako, da poteze išče samo na poljih, ki imajo vsaj enega od sosedov že zapolnjenega. Tako se prihrani dragocen čas.
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
				else ocenap = alphabeta (kopijaIgre, globina-1, alpha, beta, jaz, zavlacuj + 1).ocena;
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
	
	
	// algoritem naiven, ki naključno izbere potezo iz seznama vseh možnih
	public Koordinati naiven(Igra igra) {
		List<Koordinati> moznePoteze = igra.poteze();
		int randomIndex = random.nextInt(moznePoteze.size());
		return moznePoteze.get(randomIndex);
	}
	
	
	// algoritem minimax, ki naključno izbere eno izmed najboljših potez in jo odigra
	public static List<OcenjenaPoteza> randomMinimax(Igra igra, int globina) {
		NajboljseOcenjenePoteze najboljsePoteze = new NajboljseOcenjenePoteze();
		List<Koordinati> moznePoteze = igra.poteze();
		for (Koordinati p: moznePoteze) {
			Igra kopijaIgre = new Igra(igra); 
			kopijaIgre.odigraj (p);	//poskusimo vsako potezo v novi kopiji igre
			int ocena;
			switch (kopijaIgre.stanje()) {
			case ZMAGA_B:
			case ZMAGA_W: ocena = ZMAGA; break; // p je zmagovalna poteza
			case NEODLOCENO: ocena = NEODLOC; break;
			default: //nekdo je na potezi
				if (globina==1) ocena = OceniPozicijo.oceniPozicijo(kopijaIgre,igra.naPotezi);
				else ocena = //negacija ocene z vidike dgrugega igralca
						-randomMinimax(kopijaIgre,globina-1).get(0).ocena;  
			}
			najboljsePoteze.addIfBest(new OcenjenaPoteza(p, ocena));	
		}
		return najboljsePoteze.list();
	}
	
	// funkcija, ki minimaxu pomaga naključno izbrati potezo iz nabora najboljših potez
	public static Koordinati vrniPotezo(List<OcenjenaPoteza> ocenjenePoteze) {
		int i = random.nextInt(ocenjenePoteze.size());	
		return ocenjenePoteze.get(i).poteza;	
	}
	
	
	// funkcija, ki poišče zmago v potezi (da ne razmišlja predolgo, če lahko takoj zmaga)
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
	
	// funkcija, ki poišče, ali lahko nasprotnik zmaga v naslednji potezi (in potem ustrezno zablokira to zmago)
	public static Koordinati poisciPoraz(Igra igra) {
		Igra kopijaIgre = new Igra(igra);
		kopijaIgre.naPotezi = kopijaIgre.naPotezi.nasprotnik();
		Koordinati p = poisciZmago(kopijaIgre);
		return p;
	}
}

