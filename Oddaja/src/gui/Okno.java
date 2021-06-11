// uvoz potrebnih paketov

package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumMap;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import vodja.Vodja;
import logika.Igralec;
import vodja.VrstaIgralca;

@SuppressWarnings("serial")
public class Okno extends JFrame implements ActionListener{
	
	// platno, na katerem je narisana igra
	public Platno polje;
	
	// statusna vrstica
	private JLabel status;
	
	// zgornji meniji in gumb za razveljavitev
	private JMenuItem igraClovekRacunalnik, igraRacunalnikClovek, igraClovekClovek, igraRacunalnikRacunalnik;
	private JMenuItem velikostPlosce;
	private JButton razveljavi;
	private final JMenuItem settings;
	
	// pop-up meni za nastavitve
    private final JDialog s_platno;
    
    // gumb za shranjevanje nastavitev
    private final JButton s_shrani;
    
    // polji za ime igralcev
    private final JTextField s_B_name;
    private final JTextField s_W_name;
    
    // gumbi za spremembe barv in izbrane prikaz barve
    private final JButton s_B_colour_button;
    private final JLabel s_B_label;
    
    private final JButton s_W_colour_button;
    private final JLabel s_W_label;
    
    private final JButton s_plosca_colour_button;
    private final JLabel s_plosca_label;
    
    private final JButton s_ozadje_colour_button;
    private final JLabel s_ozadje_label;
    
    private final JButton s_poudarek_colour_button;
    private final JLabel s_poudarek_label;

    private final JButton s_zmagovalna_colour_button;
    private final JLabel s_zmagovalna_label;

    // zavesica za algoritme belega in črnega igralca
    private final JComboBox<String> s_ai1_algo;
    private final JComboBox<String> s_ai2_algo;

    // gumbi za globino algoritmov in zamik poteze racunalnika
    final JSpinner s_B_depth;
    final JSpinner s_W_depth;
    final JSpinner s_zamik_time;
    
    // kjukica za časovni zamik poteze računalnika (da ne odigra takoj)
    final JCheckBox s_omejitev_AI;
    
    // zacasne barve uporabimo zato, da se barva spremeni sele ko pritisnemo gumb shrani
    private Color t_B_colour;
    private Color t_W_colour;
    private Color t_plosca_colour;
    private Color t_ozadje_colour;
    private Color t_poudarek_colour;
    private Color t_zmagovalna_colour;

    // velikost nove igre (rabimo samo ko prvič zaženemo, da nariše platno)
    public int velikost;
	
    
    // ustvari novo glavno okno
	public Okno(int N) {
		super();
		velikost = N;
		this.setTitle("Gomoku");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new GridBagLayout());
		
		// vrstica z meniji
		JMenuBar menubar = new JMenuBar();
        setJMenuBar(menubar);
        
        // glavna menija
        JMenu menuigra = dodajMenu(menubar, "Nova igra");
        JMenu menunastavitve = dodajMenu(menubar, "Nastavitve");
        
        // podmeniji igre
        igraClovekRacunalnik = dodajMenuItem(menuigra, "Človek – računalnik");
        igraRacunalnikClovek = dodajMenuItem(menuigra, "Računalnik – človek");
        igraClovekClovek = dodajMenuItem(menuigra, "Človek – človek");
        igraRacunalnikRacunalnik = dodajMenuItem(menuigra, "Računalnik – računalnik");
        
        // podmenija nastavitev
        velikostPlosce = dodajMenuItem(menunastavitve, "Velikost plosce");
        settings = dodajMenuItem(menunastavitve,"Nastavitve");
        
        // gumb "razveljavi zadnjo potezo"
        razveljavi = new JButton("Razveljavi");
        final GridBagConstraints undo_layout = new GridBagConstraints();
        undo_layout.gridx = 0;
        undo_layout.gridy = 1;
        undo_layout.anchor = GridBagConstraints.EAST;
        getContentPane().add(razveljavi, undo_layout);
        razveljavi.addActionListener(this);
        
        // polje in postavitev polja v oknu
        polje = new Platno(velikost);
        
