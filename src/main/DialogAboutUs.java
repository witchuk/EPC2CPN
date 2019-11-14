package main;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import java.awt.Font;

public class DialogAboutUs extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JButton cancelButton;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			DialogAboutUs dialog = new DialogAboutUs();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public DialogAboutUs() {
		setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		setModal(true);
		setTitle("About EPC2CPN");
		setBounds(200, 200, 340, 260);
		BorderLayout borderLayout = new BorderLayout();
		getContentPane().setLayout(borderLayout);
		JPanel contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblDeveloper = new JLabel("Developer :");
			lblDeveloper.setFont(new Font("Tahoma", Font.PLAIN, 10));
			lblDeveloper.setBounds(37, 116, 61, 14);
			contentPanel.add(lblDeveloper);
		}
		{
			JLabel lblEpccpnV = new JLabel("EPC2CPN v.1.0.0");
			lblEpccpnV.setBounds(115, 24, 100, 14);
			contentPanel.add(lblEpccpnV);
		}
		
		JLabel lblWitchukornWannasing = new JLabel("Witchukorn Wannasing");
		lblWitchukornWannasing.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblWitchukornWannasing.setBounds(115, 116, 150, 14);
		contentPanel.add(lblWitchukornWannasing);
		
		JLabel lblNewLabel = new JLabel("witchukorn.w@gmail.com");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblNewLabel.setBounds(115, 141, 150, 14);
		contentPanel.add(lblNewLabel);
		
		
		
		JTextPane txtpnThisProgramFor = new JTextPane();
		txtpnThisProgramFor.setFont(new Font("Tahoma", Font.PLAIN, 10));
		txtpnThisProgramFor.setEditable(false);
		txtpnThisProgramFor.setText("This program has been developed for transaforming \"Event-diven Process Chain Diagram\" (XML file base on Visual Paradigm format) to Colour Petri Net (CPN Tools file format).");
//		txtpnThisProgramFor.setBounds(37, 49, 250, 56);
//		contentPanel.add(txtpnThisProgramFor);
		
		JScrollPane scrollBar = new JScrollPane();
		scrollBar.setBounds(37, 49, 250, 56);
		scrollBar.setViewportView(txtpnThisProgramFor);
		contentPanel.add(scrollBar);
		{
			JPanel buttonPane = new JPanel();
			FlowLayout fl_buttonPane = new FlowLayout(FlowLayout.CENTER);
			fl_buttonPane.setVgap(10);
			buttonPane.setLayout(fl_buttonPane);
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				cancelButton = new JButton("Close");
				cancelButton.addActionListener(this);
				buttonPane.add(cancelButton);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == cancelButton) {
			setVisible(false);
		}
	}
}
