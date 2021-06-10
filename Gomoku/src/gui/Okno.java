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
//import logika.Igra;
import logika.Igralec;
import vodja.VrstaIgralca;

@SuppressWarnings("serial")
public class Okno extends JFrame implements ActionListener{
	
	public Platno polje;
	
	private JLabel status;
	
	private JMenuItem igraClovekRacunalnik, igraRacunalnikClovek, igraClovekClovek, igraRacunalnikRacunalnik;
	private JMenuItem velikostPlosce;
	private JButton razveljavi;
	
	private final JMenuItem settings;
    private final JDialog s_pane;
    private final JButton s_save;
    
    private final JButton s_p1_colour_button;
    private final JLabel BLabel;
    
    private final JButton s_p2_colour_button;
    private final JLabel WLabel;
    
    private final JTextField s_p1_name;
    private final JTextField s_p2_name;

    private final JButton s_fg_colour_button;
    private final JLabel ploscaLabel;
    
    private final JButton s_bg_colour_button;
    private final JLabel ozadjeLabel;
    
    private final JButton s_accent_colour_button;
    private final JLabel poudarekLabel;

    private final JButton zmagovalna_colour_button;
    private final JLabel zmagovalnaLabel;

    private final JComboBox<String> s_ai1_algo;
    private final JComboBox<String> s_ai2_algo;

    final JSpinner s_minimax_depth;
    final JSpinner s_negamax_depth;
    final JSpinner s_mcts_time;
    
    final JCheckBox omejitev_AI;
    
    private Color t_p1_colour;
    private Color t_p2_colour;
    private Color t_fg_colour;
    private Color t_bg_colour;
    private Color t_accent_colour;
    private Color zmagovalna;

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
        
        settings = dodajMenuItem(menunastavitve,"Nastavitve");
        
        razveljavi = new JButton("Razveljavi");
        final GridBagConstraints undo_layout = new GridBagConstraints();
        undo_layout.gridx = 0;
        undo_layout.gridy = 1;
        undo_layout.anchor = GridBagConstraints.EAST;
        getContentPane().add(razveljavi, undo_layout);
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
			
	
    
    	s_pane = new JDialog(this, "Nastavitve", /* modal */ true);
    	s_pane.setLayout(new GridBagLayout());

    	// Nastavitve imena in barve 1. igralca
    	final JLabel s_p1_name_label = new JLabel("P1:");
    	final GridBagConstraints s_p1_name_label_layout = new GridBagConstraints();
    	s_p1_name_label_layout.gridx = 0;
    	s_p1_name_label_layout.gridy = 0;
    	s_p1_name_label_layout.anchor = GridBagConstraints.CENTER;
    	s_pane.add(s_p1_name_label, s_p1_name_label_layout);

	    s_p1_name = new JTextField();
	    s_p1_name.setColumns(15);
	    final GridBagConstraints s_p1_name_layout = new GridBagConstraints();
	    s_p1_name_layout.gridx = 1;
	    s_p1_name_layout.gridy = 0;
	    s_p1_name_layout.anchor = GridBagConstraints.WEST;
	    s_pane.add(s_p1_name, s_p1_name_layout);

	    s_p1_colour_button = new JButton("Barva");
	    final GridBagConstraints s_p1_colour_button_layout = new GridBagConstraints();
	    s_p1_colour_button_layout.gridx = 2;
	    s_p1_colour_button_layout.gridy = 0;
	    s_p1_colour_button_layout.anchor = GridBagConstraints.CENTER;
	    s_pane.add(s_p1_colour_button, s_p1_colour_button_layout);
	    s_p1_colour_button.addActionListener(this);
	    
	    BLabel = new JLabel("     ");
	    BLabel.setOpaque(true);
	    BLabel.setBackground(polje.barvaB);
	    final GridBagConstraints BLabel_layout = new GridBagConstraints();
	    BLabel_layout.gridx = 3;
	    BLabel_layout.gridy = 0;
	    BLabel_layout.anchor = GridBagConstraints.WEST;
	    s_pane.add(BLabel, BLabel_layout);
	
	    // Nastavitve imena in barve 2. igralca
	    final JLabel s_p2_name_label = new JLabel("P2:");
	    final GridBagConstraints s_p2_name_label_layout = new GridBagConstraints();
	    s_p2_name_label_layout.gridx = 0;
	    s_p2_name_label_layout.gridy = 1;
	    s_p2_name_label_layout.anchor = GridBagConstraints.CENTER;
	    s_pane.add(s_p2_name_label, s_p2_name_label_layout);
	
	    s_p2_name = new JTextField();
	    s_p2_name.setColumns(15);
	    final GridBagConstraints s_p2_name_layout = new GridBagConstraints();
	    s_p2_name_layout.gridx = 1;
	    s_p2_name_layout.gridy = 1;
	    s_p2_name_layout.anchor = GridBagConstraints.WEST;
	    s_pane.add(s_p2_name, s_p2_name_layout);
	
