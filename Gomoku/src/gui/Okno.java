package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumMap;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import vodja.Vodja;
//import logika.Igra;
import logika.Igralec;
import vodja.VrstaIgralca;

@SuppressWarnings("serial")
public class Okno extends JFrame implements ActionListener{
	
	private Platno polje;
	
	private JLabel status;
	
	private JMenuItem igraClovekRacunalnik, igraRacunalnikClovek, igraClovekClovek, igraRacunalnikRacunalnik;
	private JMenuItem velikostPlosce, barvaZetonov, algoritem, ime;
	private JButton razveljavi;
	
	public int velikost;
	
	public Okno(int N) {
		super();
		velikost = N;
		this.setTitle("Gomoku");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new GridBagLayout());
		
		JMenuBar menubar = new JMenuBar();
        setJMenuBar(menubar);
        
        JMenu menuigra = dodajMenu(menubar, "Nova igra");
        JMenu menunastavitve = dodajMenu(menubar, "Nastavitve");
        
        igraClovekRacunalnik = dodajMenuItem(menuigra, "Človek – računalnik");
        igraRacunalnikClovek = dodajMenuItem(menuigra, "Računalnik – človek");
        igraClovekClovek = dodajMenuItem(menuigra, "Človek – človek");
        igraRacunalnikRacunalnik = dodajMenuItem(menuigra, "Računalnik – računalnik");
        
        velikostPlosce = dodajMenuItem(menunastavitve, "Velikost plosce");
        barvaZetonov = dodajMenuItem(menunastavitve, "Barva zetonov");
        algoritem = dodajMenuItem(menunastavitve, "Algoritem");
        ime = dodajMenuItem(menunastavitve, "Ime");
        
        razveljavi = new JButton("Razveljavi");
        menubar.add(razveljavi);
        razveljavi.addActionListener(this);
        
        polje = new Platno(velikost);
        
        GridBagConstraints polje_layout = new GridBagConstraints();
		polje_layout.gridx = 0;
		polje_layout.gridy = 0;
		polje_layout.fill = GridBagConstraints.BOTH;
//		polje_layout.anchor = GridBagConstraints.CENTER;
		polje_layout.weightx = 1.0;
		polje_layout.weighty = 1.0;
		getContentPane().add(polje, polje_layout);
		
		status = new JLabel();
		status.setFont(new Font(status.getFont().getName(),
							    status.getFont().getStyle(),
							    20));
		GridBagConstraints status_layout = new GridBagConstraints();
		status_layout.gridx = 0;
		status_layout.gridy = 1;
		status_layout.anchor = GridBagConstraints.CENTER;
		getContentPane().add(status, status_layout);
		
		status.setText("Izberite igro!");
		
	}
	
	public JMenu dodajMenu(JMenuBar menubar, String naslov) {
        JMenu menu = new JMenu(naslov);
        menubar.add(menu);
        return menu;
    }

    public JMenuItem dodajMenuItem(JMenu menu, String naslov) {
        JMenuItem menuitem = new JMenuItem(naslov);
        menu.add(menuitem);
        menuitem.addActionListener(this);
        return menuitem;
    }
    
    
    @Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == igraClovekRacunalnik) {
			Vodja.vrstaIgralca = new EnumMap<Igralec,VrstaIgralca>(Igralec.class);
			Vodja.vrstaIgralca.put(Igralec.B, VrstaIgralca.C); 
			Vodja.vrstaIgralca.put(Igralec.W, VrstaIgralca.R);
			Vodja.igramoNovoIgro();
		} else if (e.getSource() == igraRacunalnikClovek) {
			Vodja.vrstaIgralca = new EnumMap<Igralec,VrstaIgralca>(Igralec.class);
			Vodja.vrstaIgralca.put(Igralec.B, VrstaIgralca.R); 
			Vodja.vrstaIgralca.put(Igralec.W, VrstaIgralca.C);
			Vodja.igramoNovoIgro();
		} else if (e.getSource() == igraClovekClovek) {
			Vodja.vrstaIgralca = new EnumMap<Igralec,VrstaIgralca>(Igralec.class);
			Vodja.vrstaIgralca.put(Igralec.B, VrstaIgralca.C); 
			Vodja.vrstaIgralca.put(Igralec.W, VrstaIgralca.C);
			Vodja.igramoNovoIgro();
		} else if (e.getSource() == igraRacunalnikRacunalnik) {
			Vodja.vrstaIgralca = new EnumMap<Igralec,VrstaIgralca>(Igralec.class);
			Vodja.vrstaIgralca.put(Igralec.B, VrstaIgralca.R); 
			Vodja.vrstaIgralca.put(Igralec.W, VrstaIgralca.R);
			Vodja.igramoNovoIgro();
		} else if (e.getSource() == velikostPlosce) {
			String velikostPlosce = JOptionPane.showInputDialog(this, "Velikost plosce:");
            if (velikostPlosce != null && velikostPlosce.matches("\\d+") && Integer.parseInt(velikostPlosce) > 4) {
            	velikost = Integer.parseInt(velikostPlosce);
            	polje.N = Integer.parseInt(velikostPlosce);
            	vodja.Vodja.igra = null;
            	status.setText("Izberite igro!");
                polje.repaint();
            }  
		} else if (e.getSource() == algoritem) {
			
		} else if (e.getSource() == barvaZetonov) {
			Color barvaB = JColorChooser.showDialog(this, "Izberi barvo B:", polje.barvaB);
            if (barvaB != null) {
                polje.barvaB = barvaB;
                polje.repaint();
            }
            Color barvaW = JColorChooser.showDialog(this, "Izberi barvo W:", polje.barvaW);
            if (barvaW != null) {
                polje.barvaW = barvaW;
                polje.repaint();
            }
		} else if (e.getSource() == ime) {
			
		} else if (e.getSource() == razveljavi) {
			vodja.Vodja.igra.razveljavi();
			polje.repaint();
		}
		
	}

	public void osveziGUI() {
		if (Vodja.igra == null) {
			status.setText("Igra ni v teku.");
		}
		else {
			switch(Vodja.igra.stanje()) {
			case NEODLOCENO: status.setText("Neodločeno!"); break;
			case V_TEKU: 
				status.setText("Na potezi je " + Vodja.igra.naPotezi + 
						" - " + Vodja.vrstaIgralca.get(Vodja.igra.naPotezi)); 
				break;
			case ZMAGA_B: 
				status.setText("Zmagal je B - " + 
						Vodja.vrstaIgralca.get(Vodja.igra.naPotezi.nasprotnik()));
				break;
			case ZMAGA_W: 
				status.setText("Zmagal je W - " + 
						Vodja.vrstaIgralca.get(Vodja.igra.naPotezi.nasprotnik()));
				break;
			}
		}
		polje.repaint();
	}

}
