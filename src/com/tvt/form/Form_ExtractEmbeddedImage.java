package com.tvt.form;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.tvt.common.ImageCommonHandle;

import jnafilechooser.api.JnaFileChooser;

public class Form_ExtractEmbeddedImage extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private static final String OUTPUT_EMBEDDED_IMAGE_FOLDER = "D:\\Tan\\Sample_Data\\Embedded_Image";
	
	private ImageCommonHandle imageCommonHandle = new ImageCommonHandle();
	private JPanel contentPane;
	private JButton btnLoadImage;
	private JList<String> listImage;
	private DefaultListModel<String> listModel;
	private JScrollPane scrollPane;
	private JPanel panelInputImage;
	private JLabel lbInputImage;
	private JLabel lbLoadInputImage;
	private JPanel panelOutputImage;
	private JLabel lbLoadOutputImage;
	private JLabel lbOutputImage;
	
	public Form_ExtractEmbeddedImage() {
		initComponent();
	}
	
	private void initComponent() {
		this.setTitle("Excersise 2");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setBounds(100, 100, 1439, 737);
		this.setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setContentPane(contentPane);
		contentPane.setLayout(null);

		btnLoadImage = new JButton("Load image path");

		btnLoadImage.setBounds(30, 11, 129, 40);
		contentPane.add(btnLoadImage);

		listModel = new DefaultListModel<>();

		scrollPane = new JScrollPane();
		scrollPane.setBounds(30, 62, 327, 433);
		contentPane.add(scrollPane);

		listImage = new JList<>(listModel);
		listImage.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		scrollPane.setViewportView(listImage);

		panelInputImage = new JPanel();
		panelInputImage.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelInputImage.setBounds(439, 62, 424, 433);
		contentPane.add(panelInputImage);
		panelInputImage.setLayout(null);

		lbLoadInputImage = new JLabel("");
		lbLoadInputImage.setBounds(0, 0, 424, 433);
		panelInputImage.add(lbLoadInputImage);

		lbInputImage = new JLabel("Input Image");
		lbInputImage.setFont(new Font("Tahoma", Font.BOLD, 13));
		lbInputImage.setBounds(439, 24, 95, 14);
		contentPane.add(lbInputImage);

		panelOutputImage = new JPanel();
		panelOutputImage.setLayout(null);
		panelOutputImage.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelOutputImage.setBounds(923, 62, 424, 433);
		contentPane.add(panelOutputImage);

		lbLoadOutputImage = new JLabel("");
		lbLoadOutputImage.setBounds(0, 0, 424, 433);
		panelOutputImage.add(lbLoadOutputImage);

		lbOutputImage = new JLabel("Output Image");
		lbOutputImage.setFont(new Font("Tahoma", Font.BOLD, 13));
		lbOutputImage.setBounds(923, 23, 95, 14);
		contentPane.add(lbOutputImage);
		
		handleEvent();
	}

	private void handleEvent() {
		btnLoadImage_Click();
		listImage_ValueChanged();
	}
	

	private void btnLoadImage_Click() {
		btnLoadImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JnaFileChooser fileChooser = new JnaFileChooser();
				fileChooser.setMultiSelectionEnabled(true);
				fileChooser.setCurrentDirectory("D:\\Tan\\Sample_Data\\");
				fileChooser.addFilter("Image Files", "gif", "jpg", "png", "gif", "tif", "bmp");
				fileChooser.addFilter("All files", "*.*");

				if (fileChooser.showOpenDialog(getOwner())) {
					File[] selectedFiles = fileChooser.getSelectedFiles();
					for (File file : selectedFiles) {
						listModel.addElement(file.getAbsolutePath());
					}
				}
			}
		});
	}

	private void listImage_ValueChanged() {
		listImage.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				String pathImageSelected = listImage.getSelectedValue();

				// load image to picturebox
				imageCommonHandle.loadImageToLabel(pathImageSelected, lbLoadInputImage, panelInputImage);

				// 
				extractEmbeddedImage(pathImageSelected);
			}
		});
	}

	private void extractEmbeddedImage(String pathImageSelected) {
		File outputDir = new File(OUTPUT_EMBEDDED_IMAGE_FOLDER);
        if (!outputDir.exists()) {
        	outputDir.mkdirs();
        }
        
        // Format output file name
        File inputFile = new File(pathImageSelected);
        String baseName = inputFile.getName().replaceFirst("[.][^.]+$", ""); // remove extension
        String outputPath = OUTPUT_EMBEDDED_IMAGE_FOLDER + "\\" + baseName + "_embedded.png";
        
        /**
         * Run ExifTool to extract embedded image
         * Example: exiftool -b -EmbeddedImage input.jpg > output.png
         * */
        ProcessBuilder pb = new ProcessBuilder(
                "exiftool", "-b", "-EmbeddedImage", pathImageSelected
        );
        
        try {
			pb.redirectOutput(new File(outputPath));
			Process process = pb.start();
			process.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Cannot extract embedded image: " + pathImageSelected, "WARNING", JOptionPane.WARNING_MESSAGE);
		} catch (InterruptedException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Cannot extract embedded image: " + pathImageSelected, "WARNING", JOptionPane.WARNING_MESSAGE);
		}
        
        // Show extracted embedded image
        try {
			File embeddedFile = new File(outputPath);
			
			if (embeddedFile.exists() && embeddedFile.length() > 0) {
				imageCommonHandle.loadImageToLabel(outputPath, lbLoadOutputImage, panelOutputImage);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Cannot extract embedded image: " + pathImageSelected, "WARNING", JOptionPane.WARNING_MESSAGE);
		}
	}

}
