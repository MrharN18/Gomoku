// import paketov
package vodja;

import java.util.Map;
import java.util.EnumMap;

import javax.swing.SwingWorker;
import java.util.concurrent.TimeUnit;

import gui.Okno;
import inteligenca.Inteligenca;
import logika.Igra;
import logika.Igralec;
import splosno.Koordinati;

public class Vodja {	
	
	// za vsakega igralca naredi slovar tipa tega igralca
	public static Map<Igralec,VrstaIgralca> vrstaIgralca;
	
	// okno
	public static Okno okno;
	
	// igra, katere vrednost nastavimo na null
	public static Igra igra = null;
	
	// spremenljivko uporabimo, da Swingu povemo kdaj naj uposteva pritiske na racunalniski miski
	public static boolean clovekNaVrsti = false;
	
	// za vsakega igralca slovar imen igralcev
	public static EnumMap<Igralec, String> igralecName;
	
	// za vsakega igralca algoritem (če je ta igralec računalnik)
	public static EnumMap<Igralec, String> aiAlgorithm;
	
	// default globina in zamik, takoj ko odpremo program
	public static int globina_B = 3; // globina je dolocena za vsakega igralca posebej
    	public static int globina_W = 3;
    	public static int zamik_ms = 1000;
   	public static boolean zamik = false; // ali bomo imeli zamik ali ne
	
	// default imena igralcev in algoritmov
	static {
	igralecName = new EnumMap<Igralec, String>(Igralec.class);
        igralecName.put(Igralec.B, "Igralec B");
        igralecName.put(Igralec.W, "Igralec W");

        aiAlgorithm = new EnumMap<Igralec, String>(Igralec.class);
        aiAlgorithm.put(Igralec.B, "AlfaBeta");
        aiAlgorithm.put(Igralec.W, "AlfaBeta");
	}
	
	
	// ustvari novo igro, glede na izbrano velikost v polju
	public static void igramoNovoIgro () {
		igra = new Igra(okno.polje.N);
		igramo();
	}
	
	// funkcija, ki skrbi da igra poteka nemoteno
	public static void igramo () {
		okno.osveziGUI();
		switch (igra.stanje()) {
		case ZMAGA_B:
		case ZMAGA_W: 
		case NEODLOCENO: 
			return; // odhajamo iz metode igramo
		case V_TEKU: 
			Igralec igralec = igra.naPotezi;
			VrstaIgralca vrstaNaPotezi = vrstaIgralca.get(igralec);
			switch (vrstaNaPotezi) {
			case C: 
				clovekNaVrsti = true;
				break;
			case R:
				igrajRacunalnikovoPotezo();
				break;
			}
		}
	}
	
	// swingworker skrbi za delovanje v ozadju
	private static SwingWorker<Koordinati, Void> worker = null;
	
	public static Inteligenca racunalnikovaInteligenca = new Inteligenca("nevem");
	
	// na vrsti je računalnik
	public static void igrajRacunalnikovoPotezo() {
			Igra zacetkaIgra = igra;
						
			worker = new SwingWorker<Koordinati, Void>() {
				@Override
				protected Koordinati doInBackground() {
					// v ozadju zažene izbrani algoritem izbrane globine, ki potem najde najboljšo potezo
					Koordinati poteza = null;
					switch (igra.naPotezi) {
					case B: {poteza = racunalnikovaInteligenca.izberiPotezo(igra,aiAlgorithm.get(zacetkaIgra.naPotezi), globina_B); break;}
					case W: {poteza = racunalnikovaInteligenca.izberiPotezo(igra,aiAlgorithm.get(zacetkaIgra.naPotezi), globina_W); break;}
					} // ce imamo zamik, racunalnik pocaka preden odigra potezo
					try { if (zamik) {TimeUnit.SECONDS.sleep(zamik_ms / 1000);} else {TimeUnit.SECONDS.sleep(0);}} catch (Exception e) {};
					return poteza;
				}
				@Override
				protected void done() {
					// ko algoritem konča, potezo vrne, če ga ne prekinemo vmes (s tipko razveljavi)
					Koordinati poteza = null;
					try {poteza = get();} catch (Exception e) {
						e.printStackTrace();
						return;
					}
					if (igra == zacetkaIgra) {
						igra.odigraj(poteza);
						igramo ();
					}
				}
			};
			worker.execute();
		}
	
	// na vrsti je človek
	public static void igrajClovekovoPotezo(Koordinati poteza) {
		if (igra.odigraj(poteza)) clovekNaVrsti = false;
		igramo ();
	}
	
	// metoda prekine delovanje swingworkerja in razveljavi zadnjo odigrano potezo
	public static void undo() {
	    if (worker != null) {
	        worker.cancel(false);
	    }
	    igra.razveljavi();
	    igramo(); 
	}

}