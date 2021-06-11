package inteligenca;

import splosno.Koordinati;

// razred, ki predstavi potezo skupaj z njeno oceno
public class OcenjenaPoteza {
	
	Koordinati poteza;
	int ocena;
	
	public OcenjenaPoteza (Koordinati poteza, int ocena) {
		this.poteza = poteza;
		this.ocena = ocena;
	}
	
	// primerjava dveh ocenjenih potez
	public int compareTo (OcenjenaPoteza op) {
		if (this.ocena < op.ocena) return -1;
		else if (this.ocena > op.ocena) return 1;
		else return 0;
	}

}