	    s_p2_colour_button = new JButton("Barva");
	    final GridBagConstraints s_p2_colour_button_layout = new GridBagConstraints();
	    s_p2_colour_button_layout.gridx = 2;
	    s_p2_colour_button_layout.gridy = 1;
	    s_p2_colour_button_layout.anchor = GridBagConstraints.CENTER;
	    s_pane.add(s_p2_colour_button, s_p2_colour_button_layout);
	    s_p2_colour_button.addActionListener(this);
	    
	    WLabel = new JLabel("     ");
	    WLabel.setOpaque(true);
	    WLabel.setBackground(polje.barvaW);
	    final GridBagConstraints WLabel_layout = new GridBagConstraints();
	    WLabel_layout.gridx = 3;
	    WLabel_layout.gridy = 1;
	    WLabel_layout.anchor = GridBagConstraints.WEST;
	    s_pane.add(WLabel, WLabel_layout);
	
	    // Nastavitve barv igre
	    s_fg_colour_button = new JButton("Plosca");
	    final GridBagConstraints s_fg_colour_button_layout = new GridBagConstraints();
	    s_fg_colour_button_layout.gridx = 0;
	    s_fg_colour_button_layout.gridy = 2;
	    s_fg_colour_button_layout.anchor = GridBagConstraints.CENTER;
	    s_pane.add(s_fg_colour_button, s_fg_colour_button_layout);
	    s_fg_colour_button.addActionListener(this);
	    
	    ploscaLabel = new JLabel("     ");
	    ploscaLabel.setOpaque(true);
	    ploscaLabel.setBackground(polje.plosca);
	    final GridBagConstraints ploscaLabel_layout = new GridBagConstraints();
	    ploscaLabel_layout.gridx = 1;
	    ploscaLabel_layout.gridy = 2;
	    ploscaLabel_layout.anchor = GridBagConstraints.WEST;
	    s_pane.add(ploscaLabel, ploscaLabel_layout);
	    
	    
	    s_bg_colour_button = new JButton("Ozadje");
	    final GridBagConstraints s_bg_colour_button_layout = new GridBagConstraints();
	    s_bg_colour_button_layout.gridx = 0;
	    s_bg_colour_button_layout.gridy = 3;
	    s_bg_colour_button_layout.anchor = GridBagConstraints.CENTER;
	    s_pane.add(s_bg_colour_button, s_bg_colour_button_layout);
	    s_bg_colour_button.addActionListener(this);
	    
	    ozadjeLabel = new JLabel("     ");
	    ozadjeLabel.setOpaque(true);
	    ozadjeLabel.setBackground(polje.ozadje);
	    final GridBagConstraints ozadjeLabel_layout = new GridBagConstraints();
	    ozadjeLabel_layout.gridx = 1;
	    ozadjeLabel_layout.gridy = 3;
	    ozadjeLabel_layout.anchor = GridBagConstraints.WEST;
	    s_pane.add(ozadjeLabel, ozadjeLabel_layout);
	    
	    s_accent_colour_button = new JButton("Poudarek");
	    final GridBagConstraints s_accent_colour_button_layout = new GridBagConstraints();
	    s_accent_colour_button_layout.gridx = 0;
	    s_accent_colour_button_layout.gridy = 4;
	    s_accent_colour_button_layout.anchor = GridBagConstraints.CENTER;
	    s_pane.add(s_accent_colour_button, s_accent_colour_button_layout);
	    s_accent_colour_button.addActionListener(this);
	    
	    poudarekLabel = new JLabel("     ");
	    poudarekLabel.setOpaque(true);
	    poudarekLabel.setBackground(polje.zadnjaPoteza);
	    final GridBagConstraints poudarekLabel_layout = new GridBagConstraints();
	    poudarekLabel_layout.gridx = 1;
	    poudarekLabel_layout.gridy = 4;
	    poudarekLabel_layout.anchor = GridBagConstraints.WEST;
	    s_pane.add(poudarekLabel, poudarekLabel_layout);
	    
	    zmagovalna_colour_button = new JButton("Zmagovalna");
	    final GridBagConstraints zmagovalna_button_layout = new GridBagConstraints();
	    zmagovalna_button_layout.gridx = 0;
	    zmagovalna_button_layout.gridy = 5;
	    zmagovalna_button_layout.anchor = GridBagConstraints.CENTER;
	    s_pane.add(zmagovalna_colour_button, zmagovalna_button_layout);
	    zmagovalna_colour_button.addActionListener(this);
	    
