package com.janthos.main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.NumberFormat;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class AddBuildingExpenseForm extends JDialog
{
	JDialog thisFrame = this;
	private static final long serialVersionUID = 1L;
	private JTable expenseTable = null;
	private DefaultTableModel expenseTableModel = null;
	private JButton addButton = null;
	private JButton cancelButton = null;
	private JPanel buttonPanel = null;
	private JPanel inputPanel = null;
	private JPanel consumptionPanel = null;
	private JLabel consumptionLabel = null;
	private JFormattedTextField consumptionTextField = null;
	
	public AddBuildingExpenseForm(JDialog parent, Company company, Building building, DefaultTableModel buildingExpenseTableModel)
	{
		setTitle("Add Building Expense");
		setModal(true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(600, 400));
		
		//Set closing window event handler - Begin
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) 
		    {
				int result = JOptionPane.showConfirmDialog(thisFrame, "Save changes?", "Exit Without Saving", JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.YES_OPTION)
				{
					int selection = expenseTable.getSelectedRow();
					if (selection == -1)
					{
						JOptionPane.showMessageDialog(thisFrame, "Expense selection empty", "Invalid Expense", JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					Expense<?> expense = company.getExpenseByCode(expenseTable.getValueAt(selection, 0).toString());
					if (expense instanceof FixedExpense)
					{
						company.addBuildingExpense(new FixedBuildingExpense(building, (FixedExpense)expense));
					}
					else
					{
						company.addBuildingExpense(new VariableBuildingExpense(building, (VariableExpense)expense, ((Number)consumptionTextField.getValue()).doubleValue()));
					}
					buildingExpenseTableModel.addRow(new Object[] {expense.getCode(), expense.getDescription()});
				}
				thisFrame.dispose();
		    }
		});
		//Set closing window event handler - End
		
		//Initialize - Begin
		NumberFormat percentFormat = NumberFormat.getNumberInstance();
		percentFormat.setMinimumFractionDigits(3);
		percentFormat.setMaximumFractionDigits(3);
		addButton = new JButton("Add");
		cancelButton = new JButton("Cancel");
		buttonPanel = new JPanel();
		inputPanel = new JPanel();
		consumptionPanel = new JPanel();
		consumptionLabel = new JLabel("Consumption");
		consumptionTextField = new JFormattedTextField(percentFormat);
		expenseTable = new JTable();
		expenseTableModel = new DefaultTableModel(new Object[][] {}, new String[] { "Code", "Description" }) 
		{
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) 
			{
				return false;
			}
		};
		//Initialize - End
		
		//Formatted Text Fields - Begin
		consumptionTextField.setColumns(10);
		consumptionTextField.setValue(0);
		//Formatted Text Fields - End
		
		// Building Expense Table - Begin
		expenseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		expenseTable.setModel(expenseTableModel);
		for (Expense<?> expense : company.getAvailableExpense(building))
		{
			expenseTableModel.addRow(new Object[] {expense.getCode(), expense.getDescription()});
		}
		expenseTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
		    public void valueChanged(ListSelectionEvent e) 
		    {
		        if (!e.getValueIsAdjusting()) 
		        {
		            int selection = expenseTable.getSelectedRow();
		            if (selection != -1)
		            {
		            	Expense<?> expense = company.getExpenseByCode(expenseTable.getValueAt(selection, 0).toString());
		            	if (expense instanceof FixedExpense)
		            	{
		            		consumptionTextField.setEnabled(false);
		            	}
		            	else
		            	{
		            		consumptionTextField.setEnabled(true);
		            	}
		            }
		        }
		    }
		});
		// Building Expense Table - End 
		
		//Input Panel - Begin
		inputPanel.setLayout(new BorderLayout());
		inputPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		inputPanel.add(new JScrollPane(expenseTable), BorderLayout.CENTER);
		consumptionPanel.setLayout(new FlowLayout());
		consumptionPanel.add(consumptionLabel);
		consumptionPanel.add(consumptionTextField);
		inputPanel.add(consumptionPanel, BorderLayout.PAGE_END);
		//Input Panel - End
		
		//Add Button - Begin
		addButton.setPreferredSize(cancelButton.getPreferredSize());
		addButton.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent arg0) 
			{
				int selection = expenseTable.getSelectedRow();
				if (selection == -1)
				{
					JOptionPane.showMessageDialog(thisFrame, "Expense selection empty", "Invalid Expense", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				Expense<?> expense = company.getExpenseByCode(expenseTable.getValueAt(selection, 0).toString());
				if (expense instanceof FixedExpense)
				{
					company.addBuildingExpense(new FixedBuildingExpense(building, (FixedExpense)expense));
				}
				else
				{
					company.addBuildingExpense(new VariableBuildingExpense(building, (VariableExpense)expense, ((Number)consumptionTextField.getValue()).doubleValue()));
				}
				buildingExpenseTableModel.addRow(new Object[] {expense.getCode(), expense.getDescription()});
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
