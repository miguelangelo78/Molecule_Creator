package com.app.window;
import com.app.window.TextAreaOutputStream;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JFrame;

import com.app.atom.Atom;
import com.app.molecule.Molecule;

import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JMenuBar;

import java.awt.BorderLayout;

import javax.swing.JTextField;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.PrintStream;

import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;
import javax.swing.BoxLayout;
import javax.swing.JTextArea;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EtchedBorder;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Window {
	private JFrame frame;
	private JSpinner atom_set_amount = new JSpinner();
	private final JPanel atombench = new JPanel();
	private final JButton btnBuildAtom = new JButton("Build atom");
	private final JButton btnRemoveAtom = new JButton("Remove atom");
	private JTextField proton_amount;
	private List<JLabel> atom_list;
	private int i_atom_list=0;
	int wait_incdec=0;
	private JTextField electron_config1;
	private JTextField electron_config2;
	private JTextField electron_config3;
	private JTextField electron_config4;
	private JTextField electron_config5;
	private JTextField electron_config6;
	private JTextField electron_config7;
	private int[]electron_config;
	private int protons;
	private List<Atom> atoms=new ArrayList<Atom>();
	private List<Molecule>molecules=new ArrayList<Molecule>();
	private JButton btnMakeMolecule = new JButton("Make molecule");
	private JPanel panel_1;
	private JPanel panel_2;
	private JScrollPane scrollPane;
	private JTextArea textArea;
	private JButton btnAtomInfo;
	private JButton btnMoleculeInfo;
	private JButton btnClear;
	private JLabel lblNewLabel_1;
	public static void main(String[] args) {
		try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());} catch (ClassNotFoundException | InstantiationException| IllegalAccessException | UnsupportedLookAndFeelException e) {e.printStackTrace();}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Window window = new Window();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public Window() {
		initialize();
	}
	private void disable_buttons(boolean except_remove){
		electron_config1.setEnabled(false); electron_config1.setText("0");
		electron_config2.setEnabled(false); electron_config2.setText("0");
		electron_config3.setEnabled(false); electron_config3.setText("0");
		electron_config4.setEnabled(false); electron_config4.setText("0");
		electron_config5.setEnabled(false); electron_config5.setText("0");
		electron_config6.setEnabled(false); electron_config6.setText("0");
		electron_config7.setEnabled(false); electron_config7.setText("0");
		proton_amount.setEnabled(false); proton_amount.setText("");
		atom_set_amount.setEnabled(false); atom_set_amount.setValue(1);
		btnAtomInfo.setEnabled(false);
		btnBuildAtom.setEnabled(false);
		if(!except_remove)btnRemoveAtom.setEnabled(false);
	}
	private void enable_buttons(){
		electron_config1.setEnabled(true);
		electron_config2.setEnabled(true);
		electron_config3.setEnabled(true);
		electron_config4.setEnabled(true);
		electron_config5.setEnabled(true);
		electron_config6.setEnabled(true);
		electron_config7.setEnabled(true);
		atom_set_amount.setEnabled(true);
		proton_amount.setEnabled(true);
		btnBuildAtom.setEnabled(true);
		btnRemoveAtom.setEnabled(true);
		
		int []index;
		if((index=get_selection()).length==1)
			if(!atom_list.get(index[0]).getText().contains("?"))
				btnAtomInfo.setEnabled(true);
	}
	private void select_atom(int[] indexes,boolean multiple_selection){
		if(!multiple_selection)	for(int i=0;i<atom_list.size();i++)	atom_list.get(i).setForeground(Color.BLACK);
		for(int i=0;i<indexes.length;i++) atom_list.get(indexes[i]).setForeground(Color.RED);
		enable_buttons();
		
		atom_list.get(0).requestFocus();
		atom_list.get(0).addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent key){
				if(wait_incdec>=5) wait_incdec=0;
				if(wait_incdec%2==0){
					if(key.getKeyCode()==127) remove_atom();
					int index[];
					if((index=get_selection()).length==1){
						if(!atom_list.get(index[0]).getText().contains("?")){
						if(key.getKeyCode()==38){
							//INCREMENTA
							if(!atom_list.get(index[0]).getText().matches(".*\\d.*"))
								atom_list.get(index[0]).setText(atom_list.get(index[0]).getText()+"2");
							else{
								Matcher matcher = Pattern.compile("([0-9]+)").matcher(atom_list.get(index[0]).getText());matcher.find();
								int n=Integer.valueOf(matcher.group(0))+1;
								if(n<=100) atom_list.get(index[0]).setText(atom_list.get(index[0]).getText().replaceAll("[0-9]+",n+""));
							}
						}
						if(key.getKeyCode()==40){
							//DECREMENTA
							if(!atom_list.get(index[0]).getText().matches(".*\\d.*")){}
							else{
								Matcher matcher = Pattern.compile("([0-9]+)").matcher(atom_list.get(index[0]).getText());matcher.find();
								int n=Integer.valueOf(matcher.group(0))-1;
								if(n>1) atom_list.get(index[0]).setText(atom_list.get(index[0]).getText().replaceAll("[0-9]+",n+""));
								else atom_list.get(index[0]).setText(atom_list.get(index[0]).getText().replaceAll("[0-9]+",""));
							}
						}
					}
					}
			}	
			wait_incdec++;
			}
		});
	}
	private void deselect_atom(int[]indexes){
		for(int i=0;i<indexes.length;i++)
		atom_list.get(indexes[i]).setForeground(Color.BLACK);
		disable_buttons(false);
		wait_incdec=1;
	}
	private int[] get_selection(){
		List<String>selected=new ArrayList<String>();
		for(int i=0;i<atom_list.size();i++) if(atom_list.get(i).getForeground()==Color.RED)	selected.add(atom_list.get(i).getName());
		int selected_int[]=new int[selected.size()];
		for(int i=0;i<selected.size();i++) selected_int[i]=Integer.valueOf(selected.get(i));
		return selected_int;
	}
	private void remove_atom(){
		while(get_selection().length>0)
			for(int i=0;i<atom_list.size();i++){
				if(atom_list.get(i).getForeground()==Color.RED){
					atombench.remove(atom_list.get(i));
					atom_list.remove(i);
					if(atoms.size()>0)	atoms.remove(i);
					i_atom_list--;
					for(int j=0;j<atom_list.size();j++)	atom_list.get(j).setName(j+"");
					atombench.updateUI();
				}
			}
		if(i_atom_list==0) disable_buttons(false);
		if(i_atom_list<2) btnMakeMolecule.setEnabled(false);
		disable_buttons(false);
	}
	private void initialize() {
		atom_list=new ArrayList<JLabel>();
		frame = new JFrame();
		frame.setTitle("Molecule Creator");
		frame.setBounds(100, 100, 900, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.SOUTH);
		
		JLabel lblProtons = new JLabel("Protons:");
		panel.add(lblProtons);
		
		proton_amount = new JTextField();
		proton_amount.setEnabled(false);
		panel.add(proton_amount);
		proton_amount.setColumns(3);
		
		JLabel lblNewLabel = new JLabel("Electron Configuration:");
		panel.add(lblNewLabel);
		
		electron_config1 = new JTextField();
		electron_config1.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				if(electron_config1.getText().matches("0"))	electron_config1.setText("");
			}
			public void focusLost(FocusEvent e) {
				if(	electron_config1.getText().matches("")) electron_config1.setText("0");
			}
		});
		electron_config1.setText("0");
		electron_config1.setEnabled(false);
		panel.add(electron_config1);
		electron_config1.setColumns(2);
		
		electron_config2 = new JTextField();
		electron_config2.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				if(electron_config2.getText().matches("0"))	electron_config2.setText("");
			}
			public void focusLost(FocusEvent e) {
				if(	electron_config2.getText().matches("")) electron_config2.setText("0");
			}
		});
		electron_config2.setText("0");
		electron_config2.setEnabled(false);
		panel.add(electron_config2);
		electron_config2.setColumns(2);
		
		electron_config3 = new JTextField();
		electron_config3.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				if(electron_config3.getText().matches("0"))	electron_config3.setText("");
			}
			public void focusLost(FocusEvent e) {
				if(	electron_config3.getText().matches("")) electron_config3.setText("0");
			}
		});
		electron_config3.setText("0");
		electron_config3.setEnabled(false);
		panel.add(electron_config3);
		electron_config3.setColumns(2);
		
		electron_config4 = new JTextField();
		electron_config4.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				if(electron_config4.getText().matches("0"))	electron_config4.setText("");
			}
			public void focusLost(FocusEvent e) {
				if(	electron_config4.getText().matches("")) electron_config4.setText("0");
			}
		});
		electron_config4.setText("0");
		electron_config4.setEnabled(false);
		panel.add(electron_config4);
		electron_config4.setColumns(2);
		
		electron_config5 = new JTextField();
		electron_config5.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				if(electron_config5.getText().matches("0"))	electron_config5.setText("");
			}
			public void focusLost(FocusEvent e) {
				if(	electron_config5.getText().matches("")) electron_config5.setText("0");
			}
		});
		electron_config5.setText("0");
		electron_config5.setEnabled(false);
		panel.add(electron_config5);
		electron_config5.setColumns(2);
		
		electron_config6 = new JTextField();
		electron_config6.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				if(electron_config6.getText().matches("0"))	electron_config6.setText("");
			}
			public void focusLost(FocusEvent e) {
				if(	electron_config6.getText().matches("")) electron_config6.setText("0");
			}
		});
		electron_config6.setText("0");
		electron_config6.setEnabled(false);
		panel.add(electron_config6);
		electron_config6.setColumns(2);
		
		electron_config7 = new JTextField();
		electron_config7.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				if(electron_config7.getText().matches("0"))	electron_config7.setText("");
			}
			public void focusLost(FocusEvent e) {
				if(	electron_config7.getText().matches("")) electron_config7.setText("0");
			}
		});
		electron_config7.setText("0");
		electron_config7.setEnabled(false);
		panel.add(electron_config7);
		electron_config7.setColumns(2);
		btnBuildAtom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!electron_config1.getText().matches("") && !electron_config2.getText().matches("") && !electron_config3.getText().matches("") && !electron_config4.getText().matches("") && !electron_config5.getText().matches("") && !electron_config6.getText().matches("") && !electron_config7.getText().matches("")&& !proton_amount.getText().matches("")){
					//PREPARE ELECTRON CONFIGURATION:
					List<String>temp_electron=new ArrayList<String>();
					temp_electron.add(electron_config1.getText());
					temp_electron.add(electron_config2.getText());
					temp_electron.add(electron_config3.getText());
					temp_electron.add(electron_config4.getText());
					temp_electron.add(electron_config5.getText());
					temp_electron.add(electron_config6.getText());
					temp_electron.add(electron_config7.getText());
					int electron_counter=0;
					for(int i=0;i<temp_electron.size();i++) if(!temp_electron.get(i).matches("0")) electron_counter++; 
					electron_config=new int[electron_counter];
					for(int i=0;i<temp_electron.size();i++)	if(!temp_electron.get(i).matches("0")) electron_config[i]=Integer.valueOf(temp_electron.get(i));
					
					//SET PROTON AMOUNT:
					protons=Integer.valueOf(proton_amount.getText());
					
					//CREATE ATOM:
					new Thread(){public void run(){
							try {Atom atom;
								atom = Atom.buildAtom(protons, electron_config);
								if(!atom.getSymbol().contains("<error>")){
									atoms.add(atom);
									atom_list.get(get_selection()[0]).setText(atom.getSymbol());
									btnAtomInfo.setEnabled(true);
									if(i_atom_list>1)btnMakeMolecule.setEnabled(true);
									atombench.updateUI();
									for(int i=0;i<atom_list.size();i++)	deselect_atom(new int[]{i});
								}
							}catch (Exception e) {e.printStackTrace();}
					}}.start();
				}
			}
		});
		
		btnBuildAtom.setEnabled(false);
		panel.add(btnBuildAtom);
		
		btnRemoveAtom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				remove_atom();
			}
		});
		btnRemoveAtom.setEnabled(false);
		panel.add(btnRemoveAtom);
		
		btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				textArea.setText("");
			}
		});
		panel.add(btnClear);
		
		panel_1 = new JPanel();
		frame.getContentPane().add(panel_1, BorderLayout.CENTER);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{1, 591, 0, 0, 46, 49, 0};
		gbl_panel_1.rowHeights = new int[]{59, 25, 168, 0};
		gbl_panel_1.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		atombench.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		GridBagConstraints gbc_atombench = new GridBagConstraints();
		gbc_atombench.fill = GridBagConstraints.BOTH;
		gbc_atombench.insets = new Insets(0, 0, 5, 0);
		gbc_atombench.gridwidth = 6;
		gbc_atombench.gridx = 0;
		gbc_atombench.gridy = 0;
		panel_1.add(atombench, gbc_atombench);
		
		atombench.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				for(int i=0;i<atom_list.size();i++)	atom_list.get(i).setForeground(Color.BLACK);
				disable_buttons(false);
				atombench.updateUI();
			}
		});
		
		btnMoleculeInfo = new JButton("Molecule Info");
		btnMoleculeInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(int i=0;i<molecules.size();i++){
					System.out.println("*******");
					System.out.println(molecules.get(i).properties.getMoleculeName());
					System.out.println(molecules.get(i).properties.getChemical_formula());
					System.out.println(molecules.get(i).properties.getMoreinfo());
					System.out.println("*******\n");
				}
			}
		});
		btnMoleculeInfo.setEnabled(false);
		GridBagConstraints gbc_btnMoleculeInfo = new GridBagConstraints();
		gbc_btnMoleculeInfo.insets = new Insets(0, 0, 5, 5);
		gbc_btnMoleculeInfo.gridx = 2;
		gbc_btnMoleculeInfo.gridy = 1;
		panel_1.add(btnMoleculeInfo, gbc_btnMoleculeInfo);
		
		btnAtomInfo = new JButton("Atom Info");
		btnAtomInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index[];
				if((index=get_selection()).length==1){
					String elementSymbol=atom_list.get(index[0]).getText().replaceAll("[0-9]+", "");
					for(int i=0;i<atoms.size();i++)
						if(atoms.get(i).getSymbol().matches(elementSymbol)){
							atoms.get(i).printData();
							break;
						}
				}
			}
		});
		btnAtomInfo.setEnabled(false);
		GridBagConstraints gbc_btnAtomInfo = new GridBagConstraints();
		gbc_btnAtomInfo.anchor = GridBagConstraints.WEST;
		gbc_btnAtomInfo.insets = new Insets(0, 0, 5, 5);
		gbc_btnAtomInfo.gridx = 3;
		gbc_btnAtomInfo.gridy = 1;
		panel_1.add(btnAtomInfo, gbc_btnAtomInfo);
		
		JLabel lblAmount = new JLabel("\r\nAmount:");
		GridBagConstraints gbc_lblAmount = new GridBagConstraints();
		gbc_lblAmount.anchor = GridBagConstraints.EAST;
		gbc_lblAmount.insets = new Insets(0, 0, 5, 5);
		gbc_lblAmount.gridx = 4;
		gbc_lblAmount.gridy = 1;
		panel_1.add(lblAmount, gbc_lblAmount);
		atom_set_amount.addMouseListener(new MouseAdapter() {
			public void mouseExited(MouseEvent e) {
				int index[];
				if((index=get_selection()).length>0 && !atom_list.get(index[0]).getText().contains("?")){
					String atomtempname=atom_list.get(index[0]).getText();
					atom_set_amount.setValue(1);
					atom_list.get(index[0]).setText(atomtempname);
				}
			}
		});
		atom_set_amount.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				int []index;
				if((index=get_selection()).length==1){
					if(!atom_list.get(index[0]).getText().contains("?")){
						try{
							Matcher matcher = Pattern.compile("([0-9]+)").matcher(atom_list.get(index[0]).getText());matcher.find();
							matcher.group(0);
						}catch(Exception e){atom_set_amount.setValue(2);}
						
						atom_list.get(index[0]).setText(atom_list.get(index[0]).getText().replaceAll("[0-9]+", ""));
						if(Integer.valueOf(atom_set_amount.getValue().toString())>1)
							atom_list.get(index[0]).setText(atom_list.get(index[0]).getText()+atom_set_amount.getValue());
						else atom_list.get(index[0]).setText(atom_list.get(index[0]).getText().replaceAll("[0-9]+",""));
					}else atom_set_amount.setValue(1);
				}
			}
		});
		atom_set_amount.setEnabled(false);
		atom_set_amount.setModel(new SpinnerNumberModel(1, 1, 100, 1));
		GridBagConstraints gbc_atom_set_amount = new GridBagConstraints();
		gbc_atom_set_amount.fill = GridBagConstraints.BOTH;
		gbc_atom_set_amount.insets = new Insets(0, 0, 5, 0);
		gbc_atom_set_amount.gridx = 5;
		gbc_atom_set_amount.gridy = 1;
		panel_1.add(atom_set_amount, gbc_atom_set_amount);
		panel_2 = new JPanel();
		panel_2.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridwidth = 6;
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 2;
		panel_1.add(panel_2, gbc_panel_2);
		panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.X_AXIS));
		
		scrollPane = new JScrollPane();
		panel_2.add(scrollPane);
		
		textArea = new JTextArea();
		textArea.setFont(new Font("Arial", Font.PLAIN, 13));
		textArea.setEditable(false);
		TextAreaOutputStream taos = new TextAreaOutputStream( textArea, 60 );
        PrintStream ps=new PrintStream(taos);
        System.setOut(ps);
        System.setErr(ps);
		scrollPane.setViewportView(textArea);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JButton btnNewAtom = new JButton("New atom");
		btnNewAtom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(i_atom_list<=20){
					JLabel atom=new JLabel("?");
					atom.setFont(new Font("Arial",Font.PLAIN,45));
					atom.setName(i_atom_list+"");
					atom_list.add(atom);
					atom_list.get(i_atom_list).addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent e) {
							int index=Integer.valueOf(e.getComponent().getName().toString());
							if(atom_list.get(index).getForeground()==Color.RED)	deselect_atom(new int[]{index});
							else if(e.getButton()==1)
									select_atom(new int[]{index},false);
							else if(e.getButton()==3){
								//MULTI SELECTION
								select_atom(new int[]{index},true);
								disable_buttons(true);
							}
						}
					});
					atom_list.get(i_atom_list).addMouseListener(new MouseAdapter() {
						public void mouseEntered(MouseEvent e) {
							if(e.getComponent().getForeground()!=Color.RED){
								e.getComponent().setForeground(Color.BLUE);
							}
							e.getComponent().setFont(new Font("Arial",Font.PLAIN,47));
						}
						public void mouseExited(MouseEvent e) {
							if(e.getComponent().getForeground()!=Color.RED){
								e.getComponent().setForeground(Color.BLACK);
							}
							e.getComponent().setFont(new Font("Arial",Font.PLAIN,45));
						}
					});
					atombench.add(atom_list.get(i_atom_list));
					i_atom_list++;
				}
				atombench.updateUI();
			}
		});
		menuBar.add(btnNewAtom);
		btnMakeMolecule.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					for(int i=0;i<atoms.size();i++){
						if(!atom_list.get(i).getText().matches(".*\\d.*")) Atom.addAtom(atoms.get(i), 1); 
						else{Matcher matcher = Pattern.compile("([0-9]+)").matcher(atom_list.get(i).getText());matcher.find();
							Atom.addAtom(atoms.get(i), Integer.valueOf(matcher.group(0)));
						}
					}
					new Thread(){public void run(){
							molecules.add(new Molecule(Atom.getAtomList()));
							Atom.getAtomList().clear();
							btnMoleculeInfo.setEnabled(true);
						}
					}.start();
				}catch(Exception e2){e2.printStackTrace();}
			}
		});
		
		btnMakeMolecule.setEnabled(false);
		menuBar.add(btnMakeMolecule);
	}
}