        GridBagConstraints polje_layout = new GridBagConstraints();
		polje_layout.gridx = 0;
		polje_layout.gridy = 0;
		polje_layout.fill = GridBagConstraints.BOTH;
		polje_layout.weightx = 1.0;
		polje_layout.weighty = 1.0;
		getContentPane().add(polje, polje_layout);
		
		
		// statusna vrstica in njena postavitev v oknu
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
			
	
		// pop-up meni za nastavitve
    	s_platno = new JDialog(this, "Nastavitve", /* modal */ true);
    	s_platno.setLayout(new GridBagLayout());

    	// Nastavitve imena in barve 1. igralca
    	final JLabel s_B_name_label = new JLabel("P1:");
    	final GridBagConstraints s_B_name_label_layout = new GridBagConstraints();
    	s_B_name_label_layout.gridx = 0;
    	s_B_name_label_layout.gridy = 0;
    	s_B_name_label_layout.anchor = GridBagConstraints.CENTER;
    	s_platno.add(s_B_name_label, s_B_name_label_layout);

	    s_B_name = new JTextField();
	    s_B_name.setColumns(15);
	    final GridBagConstraints s_B_name_layout = new GridBagConstraints();
	    s_B_name_layout.gridx = 1;
	    s_B_name_layout.gridy = 0;
	    s_B_name_layout.anchor = GridBagConstraints.WEST;
	    s_platno.add(s_B_name, s_B_name_layout);

	    s_B_colour_button = new JButton("Barva");
	    final GridBagConstraints s_B_colour_button_layout = new GridBagConstraints();
	    s_B_colour_button_layout.gridx = 2;
	    s_B_colour_button_layout.gridy = 0;
	    s_B_colour_button_layout.anchor = GridBagConstraints.CENTER;
	    s_platno.add(s_B_colour_button, s_B_colour_button_layout);
	    s_B_colour_button.addActionListener(this);
	    
	    s_B_label = new JLabel("     ");
	    s_B_label.setOpaque(true);
	    s_B_label.setBackground(polje.barvaB);
	    final GridBagConstraints s_B_label_layout = new GridBagConstraints();
	    s_B_label_layout.gridx = 3;
	    s_B_label_layout.gridy = 0;
	    s_B_label_layout.anchor = GridBagConstraints.WEST;
	    s_platno.add(s_B_label, s_B_label_layout);
	
	    // Nastavitve imena in barve 2. igralca
	    final JLabel s_W_name_label = new JLabel("P2:");
	    final GridBagConstraints s_W_name_label_layout = new GridBagConstraints();
	    s_W_name_label_layout.gridx = 0;
	    s_W_name_label_layout.gridy = 1;
	    s_W_name_label_layout.anchor = GridBagConstraints.CENTER;
	    s_platno.add(s_W_name_label, s_W_name_label_layout);
	
	    s_W_name = new JTextField();
	    s_W_name.setColumns(15);
	    final GridBagConstraints s_W_name_layout = new GridBagConstraints();
	    s_W_name_layout.gridx = 1;
	    s_W_name_layout.gridy = 1;
	    s_W_name_layout.anchor = GridBagConstraints.WEST;
	    s_platno.add(s_W_name, s_W_name_layout);
	
	    s_W_colour_button = new JButton("Barva");
	    final GridBagConstraints s_W_colour_button_layout = new GridBagConstraints();
	    s_W_colour_button_layout.gridx = 2;
	    s_W_colour_button_layout.gridy = 1;
	    s_W_colour_button_layout.anchor = GridBagConstraints.CENTER;
	    s_platno.add(s_W_colour_button, s_W_colour_button_layout);
	    s_W_colour_button.addActionListener(this);
	    
	    s_W_label = new JLabel("     ");
	    s_W_label.setOpaque(true);
	    s_W_label.setBackground(polje.barvaW);
	    final GridBagConstraints s_W_label_layout = new GridBagConstraints();
	    s_W_label_layout.gridx = 3;
	    s_W_label_layout.gridy = 1;
	    s_W_label_layout.anchor = GridBagConstraints.WEST;
	    s_platno.add(s_W_label, s_W_label_layout);
	
