package com.flg.DateOnFileName;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;


public class FenetreAddDateOnFileName extends JFrame {

	private JTabbedPane onglet = new JTabbedPane();
	// Config
	private static JCheckBox jcbSimulation = new JCheckBox("Simulation (just logs)");
	private JPanel panConfig = new JPanel();
	private JLabel jlbRepertoire = new JLabel("Repertoire");
	public JTextField jtfRepertoire = new JTextField();
	private JLabel jlbLog = new JLabel("Log");
	public JTextArea jtLog = new JTextArea();
	private JScrollPane scrollLog = new JScrollPane(jtLog);
	public JButton boutonStart =  new JButton("Start");

	public FenetreAddDateOnFileName()
	{
		this.setTitle("Ajout prefix dates MP3");
		this.setSize(500, 500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);

	    JPanel jpCBSimul = new JPanel();
	    jpCBSimul.setLayout(new BoxLayout(jpCBSimul, BoxLayout.LINE_AXIS));
	    jcbSimulation.addActionListener(new CheckSimulationActionListener());
	    jcbSimulation.setSelected(DateOnFileName.bSimulation); 
	    jpCBSimul.add(jcbSimulation);
	    		
	    // Répertoire
	    JPanel jpRepertoire = new JPanel();
	    jpRepertoire.setLayout(new BoxLayout(jpRepertoire, BoxLayout.LINE_AXIS));
	    jpRepertoire.add(Box.createRigidArea(new Dimension(30, 0)));
	    jpRepertoire.add(jlbRepertoire);
	    jtfRepertoire.setMaximumSize(new Dimension(Integer.MAX_VALUE, jtfRepertoire.getMinimumSize().height));
	    jpRepertoire.add(jtfRepertoire);
	    jtfRepertoire.setText(DateOnFileName.sRepertoire);

	    // Ajout du bouton start
	    JPanel jpButtonStart = new JPanel();
	    boutonStart.addActionListener(new BoutonListenerStart()); 
	    jpButtonStart.setLayout(new BoxLayout(jpButtonStart, BoxLayout.LINE_AXIS));
	    jpButtonStart.add(boutonStart);
	    
	    panConfig.setLayout(new BoxLayout(panConfig, BoxLayout.PAGE_AXIS));
	    panConfig.add(Box.createRigidArea(new Dimension(0, 20)));
	    panConfig.add(jpCBSimul);
	    panConfig.add(Box.createRigidArea(new Dimension(0, 5)));
	    panConfig.add(jpRepertoire);
	    panConfig.add(Box.createRigidArea(new Dimension(0, 5)));
	    panConfig.add(scrollLog);
	    panConfig.add(Box.createRigidArea(new Dimension(0, 5)));
	    panConfig.add(jpButtonStart);


	    onglet.add("Configuration", panConfig);
	    //On passe ensuite les onglets au content pane
		this.getContentPane().add(onglet, BorderLayout.CENTER);

	    this.setContentPane(onglet);
	    this.setVisible(true);
	}

	/*
	 * Checkbox Simulation
	 */
	class CheckSimulationActionListener implements ActionListener{
	    public void actionPerformed(ActionEvent e) {
	      System.out.println("source : " + ((JCheckBox)e.getSource()).getText() + " - état : " + ((JCheckBox)e.getSource()).isSelected());
	      DateOnFileName.bSimulation = jcbSimulation.isSelected();
	    }
	}
	
	/*
	 * Bouton Start
	 */
	class BoutonListenerStart implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
		    System.out.println("Start");
		    DateOnFileName.sRepertoire = jtfRepertoire.getText();
	    	try {
				DateOnFileName.traiteFichier.renameFilesByModificationDate(DateOnFileName.bSimulation, DateOnFileName.sRepertoire, jtLog);
			} catch (UnsupportedTagException | InvalidDataException | NotSupportedException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	    }
	  }
}
