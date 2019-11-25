package main;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.FileUtils;

import main.contant.Constant;
import main.model.Arc;
import main.model.CPNObject;
import main.model.EPCElement;
import main.model.Variable;
import main.util.TranformUtils;
import object.visualparadigm.Connector;
import object.visualparadigm.Connectors;
import object.visualparadigm.Diagram;
import object.visualparadigm.Project;
import object.visualparadigm.Shape;
import object.visualparadigm.Shapes;

import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JMenu;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.Color;
import java.awt.Desktop;

import javax.swing.JMenuItem;
import javax.swing.JTree;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

public class EPC2CPN extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static EPC2CPN frame;

	private JMenuItem mntmSetting;
	private JMenuItem mntmReset;
	private JMenuItem mntmExit;
	private JMenuItem mntmAboutUs;
	private JTextField textField;
	private JLabel lblResult1;
	private JLabel lblResult2;
	private JLabel lblResult3;
	private JButton btnBrowse;
	private JButton btnGenerateCPN;
	private JButton btnOpenCpnTools;
	private JButton btnVerified;
	private JTree treeResult;
	private JTable tableResult;
	
	private File selectedFile;
	private File targetFile;
	private String elementInfo = "No Data";
	private Shapes elementShapes = null;
	private Connectors elementConnectors = null;
	
	private Map<String, String> mapVariable = null;
	private Map<String, Shape> mapShape = null;
	private Map<String, EPCElement> mapEPCElement = null;
	
	private List<EPCElement> epcElementList;
	
	private static Properties properties;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		// Load Configuration
		try (InputStream input = new FileInputStream("src/resources/config.properties")) {
			properties = new Properties();
			properties.load(input);
			System.out.println("Load config.properties Success!");
		} catch (IOException ex) {
			System.out.println("Sorry, unable to find config.properties");
            return;
		}
		
		// Invoke Main Frame
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
		// ---------- Frame ----------
		setResizable(false);
		setTitle("EPC2CPN");
		setBounds(100, 100, 806, 555);
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener( new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				closeApplication();
		    }
		});
		
		// ---------- Menu ----------
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		mntmSetting = new JMenuItem("Setting");
		mntmSetting.addActionListener(this);
		mnFile.add(mntmSetting);
		mntmReset = new JMenuItem("Reset");
		mntmReset.addActionListener(this);
		mnFile.add(mntmReset);
		mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(this);
		mnFile.add(mntmExit);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		mntmAboutUs = new JMenuItem("About EPC2CPN");
		mntmAboutUs.addActionListener(this);
		mnHelp.add(mntmAboutUs);
		// --------------------------
		
		// ---------- Content Panel ----------
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// ---------- Panel: Top ----------
		JPanel panel_top = new JPanel();
		panel_top.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_top.setBounds(10, 10, 780, 70);
		contentPane.add(panel_top);
		panel_top.setLayout(null);
		
		JLabel lblHeader1 = new JLabel("STEP I : Browse EPC Diagram File");
		lblHeader1.setBounds(10, 11, 223, 13);
		panel_top.add(lblHeader1);
		lblHeader1.setFont(new Font("Tahoma", Font.PLAIN, 10));
		
		JLabel lblEpcDiagram = new JLabel("EPC Diagram");
		lblEpcDiagram.setBounds(20, 35, 61, 14);
		panel_top.add(lblEpcDiagram);
		lblEpcDiagram.setFont(new Font("Tahoma", Font.PLAIN, 11));
		
		btnBrowse = new JButton("Browse...");
		btnBrowse.setBounds(620, 30, 140, 23);
		panel_top.add(btnBrowse);
		btnBrowse.addActionListener(this);
		
		textField = new JTextField();
		textField.setBounds(120, 32, 490, 20);
		panel_top.add(textField);
		textField.setColumns(10);
		
		// ---------- Panel: Left ----------
		JPanel panel_left = new JPanel();
		panel_left.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_left.setBounds(10, 90, 385, 390);
		contentPane.add(panel_left);
		panel_left.setLayout(null);
		
		JLabel lblHeader2 = new JLabel("STEP II : Result of extracting EPC Element");
		lblHeader2.setBounds(10, 11, 230, 14);
		panel_left.add(lblHeader2);
		lblHeader2.setFont(new Font("Tahoma", Font.PLAIN, 10));
		
		JLabel lblNewLabel_1 = new JLabel("File name:");
		lblNewLabel_1.setBounds(20, 312, 80, 14);
		panel_left.add(lblNewLabel_1);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 11));
		
		JLabel lblNewLabel_2 = new JLabel("File size:");
		lblNewLabel_2.setBounds(20, 336, 46, 14);
		panel_left.add(lblNewLabel_2);
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 11));
		
		JLabel lblNewLabel_3 = new JLabel("Status:");
		lblNewLabel_3.setBounds(20, 360, 46, 14);
		panel_left.add(lblNewLabel_3);
		lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 11));
		
		lblResult1 = new JLabel();
		lblResult1.setBounds(120, 312, 200, 14);
		panel_left.add(lblResult1);
		
		lblResult2 = new JLabel();
		lblResult2.setBounds(120, 336, 200, 14);
		panel_left.add(lblResult2);
		
		lblResult3 = new JLabel();
		lblResult3.setBounds(120, 360, 200, 14);
		panel_left.add(lblResult3);
		
		// #Tree
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("empty");
		treeResult = new JTree(rootNode);
		treeResult.setFont(new Font("Tahoma", Font.PLAIN, 10));
		treeResult.setShowsRootHandles(true);
		treeResult.setBounds(20, 280, 360, 260);
		
		// #Scroll Pane Tree
		JScrollPane scrollTree = new JScrollPane();
		scrollTree.setBounds(10, 35, 365, 266);
		scrollTree.setViewportView(treeResult);
		panel_left.add(scrollTree);
		
		// ---------- Panel: Right ----------
		JPanel panel_right = new JPanel();
		panel_right.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_right.setBounds(405, 90, 385, 310);
		contentPane.add(panel_right);
		panel_right.setLayout(null);
		
		JLabel lblStepIii = new JLabel("STEP III : Verify initial marking");
		lblStepIii.setBounds(10, 11, 230, 14);
		panel_right.add(lblStepIii);
		lblStepIii.setFont(new Font("Tahoma", Font.PLAIN, 10));
		
		btnVerified = new JButton("Verified");
		btnVerified.setBounds(225, 276, 140, 23);
		panel_right.add(btnVerified);
		
		// #Table
		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.addColumn("ID");
		tableModel.addColumn("Name");
		tableModel.addColumn("Type");
		tableModel.addColumn("Colset");
		tableModel.addColumn("Marking");
		tableResult = new JTable(tableModel) {
	        private static final long serialVersionUID = 1L;
	        public boolean isCellEditable(int row, int column) {
	        	if (column == 4) return true;
	        	return false;
	        };
	    };
		tableResult.setFillsViewportHeight(true);
		
		JScrollPane scrollTable = new JScrollPane();
		scrollTable.setBounds(10, 35, 365, 230);
		scrollTable.setViewportView(tableResult);
		panel_right.add(scrollTable);
		
		// ---------- Panel: Bottom ----------
		JPanel panel_bottom = new JPanel();
		panel_bottom.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_bottom.setBounds(405, 411, 385, 69);
		contentPane.add(panel_bottom);
		panel_bottom.setLayout(null);
		
		btnGenerateCPN = new JButton("Generate CPN");
		btnGenerateCPN.setBounds(75, 35, 140, 23);
		panel_bottom.add(btnGenerateCPN);
		btnGenerateCPN.addActionListener(this);
		
		JLabel lblStepIv = new JLabel("STEP IV : Export To CPN File");
		lblStepIv.setBounds(10, 11, 140, 13);
		lblStepIv.setFont(new Font("Tahoma", Font.PLAIN, 10));
		panel_bottom.add(lblStepIv);
		
		btnOpenCpnTools = new JButton("Open CPN Tools");
		btnOpenCpnTools.setBounds(225, 35, 140, 23);
		panel_bottom.add(btnOpenCpnTools);
		btnOpenCpnTools.addActionListener(this);
		btnVerified.addActionListener(this);
		
		// ---------- Clear Value ----------
		resetResultText();
	}
	
	private void resetResultText() {
		lblResult1.setText("");
		lblResult2.setText("");
		lblResult3.setText("");
		
		elementInfo = "No Data";

		btnVerified.setEnabled(false);
		btnGenerateCPN.setEnabled(false);
		btnOpenCpnTools.setEnabled(false);
		
		DefaultMutableTreeNode diagramNode = new DefaultMutableTreeNode("empty");
		DefaultTreeModel model = (DefaultTreeModel) treeResult.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        root.removeAllChildren();
        model.reload();
        model.setRoot(diagramNode);
        
        DefaultTableModel tableModel = (DefaultTableModel) tableResult.getModel();
        tableModel.setRowCount(0);
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
		
		// Handle "About EPC2CPN" menu item action.
		else if (e.getSource() == mntmAboutUs) {
			try {
				DialogAboutUs dialog = new DialogAboutUs();
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setVisible(true);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		// Handle "Reset" menu item action.
		else if (e.getSource() == mntmReset) {
			resetResultText();
			
			textField.setText("");
			selectedFile = null;
		}
		
		// Handle "Reset" menu item action.
		else if (e.getSource() == mntmSetting) {
			try {
				String configBrowsePath = properties.getProperty("DEFAULT_BROWSE_PATH");
				String configSavePath = properties.getProperty("DEFAULT_SAVE_PATH");
				
				DialogSetting dialog = new DialogSetting(configBrowsePath, configSavePath, properties);
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setVisible(true);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		// Handle "Browse..." button action.
		else if (e.getSource() == btnBrowse) {
			String configBrowsePath = properties.getProperty("DEFAULT_BROWSE_PATH");
			
			JFileChooser fileChooser = new JFileChooser(configBrowsePath);
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("XML File (*.xml)", "xml"));
			fileChooser.setAcceptAllFileFilterUsed(false);
			int result = fileChooser.showOpenDialog(frame);
			if (result == JFileChooser.APPROVE_OPTION) {
				selectedFile = fileChooser.getSelectedFile();
				textField.setText(selectedFile.getAbsolutePath().toString());
				
				// Start Transfrom
				resetResultText();
				
				if (selectedFile != null && selectedFile.isFile()) {
					lblResult1.setFont(new Font("Tahoma", Font.BOLD, 11));
					lblResult1.setText(selectedFile.getName().toString());
					
					double kilobytes = (selectedFile.length() / 1024);
					lblResult2.setFont(new Font("Tahoma", Font.BOLD, 11));
					lblResult2.setText(String.valueOf(kilobytes) + " KB");
					
					elementShapes = null;
					elementConnectors = null;
					mapShape = null;
					
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
						
						// Show in Tree Panel
						for (Diagram diagram : project.getDiagrams().getDiagram()) {
							if (Constant.EPC_DIAGRAM_TYPE.equals(diagram.getDiagramType())) {
								DefaultMutableTreeNode diagramNode = new DefaultMutableTreeNode("Diagram");
								diagramNode.add(new DefaultMutableTreeNode("id = \"" + diagram.getId()+ "\""));
								diagramNode.add(new DefaultMutableTreeNode("diagramType = \"" + diagram.getDiagramType()+ "\""));
								diagramNode.add(new DefaultMutableTreeNode("name = \"" + diagram.getName()+ "\""));
								
								for (Object object : diagram.getDiagramPropertiesOrHiddenDiagramElementsOrScenarios()) {
									if (Shapes.class.isInstance(object)) {
										DefaultMutableTreeNode shapesNode = new DefaultMutableTreeNode("Shapes");
										Shapes shapes = (Shapes) object;
										elementShapes = shapes;
										mapShape = new HashMap<String, Shape>();
										for (Shape shape : shapes.getShape()) {
											DefaultMutableTreeNode shapeNode = new DefaultMutableTreeNode("Shape");
											shapeNode.add(new DefaultMutableTreeNode("id = \"" + shape.getId()+ "\""));
											shapeNode.add(new DefaultMutableTreeNode("shapeType = \"" + shape.getShapeType()+ "\""));
											shapeNode.add(new DefaultMutableTreeNode("name = \"" + shape.getName()+ "\""));
											shapeNode.add(new DefaultMutableTreeNode("height = \"" + shape.getHeight()+ "\""));
											shapeNode.add(new DefaultMutableTreeNode("width = \"" + shape.getWidth()+ "\""));
											shapeNode.add(new DefaultMutableTreeNode("x = \"" + shape.getX()+ "\""));
											shapeNode.add(new DefaultMutableTreeNode("y = \"" + shape.getY()+ "\""));
											
											shapesNode.add(shapeNode);
											mapShape.put(shape.getId(), shape);
										}
										diagramNode.add(shapesNode);
									} else if (Connectors.class.isInstance(object)) {
										DefaultMutableTreeNode connectorsNode = new DefaultMutableTreeNode("Connectors");
										Connectors connectors = (Connectors) object;
										elementConnectors = connectors;
										for (Connector connector : connectors.getConnector()) {
											DefaultMutableTreeNode connectorNode = new DefaultMutableTreeNode("Connector");
											connectorNode.add(new DefaultMutableTreeNode("id = \"" + connector.getId()+ "\""));
											connectorNode.add(new DefaultMutableTreeNode("connectorStyle = \"" + connector.getConnectorStyle()+ "\""));
											connectorNode.add(new DefaultMutableTreeNode("shapeType = \"" + connector.getShapeType()+ "\""));
											connectorNode.add(new DefaultMutableTreeNode("name = \"" + connector.getName()+ "\""));
											connectorNode.add(new DefaultMutableTreeNode("from = \"" + connector.getFrom()+ "\""));
											connectorNode.add(new DefaultMutableTreeNode("to = \"" + connector.getTo()+ "\""));
											connectorNode.add(new DefaultMutableTreeNode("height = \"" + connector.getHeight()+ "\""));
											connectorNode.add(new DefaultMutableTreeNode("width = \"" + connector.getWidth()+ "\""));
											connectorNode.add(new DefaultMutableTreeNode("x = \"" + connector.getX()+ "\""));
											connectorNode.add(new DefaultMutableTreeNode("y = \"" + connector.getY()+ "\""));
											
											connectorsNode.add(connectorNode);
										}
										diagramNode.add(connectorsNode);
									}
								}
								
								DefaultTreeModel treeModel = (DefaultTreeModel) treeResult.getModel();
						        DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) treeModel.getRoot();
						        treeNode.removeAllChildren();
						        treeModel.reload();
						        treeModel.setRoot(diagramNode);
						        
							}
							break;
						}
						
						// Check each element
						List<EPCElement> startElement = new ArrayList<EPCElement>(); 
						if (elementShapes != null) {
							mapVariable = new HashMap<String, String>();
							mapEPCElement = new HashMap<String, EPCElement>();
							epcElementList = new ArrayList<EPCElement>();
							
							DefaultTableModel tableModel = (DefaultTableModel) tableResult.getModel();
							tableModel.setRowCount(0);
							
							for (Shape shape : elementShapes.getShape()) {
								EPCElement element = new EPCElement();
								element.setShape(shape);
								
								List<String> fromShapeIdList = new ArrayList<String>();
								List<String> toShapeIdList = new ArrayList<String>();
								boolean isStartNode = false;
								
								int countFrom = 0;
								int countTo = 0;
								for (Connector connector : elementConnectors.getConnector()) {
									if (shape.getId().equalsIgnoreCase(connector.getFrom())) {
										countFrom++;
										
										Shape toShape = mapShape.get(connector.getTo());
										if (toShape != null) {
											toShapeIdList.add(toShape.getId());
										}
									}
									if (shape.getId().equalsIgnoreCase(connector.getTo())) {
										countTo++;
										
										Shape fromShape = mapShape.get(connector.getFrom());
										if (fromShape != null) {
											fromShapeIdList.add(fromShape.getId());
										}
									}
									
									// Check event 
									if (Constant.EPC_SHAPE_TYPE_EVENT.equals(shape.getShapeType())) {
										if (shape.getId().equalsIgnoreCase(connector.getTo())) {
											Shape fromShape = mapShape.get(connector.getFrom());
											if (Constant.EPC_SHAPE_TYPE_EVENT.equals(fromShape.getShapeType())
													|| Constant.EPC_SHAPE_TYPE_AND.equals(fromShape.getShapeType())
													|| Constant.EPC_SHAPE_TYPE_XOR.equals(fromShape.getShapeType())
													|| Constant.EPC_SHAPE_TYPE_OR.equals(fromShape.getShapeType())) {
												element.setReqDummyTrans(true);
											}
										}
									}
								}
								
								if (Constant.EPC_SHAPE_TYPE_EVENT.equals(shape.getShapeType())) {	
									if (countFrom == 1 && countTo == 0) {
										// Start Event
										isStartNode = true;
									}
									element.setStartNode(isStartNode);
									
									// Show initial marking value in table
									String initMark = isStartNode?"1":"";
									String[] temp = {shape.getId(), shape.getName(), shape.getShapeType(), Constant.CPN_COLSET_UNIT, initMark};
									tableModel.addRow(temp);
									
								} else if (Constant.EPC_SHAPE_TYPE_AND.equals(shape.getShapeType())
										|| Constant.EPC_SHAPE_TYPE_XOR.equals(shape.getShapeType())
										|| Constant.EPC_SHAPE_TYPE_OR.equals(shape.getShapeType())) {
									if (countFrom == 2 && countTo == 1) {
										// Split
										element.setOperatorType("Split");
									} else if (countFrom == 1 && countTo == 2) {
										// Join
										element.setOperatorType("Join");
									}
								
								} else if (Constant.EPC_SHAPE_TYPE_PROCESSPATH.equals(shape.getShapeType())
										|| Constant.EPC_SHAPE_TYPE_INFORMATIONRESOURCE.equals(shape.getShapeType())
										|| Constant.EPC_SHAPE_TYPE_ROLE.equals(shape.getShapeType())
										|| Constant.EPC_SHAPE_TYPE_SYSTEM.equals(shape.getShapeType())
										|| Constant.EPC_SHAPE_TYPE_ORGANIZATIONUNIT.equals(shape.getShapeType())) {
									if (countFrom == 1 && countTo == 0) {
										// Start Node
										isStartNode = true;
									}
									
									String colset = Constant.CPN_COLSET_UNIT;
									String initMark = isStartNode?"1":"";
									
									try{
										String[] shapeName = null;
										
										if (Constant.EPC_SHAPE_TYPE_ORGANIZATIONUNIT.equals(shape.getShapeType())){
											shapeName = new String[3];
											shapeName[0] = Constant.CPN_COLSET_STRING;
											shapeName[1] = "orgUnit";
											shapeName[2] = shape.getName();
											
											colset = shapeName[0];
											initMark = shapeName[2];
											
										} else if (Constant.EPC_SHAPE_TYPE_ROLE.equals(shape.getShapeType())) {
											shapeName = new String[3];
											shapeName[0] = Constant.CPN_COLSET_STRING;
											shapeName[1] = "role";
											shapeName[2] = shape.getName();
										
											colset = shapeName[0];
											initMark = shapeName[2];
											
										} else if (Constant.EPC_SHAPE_TYPE_SYSTEM.equals(shape.getShapeType())) {
											shapeName = new String[3];
											shapeName[0] = Constant.CPN_COLSET_STRING;
											shapeName[1] = "system";
											shapeName[2] = shape.getName();
											
											colset = shapeName[0];
											initMark = shapeName[2];
											
										} else if (Constant.EPC_SHAPE_TYPE_INFORMATIONRESOURCE.equals(shape.getShapeType())) {
											shapeName = shape.getName().split("\\|");
											if (shapeName!=null && shapeName.length == 3) {
												colset = shapeName[0];
												initMark = shapeName[2];
											} else {
												shapeName = new String[3];
												shapeName[0] = Constant.CPN_COLSET_STRING;
												shapeName[1] = "infoResource";
												shapeName[2] = shape.getName();
												
												colset = shapeName[0];
												initMark = shapeName[2];
											}
										}
										
										if (shapeName!=null && shapeName.length == 3) {
											if (!mapVariable.containsKey(shapeName[1])) {
												mapVariable.put(shapeName[1], shapeName[0]);
											}
										}
									} catch (Exception ex) {
										colset = Constant.CPN_COLSET_UNIT;
										initMark = isStartNode?"1":"";
									}
									
									// Show initial marking value in table
									String[] temp = {shape.getId(), shape.getName(), shape.getShapeType(), colset, initMark};
									tableModel.addRow(temp);
								}
								
								element.setFromShapeId(fromShapeIdList);
								element.setToShapeId(toShapeIdList);
								epcElementList.add(element);
								
								if (isStartNode) {
									startElement.add(element);
								}
								
								mapEPCElement.put(element.getShape().getId(), element);
							}
						}
						
						// Success
						if (isSuccess) {
							lblResult3.setFont(new Font("Tahoma", Font.BOLD, 11));
							lblResult3.setForeground(new Color(0, 204, 0));
							lblResult3.setText(transfromStatus);
							
							btnVerified.setEnabled(true);
						}
					} catch (Exception e1) {
						e1.printStackTrace();
						
						// Error
						lblResult3.setFont(new Font("Tahoma", Font.BOLD, 11));
						lblResult3.setForeground(new Color(255, 0, 0));
						lblResult3.setText("Invalid xml file format");
					}
				}
			}
		}
		
		// Handle "Open CPN Tools" button action.
		else if (e.getSource() == btnOpenCpnTools) {
			try {
				Desktop.getDesktop().open(targetFile);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		// Handle "Generate CPN" button action.
		else if (e.getSource() == btnGenerateCPN) {
			String configSavePath = properties.getProperty("DEFAULT_SAVE_PATH");
			
			JFileChooser fileChooser = new JFileChooser(configSavePath);
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("CPN File (*.cpn)", "cpn"));
			fileChooser.setAcceptAllFileFilterUsed(false);
			int result = fileChooser.showSaveDialog(frame);
			if (result == JFileChooser.APPROVE_OPTION) {
				targetFile = fileChooser.getSelectedFile();
		        try {
		        	if (!targetFile.exists()) {
		        		targetFile = new File(targetFile.getAbsoluteFile() + ".cpn");
		            }
		        	FileWriter fw = new FileWriter(targetFile);
		            fw.write(elementInfo);
		            fw.close();
		            
		            JOptionPane.showMessageDialog(frame, "Generate CPN File Success!");
		            
					btnOpenCpnTools.setEnabled(true);
		        } catch (IOException e1) {
		            e1.printStackTrace();
		            JOptionPane.showMessageDialog(frame, "Generate CPN File Fail!", "Error", JOptionPane.ERROR_MESSAGE);
		        }
			}
		}
		
		// Handle "View EPC Element" button action.
		else if (e.getSource() == btnVerified) {
			DefaultTableModel tableModel = (DefaultTableModel) tableResult.getModel();
			for (int i=0; i<tableModel.getRowCount(); i++) {
				try {
					String shapeId = String.valueOf(tableModel.getValueAt(i, 0));
					String colset = String.valueOf(tableModel.getValueAt(i, 3));
					String markValue = String.valueOf(tableModel.getValueAt(i, 4));
					
					if (markValue!=null && markValue.trim().length()>0) {
						// Check marking
						if (Constant.CPN_COLSET_UNIT.equals(colset) || Constant.CPN_COLSET_INT.equals(colset)) {
							Integer.parseInt(markValue);
						} else if (Constant.CPN_COLSET_BOOL.equals(colset)) {
							Boolean.parseBoolean(markValue);
						}
						
						mapEPCElement.get(shapeId).setInitMark(markValue);
					}
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(frame, "Initial marking values failed!", "Failed", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			
			
			// Find initial X, Y
			int initX = 0;
			int initY = 0;
			if (epcElementList!=null) {
				for (EPCElement element : epcElementList) {
					if (element.isStartNode()) {
						initX = element.getShape().getX().intValue();
						initY = element.getShape().getY().intValue();
						break;
					}
				}
			}
			
			// Transform
			String cpnTemplate = "";
			String transformVariable = "";
			String transformItem = "";
			try {
				cpnTemplate = FileUtils.readFileToString(new File(properties.getProperty("CPN_TEMPLATE_FILE")), Charset.defaultCharset());
			} catch (IOException io) {
				io.printStackTrace();
			}
			
			if (epcElementList!=null) {
				int countEvent = 0;
				int countFunction = 0;
				int countAND = 0;
				int countXOR = 0;
				int countOR = 0;
				int countArtifact = 0;
				
				// Transform Variable
				for (Map.Entry<String, String> me : mapVariable.entrySet()) {
					Variable var = TranformUtils.generateVariableObject(me.getValue(), me.getKey());
					transformVariable += TranformUtils.performCPNVariableXMLString(var, properties);
				}
				
				// Transform for each EPC Element
				for (EPCElement element : epcElementList) {
					Shape i = element.getShape();
					int xPoint = i.getX().intValue() - initX;
					int yPoint = initY - i.getY().intValue();
					
					List<String> incomeCPNId = new ArrayList<String>();
					List<String> outcomeCPNId = new ArrayList<String>();
					
					CPNObject cpnObject = null;
					if (Constant.EPC_SHAPE_TYPE_EVENT.equals(i.getShapeType())) {
						countEvent++;
						String name = i.getName() + "_E_" + countEvent;
						String marking = element.getInitMark();
						
						if (element.isReqDummyTrans()) {
							cpnObject = TranformUtils.generateEventDummyFunctionCPNObject(xPoint, yPoint, name, marking);
							incomeCPNId.add(cpnObject.getTransList().get(0).getId());
						} else {
							cpnObject = TranformUtils.generateEventCPNObject(xPoint, yPoint, name, marking, Constant.CPN_COLSET_UNIT);
							incomeCPNId.add(cpnObject.getPlaceList().get(0).getId());
						}
						outcomeCPNId.add(cpnObject.getPlaceList().get(0).getId());
					} else if (Constant.EPC_SHAPE_TYPE_FUNCTION.equals(i.getShapeType())) {
						countFunction++;
						String name = i.getName() + "_F_" + countFunction;
						
						cpnObject = TranformUtils.generateFunctionCPNObject(xPoint, yPoint, name);
						incomeCPNId.add(cpnObject.getTransList().get(0).getId());
						outcomeCPNId.add(cpnObject.getTransList().get(0).getId());
					} else if (Constant.EPC_SHAPE_TYPE_AND.equals(i.getShapeType())) {
						countAND++;
						String name = "AND_" + countAND;
						
						if ("Split".equals(element.getOperatorType())) {
							cpnObject = TranformUtils.generateANDSplitCPNObject(xPoint, yPoint, name);
						
							incomeCPNId.add(cpnObject.getPlaceList().get(0).getId());
							outcomeCPNId.add(cpnObject.getPlaceList().get(1).getId());
							outcomeCPNId.add(cpnObject.getPlaceList().get(2).getId());
						} else if ("Join".equals(element.getOperatorType())) {
							cpnObject = TranformUtils.generateANDJoinCPNObject(xPoint, yPoint, name);

							incomeCPNId.add(cpnObject.getPlaceList().get(0).getId());
							incomeCPNId.add(cpnObject.getPlaceList().get(1).getId());
							outcomeCPNId.add(cpnObject.getPlaceList().get(2).getId());
						}
					} else if (Constant.EPC_SHAPE_TYPE_XOR.equals(i.getShapeType())) {
						countXOR++;
						String name = "XOR_" + countXOR;
						
						if ("Split".equals(element.getOperatorType())) {
							String colset = Constant.CPN_COLSET_UNIT;
							Shape fromShape = mapShape.get(element.getFromShapeId().get(0));
							Shape toShape1 = mapShape.get(element.getToShapeId().get(0));
							Shape toShape2 = mapShape.get(element.getToShapeId().get(1));
							String condition0 = "";
							String condition1 = "";
							String condition2 = "";
							if (elementConnectors != null && elementConnectors.getConnector() != null) {
								for (Connector connector : elementConnectors.getConnector()) {
									String conName = connector.getName();
									if (conName!=null && !conName.isEmpty()) {
										if (connector.getFrom().equals(fromShape.getId()) && connector.getTo().equals(i.getId())) {
											colset = mapVariable.get(conName);
											condition0 = conName;
										} else if (connector.getFrom().equals(i.getId()) && connector.getTo().equals(toShape1.getId())) {
											if (toShape1.getX().intValue() <= i.getX().intValue()) {
												condition1 = TranformUtils.performCPNMLConditionString(conName);
											} else {
												condition2 = TranformUtils.performCPNMLConditionString(conName);
											}
										} else if (connector.getFrom().equals(i.getId()) && connector.getTo().equals(toShape2.getId())) {
											if (toShape2.getX().intValue() <= i.getX().intValue()) {
												condition1 = TranformUtils.performCPNMLConditionString(conName);
											} else {
												condition2 = TranformUtils.performCPNMLConditionString(conName);
											}
										}
									}
								}
							}
							if (condition0.isEmpty() && condition1.isEmpty() && condition2.isEmpty()) {
								cpnObject = TranformUtils.generateXORSplitCPNObject(xPoint, yPoint, name);
							} else {
								cpnObject = TranformUtils.generateXORSplitWithConditionCPNObject(xPoint, yPoint, name, colset, condition0, condition1, condition2);	
							}
							
							incomeCPNId.add(cpnObject.getPlaceList().get(0).getId());
							outcomeCPNId.add(cpnObject.getPlaceList().get(1).getId());
							outcomeCPNId.add(cpnObject.getPlaceList().get(2).getId());
						} else if ("Join".equals(element.getOperatorType())) {
							cpnObject = TranformUtils.generateXORJoinCPNObject(xPoint, yPoint, name);

							incomeCPNId.add(cpnObject.getPlaceList().get(0).getId());
							incomeCPNId.add(cpnObject.getPlaceList().get(1).getId());
							outcomeCPNId.add(cpnObject.getPlaceList().get(2).getId());
						}
					} else if (Constant.EPC_SHAPE_TYPE_OR.equals(i.getShapeType())) {
						countOR++;
						String name = "OR_" + countOR;
						
						if ("Split".equals(element.getOperatorType())) {
							String colset = Constant.CPN_COLSET_UNIT;
							Shape fromShape = mapShape.get(element.getFromShapeId().get(0));
							Shape toShape1 = mapShape.get(element.getToShapeId().get(0));
							Shape toShape2 = mapShape.get(element.getToShapeId().get(1));
							String condition0 = "";
							String condition1 = "";
							String condition2 = "";
							if (elementConnectors != null && elementConnectors.getConnector() != null) {
								for (Connector connector : elementConnectors.getConnector()) {
									String conName = connector.getName();
									if (conName!=null && !conName.isEmpty()) {
										if (connector.getFrom().equals(fromShape.getId()) && connector.getTo().equals(i.getId())) {
											colset = mapVariable.get(conName);
											condition0 = conName;
										} else if (connector.getFrom().equals(i.getId()) && connector.getTo().equals(toShape1.getId())) {
											if (toShape1.getX().intValue() <= i.getX().intValue()) {
												condition1 = TranformUtils.performCPNMLConditionString(conName);
											} else {
												condition2 = TranformUtils.performCPNMLConditionString(conName);
											}
										} else if (connector.getFrom().equals(i.getId()) && connector.getTo().equals(toShape2.getId())) {
											if (toShape2.getX().intValue() <= i.getX().intValue()) {
												condition1 = TranformUtils.performCPNMLConditionString(conName);
											} else {
												condition2 = TranformUtils.performCPNMLConditionString(conName);
											}
										}
									}
								}
							}
							if (condition0.isEmpty() && condition1.isEmpty() && condition2.isEmpty()) {
								cpnObject = TranformUtils.generateORSplitCPNObject(xPoint, yPoint, name);
							} else {
								cpnObject = TranformUtils.generateXORSplitWithConditionCPNObject(xPoint, yPoint, name, colset, condition0, condition1, condition2);	
							}
							
							incomeCPNId.add(cpnObject.getPlaceList().get(0).getId());
							outcomeCPNId.add(cpnObject.getPlaceList().get(1).getId());
							outcomeCPNId.add(cpnObject.getPlaceList().get(2).getId());
						} else if ("Join".equals(element.getOperatorType())) {
							cpnObject = TranformUtils.generateORJoinCPNObject(xPoint, yPoint, name);

							incomeCPNId.add(cpnObject.getPlaceList().get(0).getId());
							incomeCPNId.add(cpnObject.getPlaceList().get(1).getId());
							outcomeCPNId.add(cpnObject.getPlaceList().get(2).getId());
						}
					} else if (Constant.EPC_SHAPE_TYPE_PROCESSPATH.equals(i.getShapeType())) {
						countEvent++;
						String name = i.getName() + "_PP_" + countEvent;
						String marking = element.getInitMark();
						
						cpnObject = TranformUtils.generateEventDummyFunctionCPNObject(xPoint, yPoint, name, marking);
						incomeCPNId.add(cpnObject.getTransList().get(0).getId());
						
						outcomeCPNId.add(cpnObject.getPlaceList().get(0).getId());
					} else if (Constant.EPC_SHAPE_TYPE_ROLE.equals(i.getShapeType())
							|| Constant.EPC_SHAPE_TYPE_SYSTEM.equals(i.getShapeType())
							|| Constant.EPC_SHAPE_TYPE_ORGANIZATIONUNIT.equals(i.getShapeType())) {
						
						String colset = Constant.CPN_COLSET_STRING;
						String marking = "1`\""+element.getInitMark()+"\"";
						
						countArtifact++;
						String name = i.getName() + "_A_" + countArtifact;
						cpnObject = TranformUtils.generateEventCPNObject(xPoint, yPoint, name, marking, colset);
						incomeCPNId.add(cpnObject.getPlaceList().get(0).getId());
						outcomeCPNId.add(cpnObject.getPlaceList().get(0).getId());
					} else if (Constant.EPC_SHAPE_TYPE_INFORMATIONRESOURCE.equals(i.getShapeType())) {
						
						String colset = Constant.CPN_COLSET_STRING;
						String marking = element.getInitMark();
						boolean isCustomVariable = false;
						try{
							String[] shapeName = i.getName().split("\\|");
							if (shapeName!=null && shapeName.length == 3) {
								colset = shapeName[0];
							}
							
							if (Constant.CPN_COLSET_INT.equals(colset) || Constant.CPN_COLSET_UNIT.equals(colset)) {
								marking = "1`"+marking;
							} else if (Constant.CPN_COLSET_STRING.equals(colset)) {
								marking = "1`\""+marking+"\"";
							}
						} catch (Exception ex) {

						}
						
						countArtifact++;
						String name = isCustomVariable?i.getName() + "_A_" + countArtifact:"A_" + countArtifact;
						cpnObject = TranformUtils.generateEventCPNObject(xPoint, yPoint, name, marking, colset);
						incomeCPNId.add(cpnObject.getPlaceList().get(0).getId());
						outcomeCPNId.add(cpnObject.getPlaceList().get(0).getId());
					}
					element.setIncomeCPNId(incomeCPNId);
					element.setOutcomeCPNId(outcomeCPNId);
					element.setCpnObject(cpnObject);
					
					transformItem += TranformUtils.performCPNElemetXMLString(cpnObject, properties);
				}
				
				// Create arc for connect between each element
				CPNObject tempObject = new CPNObject();
				List<Arc> arcList = new ArrayList<Arc>();
				if (elementConnectors != null && elementConnectors.getConnector() != null) {
					for (Connector connector : elementConnectors.getConnector()) {
						EPCElement fromElement = mapEPCElement.get(connector.getFrom());
						EPCElement toElement = mapEPCElement.get(connector.getTo());
						

						String marking = "1`()";
						String conName = connector.getName();
						if (conName!=null && conName.trim().length() > 0) {
							marking = conName;
						}
						
						int xPoint1 = fromElement.getShape().getX().intValue() - initX;
						int yPoint1 = initY - fromElement.getShape().getY().intValue();
						int xPoint2 = toElement.getShape().getX().intValue() - initX;
						int yPoint2 = initY - toElement.getShape().getY().intValue();
						int xAnno = (xPoint1+xPoint2)/2;
						int yAnno = (yPoint1+yPoint2)/2;
						
						String arcType = "";
						String transId = "";
						String placeId = "";
						if (Constant.EPC_SHAPE_TYPE_FUNCTION.equals(fromElement.getShape().getShapeType())) {
							arcType = Constant.CPN_ARC_TYPE_TTOP;
							
							if (Constant.EPC_SHAPE_TYPE_AND.equals(toElement.getShape().getShapeType())
									|| Constant.EPC_SHAPE_TYPE_XOR.equals(toElement.getShape().getShapeType())
									|| Constant.EPC_SHAPE_TYPE_OR.equals(toElement.getShape().getShapeType())) {
								if ("Split".equals(toElement.getOperatorType())) {
									placeId = toElement.getIncomeCPNId().get(0);
								} else if ("Join".equals(toElement.getOperatorType())) {
									if (fromElement.getShape().getX().intValue() <= toElement.getShape().getX().intValue()) {
										placeId = toElement.getIncomeCPNId().get(0);
									} else if (fromElement.getShape().getX().intValue() > toElement.getShape().getX().intValue()) {
										placeId = toElement.getIncomeCPNId().get(1);
									}
								}
							} else {
								placeId = toElement.getIncomeCPNId().get(0);
							}
							transId = fromElement.getOutcomeCPNId().get(0);
							
							arcList.add(TranformUtils.generateArcObject(xAnno, yAnno, arcType, transId, placeId, marking));
						} else if (Constant.EPC_SHAPE_TYPE_EVENT.equals(fromElement.getShape().getShapeType())) {
							arcType = Constant.CPN_ARC_TYPE_PTOT;
							
							placeId = fromElement.getOutcomeCPNId().get(0);
							transId = toElement.getIncomeCPNId().get(0);
							arcList.add(TranformUtils.generateArcObject(xAnno, yAnno, arcType, transId, placeId, "1`()"));
						} else if (Constant.EPC_SHAPE_TYPE_AND.equals(fromElement.getShape().getShapeType())
								|| Constant.EPC_SHAPE_TYPE_XOR.equals(fromElement.getShape().getShapeType())
								|| Constant.EPC_SHAPE_TYPE_OR.equals(fromElement.getShape().getShapeType())) {
							arcType = Constant.CPN_ARC_TYPE_PTOT;
							
							marking = "1`()";
							
							if ("Split".equals(fromElement.getOperatorType())) {
								if (fromElement.getShape().getX().intValue() > toElement.getShape().getX().intValue()) {
									placeId = fromElement.getOutcomeCPNId().get(0);
								} else if (fromElement.getShape().getX().intValue() <= toElement.getShape().getX().intValue()) {
									placeId = fromElement.getOutcomeCPNId().get(1);
								}
								transId = toElement.getIncomeCPNId().get(0);
							} else if ("Join".equals(fromElement.getOperatorType())) {
								placeId = fromElement.getOutcomeCPNId().get(0);
								transId = toElement.getIncomeCPNId().get(0);
							}
							arcList.add(TranformUtils.generateArcObject(xAnno, yAnno, arcType, transId, placeId, marking));
						} else if (Constant.EPC_SHAPE_TYPE_ROLE.equals(fromElement.getShape().getShapeType())
								|| Constant.EPC_SHAPE_TYPE_SYSTEM.equals(fromElement.getShape().getShapeType())
								|| Constant.EPC_SHAPE_TYPE_ORGANIZATIONUNIT.equals(fromElement.getShape().getShapeType())) {
							arcType = Constant.CPN_ARC_TYPE_PTOT;
							
							if (conName==null || conName.trim().length() == 0) {
								marking = "1`\"" + fromElement.getShape().getName() + "\"";
							}
							
							placeId = fromElement.getOutcomeCPNId().get(0);
							transId = toElement.getIncomeCPNId().get(0);
							arcList.add(TranformUtils.generateArcObject(xAnno, yAnno, arcType, transId, placeId, marking));
						} else if (Constant.EPC_SHAPE_TYPE_PROCESSPATH.equals(fromElement.getShape().getShapeType())
								|| Constant.EPC_SHAPE_TYPE_INFORMATIONRESOURCE.equals(fromElement.getShape().getShapeType())) {
							arcType = Constant.CPN_ARC_TYPE_PTOT;
							
							if (conName==null || conName.trim().length() == 0) {
								marking = "1`\"" + fromElement.getShape().getName() + "\"";
							}
							
							placeId = fromElement.getOutcomeCPNId().get(0);
							transId = toElement.getIncomeCPNId().get(0);
							arcList.add(TranformUtils.generateArcObject(xAnno, yAnno, arcType, transId, placeId, marking));
						}
					}
					tempObject.setArcList(arcList);
					transformItem += TranformUtils.performCPNElemetXMLString(tempObject, properties);
				}
				
				elementInfo = cpnTemplate.replaceAll("#TRANSFORM_ITEM", transformItem)
						.replaceAll("#TRANSFORM_VARIABLE", transformVariable);
			}
			
			JOptionPane.showMessageDialog(frame, "Confirm marking.", "EPC Element", JOptionPane.INFORMATION_MESSAGE);
			btnGenerateCPN.setEnabled(true);
		}
	}
}