	    zmagovalnaLabel = new JLabel("     ");
	    zmagovalnaLabel.setOpaque(true);
	    zmagovalnaLabel.setBackground(polje.zmagovalnaVrsta);
	    final GridBagConstraints zmagovalnaLabel_layout = new GridBagConstraints();
	    zmagovalnaLabel_layout.gridx = 1;
	    zmagovalnaLabel_layout.gridy = 5;
	    zmagovalnaLabel_layout.anchor = GridBagConstraints.WEST;
	    s_pane.add(zmagovalnaLabel, zmagovalnaLabel_layout);
	
	    // Izbira algoritma za AI igralca
	    final String[] ai_list = { "Naiven", "Minimax", "AlfaBeta" };
	    // 1. AI igralec
	    final JLabel s_ai1_algo_label = new JLabel("AI1:");
	    final GridBagConstraints s_ai1_algo_label_layout = new GridBagConstraints();
	    s_ai1_algo_label_layout.gridx = 0;
	    s_ai1_algo_label_layout.gridy = 6;
	    s_ai1_algo_label_layout.anchor = GridBagConstraints.CENTER;
	    s_pane.add(s_ai1_algo_label, s_ai1_algo_label_layout);
	
	    s_ai1_algo = new JComboBox<String>(ai_list);
	    final GridBagConstraints s_ai1_algo_layout = new GridBagConstraints();
	    s_ai1_algo_layout.gridx = 1;
	    s_ai1_algo_layout.gridy = 6;
	    s_ai1_algo_layout.anchor = GridBagConstraints.CENTER;
	    s_pane.add(s_ai1_algo, s_ai1_algo_layout);
	
	    // 2. AI igralec
	    final JLabel s_ai2_algo_label = new JLabel("AI2:");
	    final GridBagConstraints s_ai2_algo_label_layout = new GridBagConstraints();
	    s_ai2_algo_label_layout.gridx = 0;
	    s_ai2_algo_label_layout.gridy = 7;
	    s_ai2_algo_label_layout.anchor = GridBagConstraints.CENTER;
	    s_pane.add(s_ai2_algo_label, s_ai2_algo_label_layout);
	
	    s_ai2_algo = new JComboBox<String>(ai_list);
	    final GridBagConstraints s_ai2_algo_layout = new GridBagConstraints();
	    s_ai2_algo_layout.gridx = 1;
	    s_ai2_algo_layout.gridy = 7;
	    s_ai2_algo_layout.anchor = GridBagConstraints.CENTER;
	    s_pane.add(s_ai2_algo, s_ai2_algo_layout);
	
	    s_minimax_depth = new JSpinner(new SpinnerNumberModel(Vodja.globina_B, 1, 5, 1));
	    final GridBagConstraints s_minimax_depth_layout = new GridBagConstraints();
	    s_minimax_depth_layout.gridx = 2;
	    s_minimax_depth_layout.gridy = 6;
	    s_minimax_depth_layout.anchor = GridBagConstraints.CENTER;
	    s_pane.add(s_minimax_depth, s_minimax_depth_layout);
	
	    s_negamax_depth = new JSpinner(new SpinnerNumberModel(Vodja.globina_W, 1, 5, 1));
	    final GridBagConstraints s_negamax_depth_layout = new GridBagConstraints();
	    s_negamax_depth_layout.gridx = 2;
	    s_negamax_depth_layout.gridy = 7;
	    s_negamax_depth_layout.anchor = GridBagConstraints.CENTER;
	    s_pane.add(s_negamax_depth, s_negamax_depth_layout);
	    
	    omejitev_AI = new JCheckBox();
	    final GridBagConstraints omejitev_layout = new GridBagConstraints();
	    omejitev_layout.gridx = 0;
	    omejitev_layout.gridy = 8;
	    omejitev_layout.anchor = GridBagConstraints.WEST;
	    s_pane.add(omejitev_AI, omejitev_layout);
	
	    final JLabel zamik_AI = new JLabel("Zamik AI:");
	    final GridBagConstraints zamik_layout = new GridBagConstraints();
	    zamik_layout.gridx = 0;
	    zamik_layout.gridy = 8;
	    zamik_layout.anchor = GridBagConstraints.CENTER;
	    s_pane.add(zamik_AI, zamik_layout);
	    
	    
	    s_mcts_time = new JSpinner(new SpinnerNumberModel(Vodja.mcts_time_ms, 1000, 10000, 1000));
	    final GridBagConstraints s_mcts_time_layout = new GridBagConstraints();
	    s_mcts_time_layout.gridx = 1;
	    s_mcts_time_layout.gridy = 8;
	    s_mcts_time_layout.anchor = GridBagConstraints.CENTER;
	    s_pane.add(s_mcts_time, s_mcts_time_layout);
	
