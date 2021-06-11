package logika;


// možni igralci.

public enum Igralec {
	W, B;

	public Igralec nasprotnik() {
		return (this == W ? B : W);
	}

	public Polje getPolje() {
		return (this == W ? Polje.W : Polje.B);
	}
}
