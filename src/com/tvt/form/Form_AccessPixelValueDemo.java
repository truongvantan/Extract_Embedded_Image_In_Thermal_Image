package com.tvt.form;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

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

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import com.tvt.common.ImageCommonHandle;

import jnafilechooser.api.JnaFileChooser;

public class Form_AccessPixelValueDemo extends JFrame {

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	private static final long serialVersionUID = 1L;
	
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
	private JButton btnClear;

	/**
	 * Create the frame.
	 */
	public Form_AccessPixelValueDemo() {
		initComponent();
	}

	private void initComponent() {
		this.setTitle("Excersise 1");
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
		
		btnClear = new JButton("Clear");
		
		btnClear.setBorderPainted(false);
		btnClear.setForeground(new Color(255, 255, 255));
		btnClear.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnClear.setBackground(new Color(109, 185, 40));
		btnClear.setBounds(206, 11, 129, 40);
		contentPane.add(btnClear);

		handleEvent();
	}

	private void handleEvent() {
		btnLoadImage_Click();
		listImage_ValueChanged();
		btnClear_Click();
	}
	
	private void btnClear_Click()
	{
//		btnClear.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				listImage.removeListSelectionListener();
//				listModel.removeAllElements();
//			}
//		});
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

				// process image with opencv
				processImage(pathImageSelected);
			}
		});
	}

	private void processImage(String pathImageSelected) {
		Mat input = Imgcodecs.imread(pathImageSelected, Imgcodecs.IMREAD_COLOR);
		
		if (input.empty()) {
			JOptionPane.showMessageDialog(null, "Cannot open image: " + pathImageSelected, "WARNING", JOptionPane.WARNING_MESSAGE);
			return;
		}

		Mat output = new Mat(input.rows(), input.cols(), input.type());

		int rows = input.rows();
		int cols = input.cols();
		int channels = input.channels();

		double[] pixel = new double[channels];
		double[] newPixel = new double[channels];

		for (int y = 0; y < rows; y++) {
			for (int x = 0; x < cols; x++) {
				pixel = input.get(y, x);
				
				if (pixel != null) {
					for (int c = 0; c < channels; c++) {
//						newPixel[c] = Math.abs(pixel[c] - 255.0);
						newPixel[c] = 2 * pixel[c];
						if (newPixel[c] > 255.0) {
							newPixel[c] = 255.0;
						}
						if (newPixel[c] < 0.0) {
							newPixel[c] = 0.0;
						}
					}
					output.put(y, x, newPixel);
				}
			}
		}
		
		imageCommonHandle.loadCVMatToLabel(output, lbLoadOutputImage, panelOutputImage);
		
		input.release();
		output.release();

	}
}
