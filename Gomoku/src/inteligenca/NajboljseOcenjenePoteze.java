package inteligenca;

import java.util.List;
import java.util.LinkedList;
import inteligenca.OcenjenaPoteza;

public class NajboljseOcenjenePoteze {
	
	// deklariramo seznam ocenjenih potez v katerega bomo shranjevali najboljse poteze
	private LinkedList<OcenjenaPoteza> buffer;
	
	// metoda, ki inicializira vrednost seznama
	public NajboljseOcenjenePoteze() {
		this.buffer = new LinkedList<OcenjenaPoteza> ();
	}
	
	// ce je poteza boljsa od vseh potez v seznamu jo dodamo in ostale poteze izbrisemo iz seznama, ce je poteza slabsa je v seznam ne dodamo
	public void addIfBest(OcenjenaPoteza ocenjenaPoteza) {
		if (buffer.isEmpty()) buffer.add(ocenjenaPoteza);
		else {
			OcenjenaPoteza op = buffer.getFirst(); // prvo potezo v seznamu uporabimo za primerjavo
			switch (ocenjenaPoteza.compareTo(op)) {
			case 1: 
				buffer.clear();  // ocenjenaPoteza > op
			case 0: // ali 1
				buffer.add(ocenjenaPoteza); // ocenjenaPoteza >= op
			}			
		}
	}
	
	// iz LinkedList naredimo List
	public List<OcenjenaPoteza> list() {
		return (List<OcenjenaPoteza>) buffer;
	}

}
