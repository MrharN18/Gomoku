package logika;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import splosno.Koordinati;

public class Igra {
	
	// velikost igre
	public int N;
	
	// plošča
	public Polje[][] plosca;
	
	// kdo je na potezi
	public Igralec naPotezi;
	
	// koordinati zadnje poteze (da jo poudari)
	public Koordinati zadnjaPoteza;
	
	// seznam vseh zmagovalnih vrst
	public static List<Vrsta> VRSTE;
	
	// seznam v katerega se shranjujejo odigrane poteze
	public ArrayList<Koordinati> odigranePoteze;
	
	// stanje igre (v teku, zmaga,...)
	protected Stanje stanje = null;
	
	// funkcija, ki glede na velikost igre določi vse zmagovalne vrste
	public static void zmagovalneVrste(int N) {
		int[][] smer = {{1,0}, {0,1}, {1,1}, {1,-1}};
		for (int x = 0; x < N; x++) {
			for (int y = 0; y < N; y++) {
				for (int[] s : smer) {
					int dx = s[0];
					int dy = s[1];
					if ((0 <= x + (4) * dx) && (x + (4) * dx < N) && 
						(0 <= y + (4) * dy) && (y + (4) * dy < N)) {
						int[] vrsta_x = new int[5];
						int[] vrsta_y = new int[5];
						for (int k = 0; k < 5; k++) {
							vrsta_x[k] = x + dx * k;
							vrsta_y[k] = y + dy * k;
						}
						VRSTE.add(new Vrsta(vrsta_x, vrsta_y));
					}
				}
			}
		}
	}
	
	// konstruktor z velikostjo za parameter
	public Igra(int N) {
		VRSTE = new LinkedList<Vrsta>();
		zadnjaPoteza = null;
		this.N = N;
		plosca = new Polje[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				plosca[i][j] = Polje.PRAZNO;
			}
		}
		naPotezi = Igralec.B; // prvi je na potezi igralec B
		zmagovalneVrste(N);
		odigranePoteze = new ArrayList<Koordinati>(); 
		
	}
	
	// privzeta igra velikosti 15x15
	public Igra() {
		VRSTE = new LinkedList<Vrsta>();
		zadnjaPoteza = null;
		this.N = 15;
		plosca = new Polje[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				plosca[i][j] = Polje.PRAZNO;
			}
		}
		naPotezi = Igralec.B;
		zmagovalneVrste(N);
		odigranePoteze = new ArrayList<Koordinati>(); 
	}
	
	// igra, ki je dvojnik neke že igrane igre v teku
	public Igra(Igra igra) {
		zadnjaPoteza = null;
		this.N = igra.N;
		
		this.plosca = new Polje[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				this.plosca[i][j] = igra.plosca[i][j];
			}
		}
		
		this.naPotezi = igra.naPotezi;
		odigranePoteze = new ArrayList<Koordinati>(igra.odigranePoteze); 
	}
	
	// metoda vrne igralno plosco
	public Polje[][] getPlosca () {
		return plosca;
	}
	
	// vse možne poteze
	public List<Koordinati> poteze() {
		LinkedList<Koordinati> ps = new LinkedList<Koordinati>();
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (plosca[i][j] == Polje.PRAZNO) {
					ps.add(new Koordinati(i, j));
				}
			}
		}
		return ps;
	}
	
	// seznam potez samo okoli polj, ki so že zasedena (za hitrejše delo inteligence)
	public List<Koordinati> poteze_omejeno(){
		LinkedList<Koordinati> ps = new LinkedList<Koordinati>();
		
		int[][] smer = {{1,0}, {0,1}, {1,1}, {1,-1}, {-1,0}, {0,-1}, {-1,-1}, {-1,1}};
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (plosca[i][j] == Polje.PRAZNO) {
					for (int[] s : smer) {
						int dx = s[0];
						int dy = s[1];
						if (dx + i > N-1 || dx + i < 0) dx = 0;
						if (dy + j > N-1 || dy + j < 0) dy = 0;
						
						if (plosca[i + dx][j + dy] != Polje.PRAZNO) {ps.add(new Koordinati(i, j));break;}
					}
				}
			}
		}
		if (ps.size() == 0) {ps.add(new Koordinati(N/2,N/2));}
		return ps;
	}
	
	// odigraj potezo (postavi plošček na koordinate p)
	// metoda vrne true, ce je bila poteza uspesno odigrana, sicer vrne false
	public boolean odigraj(Koordinati p) {
		if (p != null) {
			if (plosca[p.getX()][p.getY()] == Polje.PRAZNO) {
				plosca[p.getX()][p.getY()] = naPotezi.getPolje();
				naPotezi = naPotezi.nasprotnik();
				odigranePoteze.add(p);
				zadnjaPoteza = p;
				return true;
			}
			else {
				return false;
			}
		}
		return false;
		
	}
	
	// vrne zadnjo potezo, ce potez ni vrne null
	public Koordinati zadnja_poteza() {
        if (odigranePoteze.isEmpty()) {
            return null;
        } else {
            return odigranePoteze.get(odigranePoteze.size() - 1);
        }
    }
	
	// razveljavi zadnjo odigrano potezo
	public void razveljavi() {	
		Koordinati zadnja = zadnja_poteza();
		if (zadnja != null && stanje == Stanje.V_TEKU) {   // potezo lahko razveljavimo le, ce je igra v teku
			plosca[zadnja.getX()][zadnja.getY()] = Polje.PRAZNO;
			odigranePoteze.remove(odigranePoteze.size() - 1);
			naPotezi = naPotezi.nasprotnik();
			zadnjaPoteza = zadnja_poteza();
		}
		
	}
	
	// pogleda, če je kdo že zmagal in vrne igralca, ki je zmagovalec
	private Igralec cigavaVrsta(Vrsta t) {
		int count_W = 0;
		int count_B = 0;
		for (int k = 0; k < 5 && (count_W == 0 || count_B == 0); k++) {
			switch (plosca[t.x[k]][t.y[k]]) {
			case W: count_W += 1; break;
			case B: count_B += 1; break;
			case PRAZNO: break;
			}
		}
		if (count_W == 5) { return Igralec.W; }
		else if (count_B == 5) { return Igralec.B; }
		else { return null; }
	}

	// pogleda katera vrsta je zmagala
	public Vrsta zmagovalnaVrsta() {
		for (Vrsta t : VRSTE) {
			Igralec lastnik = cigavaVrsta(t);
			if (lastnik != null) return t;
		}
		return null;
	}
	
	// vrne trenutno stanje igre	
	public Stanje stanje() {
		// ali imamo zmagovalca
		Vrsta t = zmagovalnaVrsta();
		if (t != null) {
			switch (plosca[t.x[0]][t.y[0]]) {
			case W:
				stanje = Stanje.ZMAGA_W;
				return Stanje.ZMAGA_W; 
			case B: 
				stanje = Stanje.ZMAGA_B;
				return Stanje.ZMAGA_B;
			case PRAZNO: assert false;
			}
		}
		// ali imamo kaksno prazno polje
		// ce ga imamo, igre ni konec in je nekdo na potezi
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (plosca[i][j] == Polje.PRAZNO) {
					stanje = Stanje.V_TEKU;
					return Stanje.V_TEKU;}
			}
		}
		// plosca je polna, rezultat je neodlocen
		stanje = Stanje.NEODLOCENO;
		return Stanje.NEODLOCENO;
	}
}
