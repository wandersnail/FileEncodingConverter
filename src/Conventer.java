import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileSystemView;
import javax.swing.JTextPane;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.awt.event.ActionEvent;

public class Conventer {

	private JFrame frame;
	private JTextField textFolder;
	private JTextField textField_charset;
	private JTextField textDstFolder;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Conventer window = new Conventer();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Conventer() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmOpen = new JMenuItem("Open");
		mnFile.add(mntmOpen);
		frame.getContentPane().setLayout(new MigLayout("", "[61px][grow][]", "[16px][][grow][][]"));
		
		JLabel lblNewLabel = new JLabel("Source Folder:");
		lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel.setVerticalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(lblNewLabel, "cell 0 0,alignx right,aligny center");
		
		textFolder = new JTextField();
		frame.getContentPane().add(textFolder, "cell 1 0,growx");
		textFolder.setColumns(10);
		
		JButton btnChoose = new JButton("Choose");
		btnChoose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				jfc.setDialogTitle("Choose the source directory to open: ");
				jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

				int returnValue = jfc.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					if (jfc.getSelectedFile().isDirectory()) {
//						System.out.println("You selected the directory: " + jfc.getSelectedFile());
						textFolder.setText(jfc.getSelectedFile().getPath());
					}
				}
				
			}
		});
		frame.getContentPane().add(btnChoose, "cell 2 0");
		
		JLabel lblDestFolder = new JLabel("Dest Folder:");
		frame.getContentPane().add(lblDestFolder, "cell 0 1,alignx trailing");
		
		textDstFolder = new JTextField();
		frame.getContentPane().add(textDstFolder, "cell 1 1,growx");
		textDstFolder.setColumns(10);
		
		JButton btnChooseDstFolder = new JButton("Choose");
		btnChooseDstFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				jfc.setDialogTitle("Choose a directory to save your files: ");
				jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

				int returnValue = jfc.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					if (jfc.getSelectedFile().isDirectory()) {
//						System.out.println("You selected the directory: " + jfc.getSelectedFile());
						textDstFolder.setText(jfc.getSelectedFile().getPath());
					}
				}
			}
		});
		frame.getContentPane().add(btnChooseDstFolder, "cell 2 1");
		
		JLabel lblExt = new JLabel("Ext:");
		lblExt.setHorizontalAlignment(SwingConstants.RIGHT);
		frame.getContentPane().add(lblExt, "cell 0 2,alignx right,aligny top");
		
		JTextPane textExt = new JTextPane();
		frame.getContentPane().add(textExt, "cell 1 2,grow");
		
		JLabel lblNewLabel_1 = new JLabel("Dst Charset:");
		frame.getContentPane().add(lblNewLabel_1, "cell 0 3,alignx trailing");
		
		textField_charset = new JTextField();
		frame.getContentPane().add(textField_charset, "cell 1 3,growx");
		textField_charset.setColumns(10);
		
		JButton btnConvert = new JButton("Convert");
		btnConvert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				String srcFolder = textFolder.getText();
//				String str = textExt.getText();
				Charset targetCharset = Charset.forName(textField_charset.getText());
				List<String> suffixs = Arrays.asList(textExt.getText().split(","));
//				ConvertControl.runConvertCharset(targetCharset, suffixs, new File(srcFolder));
				ConvertControl control = new ConvertControl();
				control.setSrcFloder(new File(textFolder.getText()));
				control.setDstFolder(new File(textDstFolder.getText()));
				control.setSuffixs(suffixs);
				control.setTargetCharset(targetCharset);
				control.runConvertCharset();
			}
		});
		frame.getContentPane().add(btnConvert, "cell 1 4");
	}

}