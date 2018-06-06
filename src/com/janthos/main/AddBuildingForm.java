package com.janthos.main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.NumberFormat;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class AddBuildingForm extends JDialog
{
	private static final long serialVersionUID = 1L;
	private JDialog thisFrame = this;
	private JButton addButton = null;
	private JButton cancelButton = null;
	private JPanel buttonPanel = null;
	private JPanel inputPanel = null;
	private JLabel codeLabel = null;
	private JLabel descriptionLabel = null;
	private JLabel addressLabel = null;
	private JLabel areaLabel = null;
	private JLabel zoneValueLabel = null;
	private JTextField codeTextField = null;
	private JTextField descriptionTextField = null;
	private JTextField addressTextField = null;
	private JFormattedTextField areaTextField = null;
	private JFormattedTextField zoneValueTextField = null;
	
	public AddBuildingForm(JFrame parent, Company company, DefaultTableModel buildingTableModel)
	{
		setTitle("Add Building");
		setModal(true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(250, 250));
		
		//Set closing window event handler - Begin
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) 
		    {
				int result = JOptionPane.showConfirmDialog(thisFrame, "Save changes?", "Exit Without Saving", JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.YES_OPTION)
				{
					String code = codeTextField.getText();
					if (!company.checkBuildingCodeAvailability(code) || code.equals(""))
					{
						JOptionPane.showMessageDialog(thisFrame, "Code empty or aready in use.", "Invalid Code", JOptionPane.ERROR_MESSAGE);
						return;
					}
					String description = descriptionTextField.getText();
					if (description.equals(""))
					{
						JOptionPane.showMessageDialog(thisFrame, "Description empty.", "Invalid Description", JOptionPane.ERROR_MESSAGE);
						return;
					}
					String address = addressTextField.getText();
					if (address.equals(""))
					{
						JOptionPane.showMessageDialog(thisFrame, "Address empty.", "Invalid Address", JOptionPane.ERROR_MESSAGE);
						return;
					}
					double area = ((Number)areaTextField.getValue()).doubleValue();
					if (area <= 0)
					{
						JOptionPane.showMessageDialog(thisFrame, "Area negative or zero.", "Invalid Area", JOptionPane.ERROR_MESSAGE);
						return;
					}
					double zoneValue = ((Number)zoneValueTextField.getValue()).doubleValue();
					if (zoneValue < 0)
					{
						JOptionPane.showMessageDialog(thisFrame, "Zone value negative.", "Invalid Zone Value", JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					company.addBuilding(new Building(code, description, address, zoneValue, area));
					buildingTableModel.addRow(new Object[] {code, description});
				}
				thisFrame.dispose();
		    }
		});
		//Set closing window event handler - End		
		
		//Initialize - Begin
		codeLabel = new JLabel("Code");
		descriptionLabel = new JLabel("Description");
		addressLabel = new JLabel("Address");
		areaLabel = new JLabel("Area");
		zoneValueLabel = new JLabel("Zone Value");
		NumberFormat percentFormat = NumberFormat.getNumberInstance();
		percentFormat.setMinimumFractionDigits(3);
		percentFormat.setMaximumFractionDigits(3);
		codeTextField = new JTextField(10);
		descriptionTextField = new JTextField(10);
		addressTextField = new JTextField(10);
		areaTextField = new JFormattedTextField(percentFormat);
		zoneValueTextField = new JFormattedTextField(percentFormat);
		addButton = new JButton("Add");
		cancelButton = new JButton("Cancel");
		buttonPanel = new JPanel();
		inputPanel = new JPanel();
		//Initialize - End
		
		//Formatted Text Fields - Begin
		areaTextField.setColumns(10);
		areaTextField.setValue(0);
		zoneValueTextField.setColumns(10);
		zoneValueTextField.setValue(0);
		//Formatted Text Fields - End
		
		//Input Panel - Begin
		GridBagLayout gridBag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		inputPanel.setLayout(gridBag);
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(5, 0, 5, 5);
		c.gridx = 0;
		inputPanel.add(codeLabel, c);
		inputPanel.add(descriptionLabel, c);
		inputPanel.add(addressLabel, c);
		inputPanel.add(areaLabel, c);
		inputPanel.add(zoneValueLabel, c);
		c.gridx = 1;
		inputPanel.add(codeTextField, c);
		inputPanel.add(descriptionTextField, c);
		inputPanel.add(addressTextField, c);
		inputPanel.add(areaTextField, c);
		inputPanel.add(zoneValueTextField, c);
		//Input Panel - End
		
		//Add Button - Begin
		addButton.setPreferredSize(cancelButton.getPreferredSize());
		addButton.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent arg0) 
			{
				String code = codeTextField.getText();
				if (!company.checkBuildingCodeAvailability(code) || code.equals(""))
				{
					JOptionPane.showMessageDialog(thisFrame, "Code empty or aready in use.", "Invalid Code", JOptionPane.ERROR_MESSAGE);
					return;
				}
				String description = descriptionTextField.getText();
				if (description.equals(""))
				{
					JOptionPane.showMessageDialog(thisFrame, "Description empty.", "Invalid Description", JOptionPane.ERROR_MESSAGE);
					return;
				}
				String address = addressTextField.getText();
				if (address.equals(""))
				{
					JOptionPane.showMessageDialog(thisFrame, "Address empty.", "Invalid Address", JOptionPane.ERROR_MESSAGE);
					return;
				}
				double area = ((Number)areaTextField.getValue()).doubleValue();
				if (area <= 0)
				{
					JOptionPane.showMessageDialog(thisFrame, "Area negative or zero.", "Invalid Area", JOptionPane.ERROR_MESSAGE);
					return;
				}
				double zoneValue = ((Number)zoneValueTextField.getValue()).doubleValue();
				if (zoneValue < 0)
				{
					JOptionPane.showMessageDialog(thisFrame, "Zone value negative.", "Invalid Zone Value", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				company.addBuilding(new Building(code, description, address, zoneValue, area));
				buildingTableModel.addRow(new Object[] {code, description});
				thisFrame.dispose();
			}
		});
		//Add Button - End
		
		//Cancel Button - Begin
		cancelButton.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent arg0) 
			{
				thisFrame.dispose();
			}
		});
		//Cancel Button - End
		
		//Button Panel - Begin
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonPanel.add(addButton);
		buttonPanel.add(cancelButton);
		//Button Panel - End
		
		add(inputPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.PAGE_END);

		
		
		pack();
		setLocationRelativeTo(parent);
		setVisible(true);
	}
}
