package logika;

import java.util.LinkedList;
import java.util.List;
import splosno.Koordinati;

public class Igra {
	
	public int N;
	
	public Polje[][] plosca;
	
	public Igralec naPotezi;
	
	public Koordinati zadnjaPoteza;
	
	public Koordinati predzadnjaPoteza;
	
	public static List<Vrsta> VRSTE;
	
	protected Stanje stanje = null;

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
	
	public Igra(int N) {
		VRSTE = new LinkedList<Vrsta>();
		predzadnjaPoteza = null;
		zadnjaPoteza = null;
		this.N = N;
		plosca = new Polje[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				plosca[i][j] = Polje.PRAZNO;
			}
		}
		naPotezi = Igralec.B;
		zmagovalneVrste(N);
	}
	
	public Igra() {
		VRSTE = new LinkedList<Vrsta>();
		predzadnjaPoteza = null;
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
	}
	
	public Igra(Igra igra) {
		predzadnjaPoteza = null;
		zadnjaPoteza = null;
		this.N = igra.N;
		
		this.plosca = new Polje[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				this.plosca[i][j] = igra.plosca[i][j];
			}
		}
		
		this.naPotezi = igra.naPotezi;
	}
	
	public Polje[][] getPlosca () {
		return plosca;
	}
	
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
	
	
	public boolean odigraj(Koordinati p) {
		if (plosca[p.getX()][p.getY()] == Polje.PRAZNO) {
			plosca[p.getX()][p.getY()] = naPotezi.getPolje();
			naPotezi = naPotezi.nasprotnik();
			
			predzadnjaPoteza = zadnjaPoteza;
			zadnjaPoteza = p;
			
			return true;
		}
		else {
			return false;
		}
	}
	
	public void razveljavi() {
		if (predzadnjaPoteza != null && stanje == Stanje.V_TEKU) {
			plosca[zadnjaPoteza.getX()][zadnjaPoteza.getY()] = Polje.PRAZNO;
			plosca[predzadnjaPoteza.getX()][predzadnjaPoteza.getY()] = Polje.PRAZNO;
		}
	}
	
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

	
	public Vrsta zmagovalnaVrsta() {
		for (Vrsta t : VRSTE) {
			Igralec lastnik = cigavaVrsta(t);
			if (lastnik != null) return t;
		}
		return null;
	}
	
	
	public Stanje stanje() {
		
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

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (plosca[i][j] == Polje.PRAZNO) {
					stanje = Stanje.V_TEKU;
					return Stanje.V_TEKU;}
			}
		}
		stanje = Stanje.NEODLOCENO;
		return Stanje.NEODLOCENO;
	}
}
