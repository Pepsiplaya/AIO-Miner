package me.pepsi.xeros.mining;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class GUI {

	public AIOMining mine;
	private JPanel contentPane;
	public JFrame frame;
	
	public JPanel generalPanel;
	public JComboBox<Ores> comboBoxOres;
	public JSpinner spinnerHealAt;
	public JButton btnApplyNewSettings;
	public JButton btnCloseGui;
	public JList<String> listNearbyMonsters;
	public JLabel lblMonsterList;
	public JList<String> listSelectedMonsters;
	public JScrollPane scrollPaneNearbyMonsters;
	public JScrollPane scrollPaneSelectedMonsters;
	public JList<String> listLoot;
	public JScrollPane scrollPaneLoot;
	public DefaultListModel<String> modelLoot = new DefaultListModel<String>();
	public DefaultListModel<String> modelNearbyMonsters = new DefaultListModel<String>();
	public DefaultListModel<String> modelSelectedMonsters = new DefaultListModel<String>();
	
	public GUI(AIOMining mine) {
		try {
			this.mine = mine;
			SwingUtilities.invokeLater(new Runnable() {  public void run() {

			frame = new JFrame();
			frame.setResizable(false);
			frame.setTitle("AIO Mining");
			frame.setBounds(100, 100, 220, 210);
			contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			frame.setContentPane(contentPane);
			contentPane.setLayout(null);
			frame.setLocationRelativeTo(mine.ctx.mouse.getComponent());
			initComponents();
			frame.repaint();
			frame.setVisible(true);
				     }
			   });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void initComponents() {
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 11, 190, 75);
		contentPane.add(tabbedPane);
		
		generalPanel = new JPanel();
		generalPanel.setName("");
		tabbedPane.addTab("General", null, generalPanel, null);
		generalPanel.setLayout(null);
		
		btnApplyNewSettings = new JButton("Apply New Settings");
		btnApplyNewSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onApplyNewSettings();
			}
		});
		btnApplyNewSettings.setBounds(45, 93, 125, 23);
		contentPane.add(btnApplyNewSettings);
		
		btnCloseGui = new JButton("Close GUI");
		btnCloseGui.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onCloseGUI();
			}
		});
		btnCloseGui.setBounds(65, 120, 89, 23);
		contentPane.add(btnCloseGui);
		
		JLabel lbloreType = new JLabel("Ore Type:");
		lbloreType.setBounds(10, 11, 81, 20);
		generalPanel.add(lbloreType);
		
		comboBoxOres = new JComboBox<Ores>(new DefaultComboBoxModel<Ores>(Ores.values()));
		comboBoxOres.setBounds(93, 11, 91, 20);
		generalPanel.add(comboBoxOres);
	}
	
	public void onApplyNewSettings() {
		Ores oreType = (Ores) comboBoxOres.getSelectedItem();
		mine.setupOres(oreType.getObjectId());
		mine.started = true;
	}
	
	public void onCloseGUI() {
		frame.dispose();
	}
}