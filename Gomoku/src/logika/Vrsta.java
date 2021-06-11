package logika;

import java.util.Arrays;

// objekt, ki predstavlja eno vrsto na plošči
public class Vrsta {
	
	// vrsta je predstavljena z dvema tabelama x in y koordinat
	public int[] x;
	public int[] y;
	
	public Vrsta(int[] x, int y[]) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "Vrsta [x=" + Arrays.toString(x) + ", y=" + Arrays.toString(y) + "]";
	}
}