	    // Gumb za shranjevanje
	    s_save = new JButton("Shrani");
	    final GridBagConstraints s_save_layout = new GridBagConstraints();
	    s_save_layout.gridx = 0;
	    s_save_layout.gridy = 50;
	    s_save_layout.anchor = GridBagConstraints.PAGE_END;
	    s_pane.add(s_save, s_save_layout);
	    s_save.addActionListener(this);
	
	    s_pane.setSize(500, 400);
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
            if (velikostPlosce != null && velikostPlosce.matches("\\d+") && Integer.parseInt(velikostPlosce) > 4 && Integer.parseInt(velikostPlosce) < 24) {
            	velikost = Integer.parseInt(velikostPlosce);
            	polje.N = Integer.parseInt(velikostPlosce);
            	if (Vodja.igra != null) {
            		Vodja.igramoNovoIgro();
            	}
            	else {Vodja.igra = null; status.setText("Izberite igro!");}
                polje.repaint();
            }  
            
		} else if (e.getSource() == razveljavi) {
			Vodja.undo();
			polje.repaint();
		} else if (e.getSource() == settings) {
			

            s_p1_name.setText(Vodja.igralecName.get(Igralec.B));
            s_p2_name.setText(Vodja.igralecName.get(Igralec.W));

            s_ai1_algo.setSelectedItem(Vodja.aiAlgorithm.get(Igralec.B));
            s_ai2_algo.setSelectedItem(Vodja.aiAlgorithm.get(Igralec.W));

            s_minimax_depth.setValue(Vodja.globina_B);
            s_negamax_depth.setValue(Vodja.globina_W);
            
            s_pane.setVisible(true);
            
	    } else if (e.getSource() == s_save) {
	        if (t_p1_colour != null) {
	            polje.barvaB = t_p1_colour;
	        }
	        if (t_p2_colour != null) {
	        	polje.barvaW = t_p2_colour;
	        }
	        if (t_fg_colour != null) {
	            polje.plosca = t_fg_colour;
	        }
	        if (t_bg_colour != null) {
	           polje.spremeniOzadje(t_bg_colour);
	        }
	        if (t_accent_colour != null) {
	            polje.zadnjaPoteza = t_accent_colour;
	        }
	        if (zmagovalna != null) {
	        	polje.zmagovalnaVrsta = zmagovalna;
	        }
	
	        if (!s_p1_name.getText().isEmpty()) {
	            Vodja.igralecName.put(Igralec.B, s_p1_name.getText());
	        }
	        if (!s_p2_name.getText().isEmpty()) {
	            Vodja.igralecName.put(Igralec.W, s_p2_name.getText());
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
	
	        // Nastavi AI parametre, ki jih lahko spreminjamo med igro
	        Vodja.globina_B = (int) s_minimax_depth.getValue();
	        Vodja.globina_W = (int) s_negamax_depth.getValue();
	        Vodja.mcts_time_ms = (int) s_mcts_time.getValue();
	        	
	        if (omejitev_AI.isSelected()) {
	        	Vodja.zamik = true;
	        }
	        
	        else {
	        	Vodja.zamik = false;
	        }
	        
	        s_pane.dispose();
	        osveziGUI();
	        
	    } else if (e.getSource() == s_p1_colour_button) {
	        Color c = JColorChooser.showDialog(s_pane, "Izberi barvo", polje.barvaB);
	        if (c != null) {
	            t_p1_colour = c;
	            BLabel.setBackground(c);
	        }
	    } else if (e.getSource() == s_p2_colour_button) {
	        Color c = JColorChooser.showDialog(s_pane, "Izberi barvo", polje.barvaW);
	        if (c != null) {
	            t_p2_colour = c;
	            WLabel.setBackground(c);
	        }
	    } else if (e.getSource() == s_fg_colour_button) {
	        Color c = JColorChooser.showDialog(s_pane, "Izberi barvo", polje.plosca);
	        if (c != null) {
	            t_fg_colour = c;
	            ploscaLabel.setBackground(c);
	        }
	    } else if (e.getSource() == s_bg_colour_button) {
	        Color c = JColorChooser.showDialog(s_pane, "Izberi barvo", polje.ozadje);
	        if (c != null) {
	            t_bg_colour = c;
	            ozadjeLabel.setBackground(c);
	        }
	    } else if (e.getSource() == s_accent_colour_button) {
	        Color c = JColorChooser.showDialog(s_pane, "Izberi barvo", polje.zadnjaPoteza);
	        if (c != null) {
	            t_accent_colour = c;
	            poudarekLabel.setBackground(c);
	        }
	    } else if (e.getSource() == zmagovalna_colour_button) {
	        Color c = JColorChooser.showDialog(s_pane, "Izberi barvo", polje.zmagovalnaVrsta);
	        if (c != null) {
	            zmagovalna = c;
	            zmagovalnaLabel.setBackground(c);
	        }
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
