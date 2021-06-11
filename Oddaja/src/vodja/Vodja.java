package vodja;

import java.util.Map;
import java.awt.Color;
import java.util.EnumMap;
import java.util.List;

import javax.swing.SwingWorker;
import java.util.concurrent.TimeUnit;

import gui.Okno;
import inteligenca.Inteligenca;
import logika.Igra;
import logika.Igralec;
import splosno.Koordinati;

public class Vodja {	
	
	public static Map<Igralec,VrstaIgralca> vrstaIgralca;
	
	public static Okno okno;
	
	public static Igra igra = null;
	
	public static boolean clovekNaVrsti = false;
	
	public static EnumMap<Igralec, String> igralecName;
	public static EnumMap<Igralec, String> aiAlgorithm;
	
	public static int globina_B = 3;
    public static int globina_W = 3;
    public static int zamik_ms = 1000;
    public static boolean zamik = false;
	
	static {
		igralecName = new EnumMap<Igralec, String>(Igralec.class);
        igralecName.put(Igralec.B, "Igralec B");
        igralecName.put(Igralec.W, "Igralec W");

        aiAlgorithm = new EnumMap<Igralec, String>(Igralec.class);
        aiAlgorithm.put(Igralec.B, "AlfaBeta");
        aiAlgorithm.put(Igralec.W, "AlfaBeta");
	}

		
	public static void igramoNovoIgro () {
		igra = new Igra(okno.polje.N);
		igramo();
	}
	
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
	
	private static SwingWorker<Koordinati, Void> worker = null;
	
	public static Inteligenca racunalnikovaInteligenca = new Inteligenca("nevem");
		
	public static void igrajRacunalnikovoPotezo() {
			Igra zacetkaIgra = igra;
						
			worker = new SwingWorker<Koordinati, Void>() {
				@Override
				protected Koordinati doInBackground() {
					Koordinati poteza = null;
					switch (igra.naPotezi) {
					case B: {poteza = racunalnikovaInteligenca.izberiPotezo(igra,aiAlgorithm.get(zacetkaIgra.naPotezi), globina_B); break;}
					case W: {poteza = racunalnikovaInteligenca.izberiPotezo(igra,aiAlgorithm.get(zacetkaIgra.naPotezi), globina_W); break;}
					}
					try { if (zamik) {TimeUnit.SECONDS.sleep(zamik_ms / 1000);} else {TimeUnit.SECONDS.sleep(0);}} catch (Exception e) {};
					return poteza;
				}
				@Override
				protected void done() {
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
		
	public static void igrajClovekovoPotezo(Koordinati poteza) {
		if (igra.odigraj(poteza)) clovekNaVrsti = false;
		igramo ();
	}
	
	public static void undo() {
	    if (worker != null) {
	        worker.cancel(false);
	    }
	    igra.razveljavi();
	    igramo(); 
	}

}
