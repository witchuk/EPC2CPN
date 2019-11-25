package main;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class DialogSetting extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JButton okButton;
	private JButton cancelButton;
	private JTextField textField;
	private JTextField textField_1;
	
	private Properties properties;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			DialogSetting dialog = new DialogSetting(null, null, null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public DialogSetting(String browsePath, String savePath, Properties properties) {
		this.properties = properties;
		
		setModal(true);
		setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		setTitle("Setting");
		setBounds(200, 200, 500, 150);
		getContentPane().setLayout(new BorderLayout());
		JPanel contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblNewLabel = new JLabel("Default browse path :");
			lblNewLabel.setBounds(10, 11, 120, 14);
			contentPanel.add(lblNewLabel);
		}
		
		textField = new JTextField();
		textField.setText(browsePath);
		textField.setBounds(154, 8, 320, 20);
		contentPanel.add(textField);
		textField.setColumns(10);
		{
			JLabel lblDefaultSavePath = new JLabel("Default save path :");
			lblDefaultSavePath.setBounds(10, 39, 120, 14);
			contentPanel.add(lblDefaultSavePath);
		}
		{
			textField_1 = new JTextField();
			textField_1.setText(savePath);
			textField_1.setColumns(10);
			textField_1.setBounds(154, 36, 320, 20);
			contentPanel.add(textField_1);
		}
		{
			JPanel buttonPane = new JPanel();
			FlowLayout fl_buttonPane = new FlowLayout(FlowLayout.RIGHT);
			fl_buttonPane.setHgap(10);
			fl_buttonPane.setVgap(10);
			buttonPane.setLayout(fl_buttonPane);
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("Save");
				okButton.addActionListener(this);
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(this);
				buttonPane.add(cancelButton);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == okButton) {
			int result = JOptionPane.showConfirmDialog(
				this,
				"Are you sure you want to update setting?",
				"Update setting",
				JOptionPane.WARNING_MESSAGE);
			if (result == JOptionPane.YES_OPTION) {
				
				try {
					FileOutputStream out = new FileOutputStream("src/resources/config.properties");
					properties.setProperty("DEFAULT_BROWSE_PATH", textField.getText());
					properties.setProperty("DEFAULT_SAVE_PATH", textField_1.getText());
					properties.store(out, null);
					out.close();
					
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
				this.setVisible(false);
			}
		}
		
		else if (e.getSource() == cancelButton) {
			this.setVisible(false);
		}
	}
}