	    // Nastavitve barv igre
	    s_plosca_colour_button = new JButton("Plosca");
	    final GridBagConstraints s_plosca_colour_button_layout = new GridBagConstraints();
	    s_plosca_colour_button_layout.gridx = 0;
	    s_plosca_colour_button_layout.gridy = 2;
	    s_plosca_colour_button_layout.anchor = GridBagConstraints.CENTER;
	    s_platno.add(s_plosca_colour_button, s_plosca_colour_button_layout);
	    s_plosca_colour_button.addActionListener(this);
	    
	    s_plosca_label = new JLabel("     ");
	    s_plosca_label.setOpaque(true);
	    s_plosca_label.setBackground(polje.plosca);
	    final GridBagConstraints s_plosca_label_layout = new GridBagConstraints();
	    s_plosca_label_layout.gridx = 1;
	    s_plosca_label_layout.gridy = 2;
	    s_plosca_label_layout.anchor = GridBagConstraints.WEST;
	    s_platno.add(s_plosca_label, s_plosca_label_layout);
	    
	    
	    s_ozadje_colour_button = new JButton("Ozadje");
	    final GridBagConstraints s_ozadje_colour_button_layout = new GridBagConstraints();
	    s_ozadje_colour_button_layout.gridx = 0;
	    s_ozadje_colour_button_layout.gridy = 3;
	    s_ozadje_colour_button_layout.anchor = GridBagConstraints.CENTER;
	    s_platno.add(s_ozadje_colour_button, s_ozadje_colour_button_layout);
	    s_ozadje_colour_button.addActionListener(this);
	    
	    s_ozadje_label = new JLabel("     ");
	    s_ozadje_label.setOpaque(true);
	    s_ozadje_label.setBackground(polje.ozadje);
	    final GridBagConstraints s_ozadje_label_layout = new GridBagConstraints();
	    s_ozadje_label_layout.gridx = 1;
	    s_ozadje_label_layout.gridy = 3;
	    s_ozadje_label_layout.anchor = GridBagConstraints.WEST;
	    s_platno.add(s_ozadje_label, s_ozadje_label_layout);
	    
	    s_poudarek_colour_button = new JButton("Poudarek");
	    final GridBagConstraints s_poudarek_colour_button_layout = new GridBagConstraints();
	    s_poudarek_colour_button_layout.gridx = 0;
	    s_poudarek_colour_button_layout.gridy = 4;
	    s_poudarek_colour_button_layout.anchor = GridBagConstraints.CENTER;
	    s_platno.add(s_poudarek_colour_button, s_poudarek_colour_button_layout);
	    s_poudarek_colour_button.addActionListener(this);
	    
	    s_poudarek_label = new JLabel("     ");
	    s_poudarek_label.setOpaque(true);
	    s_poudarek_label.setBackground(polje.zadnjaPoteza);
	    final GridBagConstraints s_poudarek_label_layout = new GridBagConstraints();
	    s_poudarek_label_layout.gridx = 1;
	    s_poudarek_label_layout.gridy = 4;
	    s_poudarek_label_layout.anchor = GridBagConstraints.WEST;
	    s_platno.add(s_poudarek_label, s_poudarek_label_layout);
	    
	    s_zmagovalna_colour_button = new JButton("Zmagovalna");
	    final GridBagConstraints s_zmagovalna_button_layout = new GridBagConstraints();
	    s_zmagovalna_button_layout.gridx = 0;
	    s_zmagovalna_button_layout.gridy = 5;
	    s_zmagovalna_button_layout.anchor = GridBagConstraints.CENTER;
	    s_platno.add(s_zmagovalna_colour_button, s_zmagovalna_button_layout);
	    s_zmagovalna_colour_button.addActionListener(this);
	    
	    s_zmagovalna_label = new JLabel("     ");
	    s_zmagovalna_label.setOpaque(true);
	    s_zmagovalna_label.setBackground(polje.zmagovalnaVrsta);
	    final GridBagConstraints s_zmagovalna_label_layout = new GridBagConstraints();
	    s_zmagovalna_label_layout.gridx = 1;
	    s_zmagovalna_label_layout.gridy = 5;
	    s_zmagovalna_label_layout.anchor = GridBagConstraints.WEST;
	    s_platno.add(s_zmagovalna_label, s_zmagovalna_label_layout);
	
