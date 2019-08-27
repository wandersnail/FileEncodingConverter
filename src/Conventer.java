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
import java.awt.event.ActionEvent;

public class Conventer {

	private JFrame frame;
	private JTextField textFolder;
	private JTextField textField_charset;

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
		frame.getContentPane().setLayout(new MigLayout("", "[61px][grow][]", "[16px][grow][][]"));
		
		JLabel lblNewLabel = new JLabel("Convert Folder:");
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
				jfc.setDialogTitle("Choose a directory to save your file: ");
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
		
		JLabel lblExt = new JLabel("Ext:");
		lblExt.setHorizontalAlignment(SwingConstants.RIGHT);
		frame.getContentPane().add(lblExt, "cell 0 1,alignx right,aligny top");
		
		JTextPane textExt = new JTextPane();
		frame.getContentPane().add(textExt, "cell 1 1,grow");
		
		JLabel lblNewLabel_1 = new JLabel("Dst Charset:");
		frame.getContentPane().add(lblNewLabel_1, "cell 0 2,alignx trailing");
		
		textField_charset = new JTextField();
		frame.getContentPane().add(textField_charset, "cell 1 2,growx");
		textField_charset.setColumns(10);
		
		JButton btnConvert = new JButton("Convert");
		btnConvert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConvertControl.runConvertCharset(targetCharset, suffixs, srcFolder);
			}
		});
		frame.getContentPane().add(btnConvert, "cell 1 3");
	}

}
