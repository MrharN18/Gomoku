package vodja;

import java.util.Random;
import java.util.Map;
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
		
	public static void igramoNovoIgro () {
		igra = new Igra(okno.velikost);
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
				igrajRacunalnikovoPotezo ();
				break;
			}
		}
	}
	
	private static Random random = new Random ();
	
//	public static void igrajRacunalnikovoPotezo() {
//		List<Koordinati> moznePoteze = igra.poteze();
//		try {TimeUnit.SECONDS.sleep(2);} catch (Exception e) {};
//		int randomIndex = random.nextInt(moznePoteze.size());
//		Koordinati poteza = moznePoteze.get(randomIndex);
//		igra.odigraj(poteza);
//		igramo ();
//	}
	
//	public static void igrajRacunalnikovoPotezo() {
//		Igra zacetkaIgra = igra;
//		SwingWorker<Koordinati, Void> worker = new SwingWorker<Koordinati, Void> () {
//			@Override
//			protected Koordinati doInBackground() {
//				try {TimeUnit.SECONDS.sleep(1);} catch (Exception e) {};
//				List<Koordinati> moznePoteze = igra.poteze();
//				int randomIndex = random.nextInt(moznePoteze.size());
//				return moznePoteze.get(randomIndex);
//			}
//			@Override
//			protected void done () {
//				Koordinati poteza = null;
//				try {poteza = get();} catch (Exception e) {};
//				if (igra == zacetkaIgra) {
//					igra.odigraj(poteza);
//					igramo ();
//				}
//			}
//		};
//		worker.execute();
//	}
	
	
	public static Inteligenca racunalnikovaInteligenca = new Inteligenca("nevem");
	
	public static void igrajRacunalnikovoPotezo() {
		Igra zacetkaIgra = igra;
		SwingWorker<Koordinati, Void> worker = new SwingWorker<Koordinati, Void> () {
			@Override
			protected Koordinati doInBackground() {
				Koordinati poteza = racunalnikovaInteligenca.izberiPotezo(igra);
				try {TimeUnit.SECONDS.sleep(0);} catch (Exception e) {};
				return poteza;
			}
			@Override
			protected void done () {
				Koordinati poteza = null;
				try {poteza = get();} catch (Exception e) {};
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


}
