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
import main.model.CPNObject;
import main.model.EPCElement;
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
	
	private Map<String, Shape> mapShape = null;
	
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
		tableModel.addColumn("COLSET");
		tableModel.addColumn("Values");
		tableResult = new JTable(tableModel);
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
		
		// Handle "Reset" menu item action.
		else if (e.getSource() == mntmReset) {
			resetResultText();
			
			textField.setText("");
			selectedFile = null;
		}
		
		// Handle "Reset" menu item action.
		else if (e.getSource() == mntmSetting) {
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
						
						for (Diagram diagram : project.getDiagrams().getDiagram()) {
							if (Constant.EPC_DIAGRAM_TYPE.equals(diagram.getDiagramType())) {
								DefaultMutableTreeNode diagramNode = new DefaultMutableTreeNode("Diagram");
								diagramNode.add(new DefaultMutableTreeNode("id = \"" + diagram.getId()+ "\""));
								diagramNode.add(new DefaultMutableTreeNode("diagramType = \"" + diagram.getDiagramType()+ "\""));
								diagramNode.add(new DefaultMutableTreeNode("name = \"" + diagram.getName()+ "\""));
								
								DefaultTableModel tableModel = (DefaultTableModel) tableResult.getModel();
								tableModel.setRowCount(0);
								
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
											
											if (Constant.EPC_SHAPE_TYPE_EVENT.equals(shape.getShapeType())) {
												String[] temp = {shape.getId(), shape.getName(), "UNIT", ""};
												tableModel.addRow(temp);
											}
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
						
						if (elementShapes != null) {
							epcElementList = new ArrayList<EPCElement>();
							
							for (Shape shape : elementShapes.getShape()) {
								EPCElement element = new EPCElement();
								element.setShape(shape);
								
								int countFrom = 0;
								int countTo = 0;
								for (Connector connector : elementConnectors.getConnector()) {
									if (shape.getId().equalsIgnoreCase(connector.getFrom())) {
										countFrom++;
									}
									if (shape.getId().equalsIgnoreCase(connector.getTo())) {
										countTo++;
									}
								}
								
								if (Constant.EPC_SHAPE_TYPE_EVENT.equals(shape.getShapeType())) {	
									if (countFrom == 0 && countTo > 0) {
										// Start Event
										element.setStartNode(true);
									}
									
								} else if (Constant.EPC_SHAPE_TYPE_FUNCTION.equals(shape.getShapeType())) {
									
								} else if (Constant.EPC_SHAPE_TYPE_AND.equals(shape.getShapeType())
										|| Constant.EPC_SHAPE_TYPE_XOR.equals(shape.getShapeType())
										|| Constant.EPC_SHAPE_TYPE_OR.equals(shape.getShapeType())) {
									if (countFrom == 1 && countTo == 2) {
										// Split
										element.setOperatorType("Split");
									} else if (countFrom == 2 && countTo == 1) {
										// Join
										element.setOperatorType("Join");
									}
									
								}
								
								epcElementList.add(element);
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
			JFileChooser fileChooser = new JFileChooser();
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
			// Find start event
			Shape temp = new Shape();
			int initX = 0;
			int initY = 0;
			List<EPCElement> extendedShapeList = new ArrayList<EPCElement>();
			if (elementConnectors!=null) {
				for (Connector i : elementConnectors.getConnector()) {
					Shape tempFrom = mapShape.get(i.getFrom());
					boolean isStartNode = true;
					if (Constant.EPC_SHAPE_TYPE_EVENT.equals(tempFrom.getShapeType())) {
						for (Connector j : elementConnectors.getConnector()) {
							if (i.getFrom().equalsIgnoreCase(j.getTo())) {
								isStartNode = false;
								break;
							}
						}
					} else {
						isStartNode = false;
					}
					EPCElement eShape = new EPCElement();
					eShape.setShape(temp);
					eShape.setStartNode(isStartNode);
					extendedShapeList.add(eShape);
					
					System.out.println("Node Id = "+i.getId()+", isStartNode = "+isStartNode);
					if (isStartNode) {
						temp = tempFrom;
						initX = temp.getX().intValue();
						initY = temp.getY().intValue();
					}
				}
			}
			
			// TEST Transform
			String cpnTemplate = "";
			String placeItem = "";
			String transItem = "";
			String arcItem = "";
			try {
				cpnTemplate = FileUtils.readFileToString(new File(properties.getProperty("CPN_TEMPLATE_FILE")), Charset.defaultCharset());
			} catch (IOException io) {
				io.printStackTrace();
			}
			
			if (elementShapes!=null) {
				for (Shape i : elementShapes.getShape()) {
					if (Constant.EPC_SHAPE_TYPE_EVENT.equals(i.getShapeType())) {
						int xPoint = initX - i.getX().intValue();
						int yPoint = initY - i.getY().intValue();
						
						CPNObject cpnObject = TranformUtils.generateEventCPNObject(xPoint, yPoint, i.getName());
						placeItem += TranformUtils.performCPNElemetXMLString(cpnObject, properties);
						
					} else if (Constant.EPC_SHAPE_TYPE_FUNCTION.equals(i.getShapeType())) {
						int xPoint = initX - i.getX().intValue();
						int yPoint = initY - i.getY().intValue();
						
						CPNObject cpnObject = TranformUtils.generateFunctionCPNObject(xPoint, yPoint, i.getName());
						transItem += TranformUtils.performCPNElemetXMLString(cpnObject, properties);
						
					} else if (Constant.EPC_SHAPE_TYPE_AND.equals(i.getShapeType())) {
						
					} else if (Constant.EPC_SHAPE_TYPE_XOR.equals(i.getShapeType())) {
						
					} else if (Constant.EPC_SHAPE_TYPE_OR.equals(i.getShapeType())) {
						
					} else if (Constant.EPC_SHAPE_TYPE_PROCESSPATH.equals(i.getShapeType())
							|| Constant.EPC_SHAPE_TYPE_INFORMATIONRESOURCE.equals(i.getShapeType())
							|| Constant.EPC_SHAPE_TYPE_ROLE.equals(i.getShapeType())
							|| Constant.EPC_SHAPE_TYPE_SYSTEM.equals(i.getShapeType())
							|| Constant.EPC_SHAPE_TYPE_ORGANIZATIONUNIT.equals(i.getShapeType())) {
						
					}
				}
//				for (Connector j : elementConnectors.getConnector()) {
//					if (Constant.EPC_SHAPE_TYPE_CONTROLFLOW.equals(j.getShapeType())) {
//						
//						Shape shapeFrom = mapShape.get(j.getFrom());
//						Shape shapeTo = mapShape.get(j.getTo());
//						
//						if (shapeFrom!=null && shapeTo!=null) {
//							String arcType = null;
//							String transId = "";
//							String plactId = "";
//							if (Constant.EPC_SHAPE_TYPE_EVENT.equals(shapeFrom.getShapeType()) && Constant.EPC_SHAPE_TYPE_FUNCTION.equals(shapeTo.getShapeType())) {
//								arcType = "PtoT";
//								plactId = shapeFrom.getId();
//								transId = shapeTo.getId();
//							} else if (Constant.EPC_SHAPE_TYPE_FUNCTION.equals(shapeFrom.getShapeType()) && Constant.EPC_SHAPE_TYPE_EVENT.equals(shapeTo.getShapeType())) {
//								arcType = "TtoP";
//								plactId = shapeTo.getId();
//								transId = shapeFrom.getId();
//							}
//							
//							if (arcType != null) {
//								arcItem += arcTemplate;
//								arcItem = arcItem.replaceAll("#ARC_ID", j.getId())
//										.replaceAll("#ARC_TYPE", arcType)
//										.replaceAll("#TRANS_ID", transId)
//										.replaceAll("#PLACE_ID", plactId)
//										.replaceAll("#ARC_ANNO_X", "0.000000")
//										.replaceAll("#ARC_ANNO_Y", "0.000000")
//										.replaceAll("#ARC_ANNO_TEXT", "1`()");
//							}
//						}
//					}
//				}
				
//				CPNObject cpnObject = TranformUtils.generateORJoinCPNObject(0, 0, "TEST");
//				String a1 = TranformUtils.performCPNElemetXMLString(cpnObject, properties);
//				cpnObject = ConvertUtils.generateANDJoinObject(100, 100, "TEST");
//				String a2 = ConvertUtils.performCPNElemetXMLString(cpnObject, properties);
				elementInfo = cpnTemplate.replaceAll("#PLACE_ITEM", placeItem)
						.replaceAll("#TRANS_ITEM", transItem)
						.replaceAll("#ARC_ITEM", "");
			}
			
			JOptionPane.showMessageDialog(frame, elementInfo, "EPC Element", JOptionPane.INFORMATION_MESSAGE);
			
			btnGenerateCPN.setEnabled(true);
		}
	}
}
