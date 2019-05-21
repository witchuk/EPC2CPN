package main;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import object.Model;
import object.Project;

import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JMenu;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JSeparator;
import java.awt.Font;
import java.awt.Color;
import java.awt.Desktop;

import javax.swing.JMenuItem;


public class EPC2CPN extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static EPC2CPN frame;
	
	private JPanel contentPane;
	private JMenuItem mntmReset;
	private JMenuItem mntmExit;
	private JMenuItem mntmAboutUs;
	private JTextField textField;
	private JLabel lblResult1;
	private JLabel lblResult2;
	private JLabel lblResult3;
	private JButton btnBrowse;
	private JButton btnStartTransform;
	private JButton btnGenerateCPN;
	private JButton btnOpenCpnTools;
	private JButton btnViewEPCElement;
	
	private File selectedFile;
	private String elementInfo = "No Data";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new EPC2CPN();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public EPC2CPN() {
		setResizable(false);
		setTitle("EPC2CPN");
		setBounds(100, 100, 760, 400);
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener( new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				closeApplication();
		    }
		});
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		mntmReset = new JMenuItem("Reset");
		mntmReset.addActionListener(this);
		mnFile.add(mntmReset);
		mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(this);
		mnFile.add(mntmExit);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		mntmAboutUs = new JMenuItem("About EPC2CPN");
		mnHelp.add(mntmAboutUs);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblHeader1 = new JLabel("STEP I : Browse and Transform EPC Diagram File");
		lblHeader1.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblHeader1.setBounds(20, 15, 230, 14);
		contentPane.add(lblHeader1);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(20, 30, 715, 2);
		contentPane.add(separator);
		
		JLabel lblEpcDiagram = new JLabel("EPC Diagram");
		lblEpcDiagram.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblEpcDiagram.setBounds(40, 60, 80, 14);
		contentPane.add(lblEpcDiagram);
		
		textField = new JTextField();
		textField.setBounds(140, 57, 470, 20);
		textField.setColumns(10);
		contentPane.add(textField);
		
		btnBrowse = new JButton("Browse...");
		btnBrowse.setBounds(635, 56, 80, 23);
		btnBrowse.addActionListener(this);
		contentPane.add(btnBrowse);
		
		btnStartTransform = new JButton("Start Transaform");
		btnStartTransform.setBounds(140, 88, 150, 23);
		btnStartTransform.addActionListener(this);
		contentPane.add(btnStartTransform);
				
		JLabel lblHeader2 = new JLabel("STEP II : Transform Result");
		lblHeader2.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblHeader2.setBounds(20, 145, 180, 14);
		contentPane.add(lblHeader2);
				
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(20, 160, 715, 2);
		contentPane.add(separator_1);
		
		JLabel lblNewLabel_1 = new JLabel("File name");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblNewLabel_1.setBounds(40, 190, 80, 14);
		contentPane.add(lblNewLabel_1);
		
		lblResult1 = new JLabel();
		lblResult1.setBounds(140, 190, 150, 14);
		contentPane.add(lblResult1);
		
		JLabel lblNewLabel_2 = new JLabel("File Size");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblNewLabel_2.setBounds(40, 215, 46, 14);
		contentPane.add(lblNewLabel_2);
		
		lblResult2 = new JLabel();
		lblResult2.setBounds(140, 215, 150, 14);
		contentPane.add(lblResult2);
		
		JLabel lblNewLabel_3 = new JLabel("Status");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblNewLabel_3.setBounds(40, 240, 46, 14);
		contentPane.add(lblNewLabel_3);
		
		lblResult3 = new JLabel();
		lblResult3.setBounds(140, 240, 150, 14);
		contentPane.add(lblResult3);
		
		btnGenerateCPN = new JButton("Generate CPN");
		btnGenerateCPN.setBounds(310, 290, 150, 23);
		btnGenerateCPN.addActionListener(this);
		contentPane.add(btnGenerateCPN);
		
		btnOpenCpnTools = new JButton("Open CPN Tools");
		btnOpenCpnTools.setBounds(480, 290, 150, 23);
		btnOpenCpnTools.addActionListener(this);
		contentPane.add(btnOpenCpnTools);
		
		btnViewEPCElement = new JButton("View EPC Element");
		btnViewEPCElement.setBounds(140, 290, 150, 23);
		btnViewEPCElement.addActionListener(this);
		contentPane.add(btnViewEPCElement);
	}
	
	private void resetResultText() {
		lblResult1.setText("");
		lblResult2.setText("");
		lblResult3.setText("");
		
		elementInfo = "No Data";
		
		//btnGenerateCPN.setEnabled(false);
		//btnOpenCpnTools.setEnabled(false);
	}
	
	private void closeApplication() {
		int result = JOptionPane.showConfirmDialog(
			frame,
			"Are you sure you want to exit the application?",
			"Exit Application",
			JOptionPane.YES_NO_OPTION);
		if (result == JOptionPane.YES_OPTION) {
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(false);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// Handle "Exit" menu item action.
		if (e.getSource() == mntmExit) {
			closeApplication();
		}
		
		// Handle "Reset" menu item action.
		else if (e.getSource() == mntmReset) {
			resetResultText();
			textField.setText("");
			selectedFile = null;
		}
		
		// Handle "Browse..." button action.
		else if (e.getSource() == btnBrowse) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("XML File (*.xml)", "xml"));
			fileChooser.setAcceptAllFileFilterUsed(false);
			int result = fileChooser.showOpenDialog(frame);
			if (result == JFileChooser.APPROVE_OPTION) {
				selectedFile = fileChooser.getSelectedFile();
				textField.setText(selectedFile.getAbsolutePath().toString());
				
			}
		}
		
		// Handle "Start Transform" button action.
		else if (e.getSource() == btnStartTransform) {
			resetResultText();
			//btnGenerateCPN.setEnabled(true);
			//btnOpenCpnTools.setEnabled(true);
			
			if (selectedFile != null && selectedFile.isFile()) {
				lblResult1.setFont(new Font("Tahoma", Font.BOLD, 11));
				lblResult1.setText(selectedFile.getName().toString());
				
				double kilobytes = (selectedFile.length() / 1024);
				lblResult2.setFont(new Font("Tahoma", Font.BOLD, 11));
				lblResult2.setText(String.valueOf(kilobytes) + " KB");
				
				try {
					// 1. Check & Add XML Namespace
					String contents = new String(Files.readAllBytes(selectedFile.toPath()));
					if (contents.contains("<Project ")) {
						if (!contents.contains("xmlns=\"http://www.visual-paradigm.com/product/vpuml/modelexporter\"")) {
							contents = contents.replaceFirst("<Project ", "<Project xmlns=\"http://www.visual-paradigm.com/product/vpuml/modelexporter\" ");
						
							FileOutputStream outputStream = new FileOutputStream(selectedFile.getAbsolutePath());
							byte[] strToBytes = contents.getBytes();
						    outputStream.write(strToBytes);
						    outputStream.close();
						    
						    selectedFile = new File(selectedFile.getAbsolutePath());
						}
					}
					
					// 2. Convert XML File to Java Object
					boolean isSuccess = true;
					String transfromStatus = "Success";
					
					JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
					Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
					Project project = (Project) unmarshaller.unmarshal(selectedFile);
					
					int sumEvent = 0;
					int sumFunction = 0;
					int sumProcessPath = 0;
					int sumAndOperator = 0;
					int sumOrOperator = 0;
					int sumXorOperator = 0;
					int sumInfoResource = 0;
					int sumOrgUnit = 0;
					int sumRole = 0;
					int sumSystem = 0;
					
					Map<String, List<Model>> epcElement = new HashMap<String, List<Model>>();
					for (Model model : project.getModels().getModel()) {
						if (model.getModelType().startsWith("EPC")) {
							if (!epcElement.containsKey(model.getModelType())) {
								epcElement.put(model.getModelType(), new ArrayList<Model>());
							} 
							epcElement.get(model.getModelType()).add(model);
						}
						
						if ("EPCEvent".equals(model.getModelType())) 
							sumEvent++;
						else if ("EPCFunction".equals(model.getModelType()))
							sumFunction++;
						else if ("EPCProcessPath".equals(model.getModelType()))
							sumProcessPath++;
						else if ("EPCAndOperator".equals(model.getModelType()))
							sumAndOperator++;
						else if ("EPCOrOperator".equals(model.getModelType()))
							sumOrOperator++;
						else if ("EPCXOROperator".equals(model.getModelType()))
							sumXorOperator++;
						else if ("EPCInformationResource".equals(model.getModelType()))
							sumInfoResource++;
						else if ("EPCOrganizationUnit".equals(model.getModelType()))
							sumOrgUnit++;
						else if ("EPCRole".equals(model.getModelType()))
							sumRole++;
						else if ("EPCSystem".equals(model.getModelType()))
							sumSystem++;
					}
					System.out.println(epcElement.size());
					
					elementInfo = "";
					elementInfo += "#Event: " + sumEvent + "\n";
					elementInfo += "#Function: " + sumFunction + "\n";
					elementInfo += "#ProcessPath: " + sumProcessPath + "\n";
					elementInfo += "#AND: " + sumAndOperator + "\n";
					elementInfo += "#OR: " + sumOrOperator + "\n";
					elementInfo += "#XOR: " + sumXorOperator + "\n";
					elementInfo += "#InformationResource: " + sumInfoResource + "\n";
					elementInfo += "#OrganizationUnit: " + sumOrgUnit + "\n";
					elementInfo += "#Role: " + sumRole + "\n";
					elementInfo += "#System: " + sumSystem + "\n";
					
					
					// Success
					if (isSuccess) {
						lblResult3.setFont(new Font("Tahoma", Font.BOLD, 11));
						lblResult3.setForeground(new Color(0, 204, 0));
						lblResult3.setText(transfromStatus);
					}
				} catch (Exception e1) {
					//e1.printStackTrace();
					
					// Error
					lblResult3.setFont(new Font("Tahoma", Font.BOLD, 11));
					lblResult3.setForeground(new Color(255, 0, 0));
					lblResult3.setText("Invalid xml file format");
				}
			}
		}
		
		// Handle "Open CPN Tools" button action.
		else if (e.getSource() == btnOpenCpnTools) {
			File file = new File("C:\\Users\\DELL-1809\\Documents\\CPN Project\\Test XOR_New.cpn");
			try {
				Desktop.getDesktop().open(file);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		// Handle "Generate CPN" button action.
		else if (e.getSource() == btnGenerateCPN) {
			JFileChooser fileChooser = new JFileChooser();
			int result = fileChooser.showSaveDialog(frame);
			if (result == JFileChooser.APPROVE_OPTION) {
				File targetFile = fileChooser.getSelectedFile();
		        try {
		            if (!targetFile.exists()) {
		                targetFile.createNewFile();
		            }
		            FileWriter fw = new FileWriter(targetFile);
		            fw.write("Hello World");
		            fw.close();
		            
		            JOptionPane.showMessageDialog(frame, "Generate CPN File Success!");
		        } catch (IOException e1) {
		            e1.printStackTrace();
		            JOptionPane.showMessageDialog(frame, "Generate CPN File Fail!", "Error", JOptionPane.ERROR_MESSAGE);
		        }
			}
		}
		
		// Handle "View EPC Element" button action.
		else if (e.getSource() == btnViewEPCElement) {
			JOptionPane.showMessageDialog(frame, elementInfo, "EPC Element", JOptionPane.INFORMATION_MESSAGE);
		}
	}
}
