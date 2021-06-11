// razni paketi 
package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import vodja.Vodja;

import logika.Polje;
import logika.Vrsta;
import splosno.Koordinati;


@SuppressWarnings("serial")
public class Platno extends JPanel implements MouseListener{
	
	// barve igralcev, ozadja, polja, potez,...
	Color barvaB;
	Color barvaW;
	Color ozadje = Color.WHITE;
	Color plosca = Color.GREEN;
	Color zadnjaPoteza = Color.RED;
	Color zmagovalnaVrsta = Color.LIGHT_GRAY;
	
	// velikost igre
	public int N;
	
	// funkcija, ki spremeni barvo ozadja
	public void spremeniOzadje(Color ozadje) {
		setBackground(ozadje);
	}
	
	
	
	public Platno(int velikost) {
		setBackground(ozadje);
		this.addMouseListener(this);
		barvaB =Color.BLACK;
		barvaW = Color.WHITE;
		N = velikost;
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(800, 800);
	}

	// relativna širina črte
	private final static double LINE_WIDTH = 0.05;
		
	// širina enega kvadratka
	private double squareWidth() {
		return Math.min(getWidth(), getHeight()) / (double) N - 0.03 * (Math.min(getWidth(), getHeight()) / (double) N);
	}
	
	// relativni prostor okoli B in W
	private final static double PADDING = 0.07;
	
	// nariše krogec belega igralca na koordinate (i,j)
	private void paintB(Graphics2D g2, int i, int j) {
		double w = squareWidth();
		double dx = (getWidth()/2.0)- ((N/2.0) * w);
		double dy = (getHeight()/2.0)- ((N/2.0) * w);
		
		double d = w * (1.0 - LINE_WIDTH - 2.0 * PADDING); // premer O
		double x = w * (i + 0.5 * LINE_WIDTH + PADDING) + dx;
		double y = w * (j + 0.5 * LINE_WIDTH + PADDING) + dy;
		g2.setColor(barvaB);
		g2.setStroke(new BasicStroke((float) (w * LINE_WIDTH)));
		g2.fillOval((int)x, (int)y, (int)d , (int)d);
		g2.setColor(Color.BLACK);
		g2.drawOval((int)x, (int)y, (int)d , (int)d);
	}
	
	// nariše krogec črnega igralca na koordinate (i,j)
	private void paintW(Graphics2D g2, int i, int j) {
		double w = squareWidth();
		double dx = (getWidth()/2.0)- ((N/2.0) * w);
		double dy = (getHeight()/2.0)- ((N/2.0) * w);
		
		double d = w * (1.0 - LINE_WIDTH - 2.0 * PADDING); // premer O
		double x = w * (i + 0.5 * LINE_WIDTH + PADDING) + dx;
		double y = w * (j + 0.5 * LINE_WIDTH + PADDING) + dy;
		g2.setColor(barvaW);
		g2.setStroke(new BasicStroke((float) (w * LINE_WIDTH)));
		g2.fillOval((int)x, (int)y, (int)d , (int)d);
		g2.setColor(Color.BLACK);
		g2.drawOval((int)x, (int)y, (int)d , (int)d);
	}
	
	// poudari zadnjo potezo za boljšo preglednost
	private void paintZadnjaPoteza(Graphics2D g2, Koordinati zadnja) {
		if (zadnja != null) {
			int i = zadnja.getX();
			int j = zadnja.getY();
			
			double w = squareWidth();
			double dx = (getWidth()/2.0)- ((N/2.0) * w);
			double dy = (getHeight()/2.0)- ((N/2.0) * w);
			
			double d = w * (1.0 - LINE_WIDTH - 2.0 * PADDING); // premer O
			double x = w * (i + 0.5 * LINE_WIDTH + PADDING) + dx;
			double y = w * (j + 0.5 * LINE_WIDTH + PADDING) + dy;
			
			g2.setStroke(new BasicStroke((float) (1.5 * w * LINE_WIDTH)));
			g2.setColor(zadnjaPoteza);
			g2.drawOval((int)x, (int)y, (int)d , (int)d);
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;

		double w = squareWidth();
		double dx = (getWidth()/2.0)- ((N/2.0) * w);
		double dy = (getHeight()/2.0)- ((N/2.0) * w);

		// plošča
		g2.setColor(plosca);
		g2.fillRect( (int) dx, (int) dy, (int) (N * w), (int) (N * w));
		
		// če imamo zmagovalno terico, njeno ozadje pobarvamo
		Vrsta t = null;
		if (Vodja.igra != null) {t = Vodja.igra.zmagovalnaVrsta();}
		if (t != null) {
			g2.setColor(zmagovalnaVrsta);
			for (int k = 0; k < 5; k++) {
				int i = t.x[k];
				int j = t.y[k];
				g2.fillRect((int)(w * i + dx), (int)(w * j + dy), (int)w, (int)w);
			}
		}
	
		// črte
		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke((float) (w * LINE_WIDTH)));
		for (int i = 0; i <= N; i++) {
			g2.drawLine((int)(i * w + dx),
					    (int)(0 + dy),
					    (int)(i * w + dx),
					    (int)(N * w + dy));
			g2.drawLine((int)(0 + dx),
					    (int)(i * w + dy),
					    (int)(N * w + dx),
					    (int)(i * w + dy));
		}	
		
		// narise odigrano potezo
		Polje[][] plosca;;
		if (Vodja.igra != null) {
			plosca = Vodja.igra.getPlosca();
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					switch(plosca[i][j]) {
					case B: paintB(g2, i, j); paintZadnjaPoteza(g2, Vodja.igra.zadnjaPoteza); break;
					case W: paintW(g2, i, j); paintZadnjaPoteza(g2, Vodja.igra.zadnjaPoteza); break;
					default: break;
					}
				}
			}
		}	
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
		// ob kliku na polje igramo potezo, ce je na vrsti clovek
		if (Vodja.clovekNaVrsti) {
			int x = e.getX();
			int y = e.getY();
			int w = (int)(squareWidth());
			int dx = (int)((getWidth()/2.0)- ((N/2.0) * w));
			int dy = (int)((getHeight()/2.0)- ((N/2.0) * w));
			int i = (x - dx)/w;
			double di = ((x-dx) % w) / squareWidth() ;
			int j = (y - dy)/w;
			double dj = ((y-dy) % w) / squareWidth() ;
			if (0 <= i && i < N &&
					0.5 * LINE_WIDTH < di && di < 1.0 - 0.5 * LINE_WIDTH &&
					0 <= j && j < N && 
					0.5 * LINE_WIDTH < dj && dj < 1.0 - 0.5 * LINE_WIDTH) {
				Vodja.igrajClovekovoPotezo (new Koordinati(i, j));
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {		
	}

	@Override
	public void mouseReleased(MouseEvent e) {		
	}

	@Override
	public void mouseEntered(MouseEvent e) {		
	}

	@Override
	public void mouseExited(MouseEvent e) {		
	}
}