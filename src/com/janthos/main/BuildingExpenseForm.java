package com.janthos.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class BuildingExpenseForm extends JDialog 
{
	private static final long serialVersionUID = 1L;

	private JTable buildingExpenseTable = null;
	private DefaultTableModel buildingExpenseTableModel = null;
	private JPanel buttonPanel = null;
	private JButton addButton = null;
	private JButton removeButton = null;
	private JButton editShowButton = null;
	private JDialog thisFrame = this;

	public BuildingExpenseForm(JFrame parent, Company company, Building building) 
	{
		setTitle("Building Expenses of " + building.getCode());
		setModal(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(600, 400));

		// Initialize - Begin
		buttonPanel = new JPanel();
		buildingExpenseTable = new JTable();
		buildingExpenseTableModel = new DefaultTableModel(new Object[][] {}, new String[] { "Code", "Description" }) 
		{
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) 
			{
				return false;
			}
		};
		addButton = new JButton("Add");
		removeButton = new JButton("Remove");
		editShowButton = new JButton("Edit/Show");
		// Initialize - End

		//Building Expense Table Model - Begin
		for (BuildingExpense<?> buildExp : company.getBuildingExpensesOfBuilding(building))
		{
			buildingExpenseTableModel.addRow(new Object[] {buildExp.getExpense().getCode(), buildExp.getExpense().getDescription()});
		}
		//Building Expense Table Model - End
		
		// Building Expense Table - Begin
		buildingExpenseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		buildingExpenseTable.setModel(buildingExpenseTableModel);
		buildingExpenseTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) 
			{
				if (evt.getClickCount() == 2) 
				{
					Point pnt = evt.getPoint();
					int row = buildingExpenseTable.rowAtPoint(pnt);
					if (row != -1)
					{
						Expense<?> expense = company.getExpenseByCode(buildingExpenseTable.getValueAt(row, 0).toString());
						BuildingExpense<?> buildingExpense = company.getBuildingExpenseOfBuilding(building, expense);
						new EditBuildingExpenseForm(thisFrame, company, buildingExpense, buildingExpenseTableModel, row);
					}
				}
			}
		});
		// Building Expense Table - End

		add(new JScrollPane(buildingExpenseTable), BorderLayout.CENTER);

		// Add Button - Begin
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				new AddBuildingExpenseForm(thisFrame, company, building, buildingExpenseTableModel);
			}
		});
		// Add Button - End

		// Remove Button - Begin
		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				int selection = buildingExpenseTable.getSelectedRow();
				if (selection != -1) 
				{
					int result = JOptionPane.showConfirmDialog(thisFrame, "Are You Sure You Want to Delete this Building Expense?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
					if (result == JOptionPane.YES_OPTION) 
					{
						String code = buildingExpenseTable.getValueAt(selection, 0).toString();
						for (BuildingExpense<?> buildExp : company.getBuildingExpensesOfBuilding(building))
						{
							if (buildExp.getExpense().getCode().equals(code))
							{
								building.getBuildingExpense().remove(buildExp);
								company.getBuildingExpense().remove(buildExp);
								buildingExpenseTableModel.removeRow(selection);
							}
						}
					}
				}
			}
		});
		// Remove Button - End

		// Edit/Show Button - Begin
		editShowButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				int selection = buildingExpenseTable.getSelectedRow();
				if (selection != -1) 
				{
					Expense<?> expense = company.getExpenseByCode(buildingExpenseTable.getValueAt(selection, 0).toString());
					BuildingExpense<?> buildingExpense = company.getBuildingExpenseOfBuilding(building, expense);
					new EditBuildingExpenseForm(thisFrame, company, buildingExpense, buildingExpenseTableModel, selection);
				}
			}
		});
		// Edit/Show Button - End
		
		
		//Button Panel - Begin
		GridBagLayout gridBag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		buttonPanel.setLayout(gridBag);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.insets = new Insets(5, 5, 5, 5);
		buttonPanel.add(addButton, c);
		buttonPanel.add(removeButton, c);
		buttonPanel.add(editShowButton, c);
		//Button Panel - End
		
		add(buttonPanel, BorderLayout.EAST);
				
				
		pack();
		setLocationRelativeTo(parent);
		setVisible(true);
	}
}
