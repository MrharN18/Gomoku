package vodja;

// enum razred doloca mozne vrste igralca
public enum VrstaIgralca {
	R, C; 

	// to metodo uporabi print, ce mora izpisati element razreda
	@Override
	public String toString() {
		switch (this) {
		case C: return "človek";
		case R: return "računalnik";
		default: assert false; return "";
		}
	}

}
