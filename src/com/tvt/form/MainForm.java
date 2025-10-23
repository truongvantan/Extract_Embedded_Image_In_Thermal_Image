package com.tvt.form;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainForm extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JMenuBar menuBar;
	private JMenu mnNewMenu;
	private JMenuItem menuItemAccessPixelValueDemo;
	private JMenuItem menuItemExtractEmbeddedImage;
	
	
	public MainForm() {
		initComponent();
		
	}
	
	private void initComponent() {
		this.setTitle("Main Form");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 1100, 619);
		this.setLocationRelativeTo(null);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnNewMenu = new JMenu("Excersise");
		menuBar.add(mnNewMenu);
		
		menuItemAccessPixelValueDemo = new JMenuItem("Access Pixel Value Demo");
		mnNewMenu.add(menuItemAccessPixelValueDemo);
		
		menuItemExtractEmbeddedImage = new JMenuItem("Extract Embedded Image");
		mnNewMenu.add(menuItemExtractEmbeddedImage);
		
		handleEvent();
	}
	
	private void handleEvent() {
		menuItemAccessPixelValueDemo_Click();
		menuItemExtractEmbeddedImage_Click();
	}

	private void menuItemAccessPixelValueDemo_Click() {
		menuItemAccessPixelValueDemo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame form = new Form_AccessPixelValueDemo();
				form.setVisible(true);
			}
		});
	}

	private void menuItemExtractEmbeddedImage_Click() {
		menuItemExtractEmbeddedImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame form = new Form_ExtractEmbeddedImage();
				form.setVisible(true);
			}
		});
	}
}