	    // Izbira algoritma za AI igralca
	    final String[] ai_list = { "Naiven", "Minimax", "AlfaBeta" };
	    
	    // 1. AI igralec
	    final JLabel s_ai1_algo_label = new JLabel("AI1:");
	    final GridBagConstraints s_ai1_algo_label_layout = new GridBagConstraints();
	    s_ai1_algo_label_layout.gridx = 0;
	    s_ai1_algo_label_layout.gridy = 6;
	    s_ai1_algo_label_layout.anchor = GridBagConstraints.CENTER;
	    s_platno.add(s_ai1_algo_label, s_ai1_algo_label_layout);
	
	    s_ai1_algo = new JComboBox<String>(ai_list);
	    final GridBagConstraints s_ai1_algo_layout = new GridBagConstraints();
	    s_ai1_algo_layout.gridx = 1;
	    s_ai1_algo_layout.gridy = 6;
	    s_ai1_algo_layout.anchor = GridBagConstraints.CENTER;
	    s_platno.add(s_ai1_algo, s_ai1_algo_layout);
	
	    // 2. AI igralec
	    final JLabel s_ai2_algo_label = new JLabel("AI2:");
	    final GridBagConstraints s_ai2_algo_label_layout = new GridBagConstraints();
	    s_ai2_algo_label_layout.gridx = 0;
	    s_ai2_algo_label_layout.gridy = 7;
	    s_ai2_algo_label_layout.anchor = GridBagConstraints.CENTER;
	    s_platno.add(s_ai2_algo_label, s_ai2_algo_label_layout);
	
	    s_ai2_algo = new JComboBox<String>(ai_list);
	    final GridBagConstraints s_ai2_algo_layout = new GridBagConstraints();
	    s_ai2_algo_layout.gridx = 1;
	    s_ai2_algo_layout.gridy = 7;
	    s_ai2_algo_layout.anchor = GridBagConstraints.CENTER;
	    s_platno.add(s_ai2_algo, s_ai2_algo_layout);
	
	    // globini algoritmov 1. in 2. igralca
	    s_B_depth = new JSpinner(new SpinnerNumberModel(Vodja.globina_B, 1, 5, 1));
	    final GridBagConstraints s_B_depth_layout = new GridBagConstraints();
	    s_B_depth_layout.gridx = 2;
	    s_B_depth_layout.gridy = 6;
	    s_B_depth_layout.anchor = GridBagConstraints.CENTER;
	    s_platno.add(s_B_depth, s_B_depth_layout);
	
	    s_W_depth = new JSpinner(new SpinnerNumberModel(Vodja.globina_W, 1, 5, 1));
	    final GridBagConstraints s_W_depth_layout = new GridBagConstraints();
	    s_W_depth_layout.gridx = 2;
	    s_W_depth_layout.gridy = 7;
	    s_W_depth_layout.anchor = GridBagConstraints.CENTER;
	    s_platno.add(s_W_depth, s_W_depth_layout);
	    
	    
	    // časovni zamik za potezo racunalnika
	    s_omejitev_AI = new JCheckBox();
	    final GridBagConstraints omejitev_layout = new GridBagConstraints();
	    omejitev_layout.gridx = 0;
	    omejitev_layout.gridy = 8;
	    omejitev_layout.anchor = GridBagConstraints.WEST;
	    s_platno.add(s_omejitev_AI, omejitev_layout);
	
	    final JLabel zamik_AI = new JLabel("Zamik AI:");
	    final GridBagConstraints zamik_layout = new GridBagConstraints();
	    zamik_layout.gridx = 0;
	    zamik_layout.gridy = 8;
	    zamik_layout.anchor = GridBagConstraints.CENTER;
	    s_platno.add(zamik_AI, zamik_layout);
	    
	    
	    s_zamik_time = new JSpinner(new SpinnerNumberModel(Vodja.zamik_ms, 1000, 10000, 1000));
	    final GridBagConstraints s_zamik_time_layout = new GridBagConstraints();
	    s_zamik_time_layout.gridx = 1;
	    s_zamik_time_layout.gridy = 8;
	    s_zamik_time_layout.anchor = GridBagConstraints.CENTER;
	    s_platno.add(s_zamik_time, s_zamik_time_layout);
	
	    // Gumb za shranjevanje
	    s_shrani = new JButton("Shrani");
	    final GridBagConstraints s_shrani_layout = new GridBagConstraints();
	    s_shrani_layout.gridx = 0;
	    s_shrani_layout.gridy = 50;
	    s_shrani_layout.anchor = GridBagConstraints.PAGE_END;
	    s_platno.add(s_shrani, s_shrani_layout);
	    s_shrani.addActionListener(this);
	
	    // privzeta velikost pop-up menija
	    s_platno.setSize(500, 400);
	}   
	
	
	// funkcija, ki doda meni zgornji orodni vrstici
	public JMenu dodajMenu(JMenuBar menubar, String naslov) {
        JMenu menu = new JMenu(naslov);
        menubar.add(menu);
        return menu;
    }

	
	// funkcija, ki doda podmeni meniju
    public JMenuItem dodajMenuItem(JMenu menu, String naslov) {
        JMenuItem menuitem = new JMenuItem(naslov);
        menu.add(menuitem);
        menuitem.addActionListener(this);
        return menuitem;
    }
    
    
    // tukaj so dogodki, ki se zgodijo če uporabnik pritisne nek gumb oz. odpre meni, ...
    @Override
	public void actionPerformed(ActionEvent e) {
    	
    	// dogodki, povezani z izbiro igre
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
					
		// velikost plošče	
		} else if (e.getSource() == velikostPlosce) {
			String velikostPlosce = JOptionPane.showInputDialog(this, "Velikost plosce:");
            if (velikostPlosce != null && velikostPlosce.matches("\\d+") && Integer.parseInt(velikostPlosce) > 4 && Integer.parseInt(velikostPlosce) < 24) {
            	velikost = Integer.parseInt(velikostPlosce);
            	polje.N = Integer.parseInt(velikostPlosce);
            	if (Vodja.igra != null) {
            		Vodja.igramoNovoIgro();
            	}
            	else {Vodja.igra = null; status.setText("Izberite igro!");}
                polje.repaint();
            }  
         
        // razveljavitev poteze	
		} else if (e.getSource() == razveljavi) {
			Vodja.undo();
			polje.repaint();
			
		// pop-up meni nastavitev
		} else if (e.getSource() == settings) {
			
			// v pop-up meni nastavitve damo privzete vrednosti (oz. vrednosti, ki v danem trenutku veljajo)
            s_B_name.setText(Vodja.igralecName.get(Igralec.B));
            s_W_name.setText(Vodja.igralecName.get(Igralec.W));

            s_ai1_algo.setSelectedItem(Vodja.aiAlgorithm.get(Igralec.B));
            s_ai2_algo.setSelectedItem(Vodja.aiAlgorithm.get(Igralec.W));

            s_B_depth.setValue(Vodja.globina_B);
            s_W_depth.setValue(Vodja.globina_W);
            
            s_platno.setVisible(true);
            
        // pritisnemo gumb shrani, ki shrani vse nastavitve, ki smo jih spremenili
	    } else if (e.getSource() == s_shrani) {
	        if (t_B_colour != null) {
	            polje.barvaB = t_B_colour;
	        }
	        if (t_W_colour != null) {
	        	polje.barvaW = t_W_colour;
	        }
	        if (t_plosca_colour != null) {
	            polje.plosca = t_plosca_colour;
	        }
	        if (t_ozadje_colour != null) {
	           polje.spremeniOzadje(t_ozadje_colour);
	        }
	        if (t_poudarek_colour != null) {
	            polje.zadnjaPoteza = t_poudarek_colour;
	        }
	        if (t_zmagovalna_colour != null) {
	        	polje.zmagovalnaVrsta = t_zmagovalna_colour;
	        }
	
	        if (!s_B_name.getText().isEmpty()) {
	            Vodja.igralecName.put(Igralec.B, s_B_name.getText());
	        }
	        if (!s_W_name.getText().isEmpty()) {
	            Vodja.igralecName.put(Igralec.W, s_W_name.getText());
	        }
	
	        // Preveri, če se je algoritem za AI spremenil in nato znova zažene igro
	        if (!Vodja.aiAlgorithm.get(Igralec.B).equals(s_ai1_algo.getSelectedItem().toString())
	                || !Vodja.aiAlgorithm.get(Igralec.W).equals(s_ai2_algo.getSelectedItem().toString())) {
	            Vodja.aiAlgorithm.put(Igralec.B, s_ai1_algo.getSelectedItem().toString());
	            Vodja.aiAlgorithm.put(Igralec.W, s_ai2_algo.getSelectedItem().toString());
	            if (Vodja.igra != null) {
	                Vodja.igramoNovoIgro();
	            }
	        }
	
	        // nastavi AI parametre, ki jih lahko spreminjamo med igro (globine algoritmov, razne barve, itd.)
	        Vodja.globina_B = (int) s_B_depth.getValue();
	        Vodja.globina_W = (int) s_W_depth.getValue();
	        Vodja.zamik_ms = (int) s_zamik_time.getValue();
	        	
	        if (s_omejitev_AI.isSelected()) {
	        	Vodja.zamik = true;
	        }
	        
	        else {
	        	Vodja.zamik = false;
	        }
	        
	        s_platno.dispose();
	        osveziGUI(); // sele ko pritisnemo gumb shrani, se ponastavi izgled plosce
	     
	    // zacasne spremenljivke barv nastavimo na izbrane barve
	    } else if (e.getSource() == s_B_colour_button) {
	        Color c = JColorChooser.showDialog(s_platno, "Izberi barvo", polje.barvaB);
	        if (c != null) {
	            t_B_colour = c;
	            s_B_label.setBackground(c);
	        }
	    } else if (e.getSource() == s_W_colour_button) {
	        Color c = JColorChooser.showDialog(s_platno, "Izberi barvo", polje.barvaW);
	        if (c != null) {
	            t_W_colour = c;
	            s_W_label.setBackground(c);
	        }
	    } else if (e.getSource() == s_plosca_colour_button) {
	        Color c = JColorChooser.showDialog(s_platno, "Izberi barvo", polje.plosca);
	        if (c != null) {
	            t_plosca_colour = c;
	            s_plosca_label.setBackground(c);
	        }
	    } else if (e.getSource() == s_ozadje_colour_button) {
	        Color c = JColorChooser.showDialog(s_platno, "Izberi barvo", polje.ozadje);
	        if (c != null) {
	            t_ozadje_colour = c;
	            s_ozadje_label.setBackground(c);
	        }
	    } else if (e.getSource() == s_poudarek_colour_button) {
	        Color c = JColorChooser.showDialog(s_platno, "Izberi barvo", polje.zadnjaPoteza);
	        if (c != null) {
	            t_poudarek_colour = c;
	            s_poudarek_label.setBackground(c);
	        }
	    } else if (e.getSource() == s_zmagovalna_colour_button) {
	        Color c = JColorChooser.showDialog(s_platno, "Izberi barvo", polje.zmagovalnaVrsta);
	        if (c != null) {
	            t_zmagovalna_colour = c;
	            s_zmagovalna_label.setBackground(c);
	        }
	    }
				
	}
    
    // funkcija, ki skrbi za repaint platna in spreminjanje statusne vrstice
	public void osveziGUI() {
		if (Vodja.igra == null) {
			status.setText("Igra ni v teku.");
		}
		else {
			switch(Vodja.igra.stanje()) {
			case NEODLOCENO: status.setText("Neodločeno!"); break;
			case V_TEKU: 
				status.setText("Na potezi je " + Vodja.igralecName.get(Vodja.igra.naPotezi) + 
						" - " + Vodja.vrstaIgralca.get(Vodja.igra.naPotezi)); 
				break;
			case ZMAGA_B: 
				status.setText("Zmagal je " + Vodja.igralecName.get(Igralec.B) + " - " + 
						Vodja.vrstaIgralca.get(Vodja.igra.naPotezi.nasprotnik()));
				break;
			case ZMAGA_W: 
				status.setText("Zmagal je " + Vodja.igralecName.get(Igralec.W) + " - " + 
						Vodja.vrstaIgralca.get(Vodja.igra.naPotezi.nasprotnik()));
				break;
			}
		}
		polje.repaint();
	}

}
