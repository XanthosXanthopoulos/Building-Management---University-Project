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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;

public class MainForm extends JFrame
{
	private static final long serialVersionUID = 7967386909836532270L;
	
	private JMenuBar menuBar = null;
	private JMenu fileMenu = null;
	private JMenuItem openExpenseMenuItem = null;
	private JMenuItem openBuildingMenuItem = null;
	private JMenuItem saveExpenseMenuItem = null;
	private JMenuItem saveBuildingMenuItem = null;
	private JComboBox<String> expenseTypeComboBox = null;
	private JTabbedPane tabControl = null;
	private JPanel expensePanel = null, buildingPanel = null, buttonPanel = null;
	private JTable expenseTable = null, buildingTable = null;
	private DefaultTableModel expenseTableModel = null;
	private DefaultTableModel buildingTableModel = null;
	private JButton addButton = null;
	private JButton removeButton = null;
	private JButton editShowButton = null;
	private JButton manageBuildingExpenseButton = null;
	private JButton calculateCostButton = null;
    private JFrame thisFrame = this;
	
	public MainForm(Company company)
	{
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(600, 400));
		setTitle(company.getBrandName() + " Expense Manager");
		
		//Initialize - Begin
		menuBar = new JMenuBar();
		fileMenu = new JMenu();
		openExpenseMenuItem = new JMenuItem();
		openBuildingMenuItem = new JMenuItem();
		saveExpenseMenuItem = new JMenuItem();
		saveBuildingMenuItem = new JMenuItem();
		tabControl = new JTabbedPane();
		expensePanel = new JPanel();
		buildingPanel = new JPanel();
		expenseTable = new JTable();
		buildingTable = new JTable();
		expenseTableModel = new DefaultTableModel(new Object[][] {}, new String[] {"Code", "Description"}) 
		{
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column)
		    {
		      return false;
		    }
		};
		buildingTableModel = new DefaultTableModel(new Object[][] {}, new String[] {"Code", "Description"}) 
		{
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column)
		    {
		      return false;
		    }
		};
		buttonPanel = new JPanel();
		addButton = new JButton("Add");
		removeButton = new JButton("Remove");
		editShowButton = new JButton("Edit/Show");
		manageBuildingExpenseButton = new JButton("Manage Building Expenses");
		calculateCostButton = new JButton("Calculate Building Cost");
		//Initialize - End
		
		//fileMenu - Begin
		fileMenu.setText("File");
		//fileMenu - End
		
		//openExpenseMenuItem - Begin
		openExpenseMenuItem.setText("Open Expenses...");
		openExpenseMenuItem.addActionListener(new AbstractAction("Open Expenses...") {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) 
			{
				JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getDefaultDirectory());
				fileChooser.setFileFilter(new FileNameExtensionFilter("Text files", "txt", "text"));
				int returnValue = fileChooser.showOpenDialog(null);
				
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					Logger logger = new Logger();
					loadExpenses(selectedFile.getAbsolutePath(), company, logger);
					JOptionPane.showMessageDialog(thisFrame, logger.getLog(), "Parsing Info", JOptionPane.PLAIN_MESSAGE);
					
					expenseTableModel.setRowCount(0);
					for (Expense<?> expense : company.getExpense())
					{
						if (expense.getClass().getSimpleName().substring(0, expense.getClass().getSimpleName().length() - 7).equals(expenseTypeComboBox.getSelectedItem().toString()))
						{
							expenseTableModel.addRow(new Object[] {expense.getCode(), expense.getDescription()});
						}
					}
				}
			}
		});
		//openExpenseMenuItem - End
		
		//openBuildingMenuItem - Begin
		openBuildingMenuItem.setText("Open Buildings...");
		openBuildingMenuItem.addActionListener(new AbstractAction("Open Buildings...") {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) 
			{
				JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getDefaultDirectory());
				fileChooser.setFileFilter(new FileNameExtensionFilter("Text files", "txt", "text"));
				int returnValue = fileChooser.showOpenDialog(null);
				
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					Logger logger = new Logger();
					loadBuildings(selectedFile.getAbsolutePath(), company, logger);
					JOptionPane.showMessageDialog(thisFrame, logger.getLog(), "Parsing Info", JOptionPane.PLAIN_MESSAGE);
					
					buildingTableModel.setRowCount(0);
					for (Building building : company.getBuilding())
					{
						buildingTableModel.addRow(new Object[] {building.getCode(), building.getDescription()});
					}
				}
			}
		});
		//openBuildingMenuItem - End
		
		//saveExpenseMenuItem - Begin
		saveExpenseMenuItem.setText("Save Expenses");
		saveExpenseMenuItem.addActionListener(new AbstractAction("Save Expenses") {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) 
			{
				JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getDefaultDirectory());
				fileChooser.setFileFilter(new FileNameExtensionFilter("Text files", "txt", "text"));
				int returnValue = fileChooser.showSaveDialog(null);
				
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					Logger logger = new Logger();
					SaveExpenses(selectedFile.getAbsolutePath(), company, logger);
					JOptionPane.showMessageDialog(thisFrame, logger.getLog(), "Saving Info", JOptionPane.PLAIN_MESSAGE);
				}	
			}
		});
		//saveExpenseMenuItem - End
		
		//saveBuildingMenuItem - Begin
		saveBuildingMenuItem.setText("Save Buildings");
		saveBuildingMenuItem.addActionListener(new AbstractAction("Save Buildings") {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) 
			{
				JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getDefaultDirectory());
				fileChooser.setFileFilter(new FileNameExtensionFilter("Text files", "txt", "text"));
				int returnValue = fileChooser.showSaveDialog(null);
				
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					Logger logger = new Logger();
					SaveBuildings(selectedFile.getAbsolutePath(), company, logger);
					JOptionPane.showMessageDialog(thisFrame, logger.getLog(), "Saving Info", JOptionPane.PLAIN_MESSAGE);
				}				
			}
		});
		//saveBuildingMenuItem - End
		
		fileMenu.add(openExpenseMenuItem);
		fileMenu.add(openBuildingMenuItem);
		fileMenu.add(new JSeparator());
		fileMenu.add(saveExpenseMenuItem);
		fileMenu.add(saveBuildingMenuItem);
		menuBar.add(fileMenu);
		setJMenuBar(menuBar);
		
		//Tab Control - Begin
		tabControl.addTab("Expenses", expensePanel);
		tabControl.addTab("Buildings", buildingPanel);
		add(tabControl, BorderLayout.CENTER);
		//Tab Control - End
		
		//Combo Box - Begin
		expenseTypeComboBox = new JComboBox<String>();
		expenseTypeComboBox.addItem("Cleaning");
		expenseTypeComboBox.addItem("Energy");
		expenseTypeComboBox.addItem("Rent");
		expenseTypeComboBox.addItem("Telephone");
		expenseTypeComboBox.addItem("Water");
		expenseTypeComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				if (expenseTableModel.getRowCount() > 0)
				{
					for (int i = expenseTableModel.getRowCount() - 1; i > -1; i--) 
					{
				        expenseTableModel.removeRow(i);
				    }
				}
				for (Expense<?> expense : company.getExpense())
				{
					if (expense.getClass().getSimpleName().substring(0, expense.getClass().getSimpleName().length() - 7).equals(expenseTypeComboBox.getSelectedItem().toString()))
					{
						expenseTableModel.addRow(new Object[] {expense.getCode(), expense.getDescription()});
					}
				}
			}
		});
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		p.add(Box.createHorizontalStrut(140), BorderLayout.WEST);
		p.add(expenseTypeComboBox, BorderLayout.CENTER);
		p.add(Box.createHorizontalStrut(140), BorderLayout.EAST);
		//Combo Box - End
		
		//Expense Table - Begin
		expenseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		expenseTable.setModel(expenseTableModel);
		expenseTable.addMouseListener(new MouseAdapter() {
	         public void mouseClicked(MouseEvent evt) {
	             if (evt.getClickCount() == 2) {
	                 Point pnt = evt.getPoint();
	                 int row = expenseTable.rowAtPoint(pnt);
	                 new ExpenseForm(thisFrame, company, company.getExpenseByCode(expenseTable.getValueAt(row, 0).toString()));
	             }
	         }
	     });
		//Expense - Table - End
		
		//Expense Panel - Begin
		expensePanel.setLayout(new BorderLayout());
		expensePanel.add(p, BorderLayout.PAGE_START);
		expensePanel.add(new JScrollPane(expenseTable), BorderLayout.CENTER);
		//Expense Panel - End
		
		//Building Table - Begin
		buildingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		buildingTable.setModel(buildingTableModel);
		buildingTable.addMouseListener(new MouseAdapter() {
	         public void mouseClicked(MouseEvent evt) {
	             if (evt.getClickCount() == 2) {
	                 Point pnt = evt.getPoint();
	                 int row = buildingTable.rowAtPoint(pnt);
	                 new EditBuildingForm(thisFrame, company, company.getBuildingByCode(buildingTable.getValueAt(row, 0).toString()), buildingTableModel, row);
	             }
	         }
	     });
		//Building Table - End
		
		//Building Panel - Begin
		buildingPanel.setLayout(new BorderLayout());
		buildingPanel.add(new JScrollPane(buildingTable), BorderLayout.CENTER);
		//Building Panel - End
		
		//Add Button - Begin
		addButton.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent arg0) 
			{
				new AddBuildingForm(thisFrame, company, buildingTableModel);
			}
		});
		//Add Button - End
		
		//Remove Button - Begin
		removeButton.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent arg0) 
			{
				int selection = buildingTable.getSelectedRow();
				if (selection != -1)
				{
					int result = JOptionPane.showConfirmDialog(thisFrame, "Are You Sure You Want to Delete this Building?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
					if (result == JOptionPane.YES_OPTION)
					{
						Building building = company.getBuildingByCode(buildingTable.getValueAt(selection, 0).toString());
						company.getBuildingExpense().removeAll(company.getBuildingExpensesOfBuilding(building));
						company.getBuilding().remove(building);
						buildingTableModel.removeRow(selection);
					}
				}
			}
		});
		//Remove Button - End
		
		//Edit/Show Button - Begin
		editShowButton.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent arg0) 
			{
				int selection = buildingTable.getSelectedRow();
				if (selection != -1)
				{
					Building building = company.getBuildingByCode(buildingTable.getValueAt(selection, 0).toString());
					new EditBuildingForm(thisFrame, company, building, buildingTableModel, selection);
				}
			}
		});
		//Edit/Show Button - End
		
		//Manage Expense Button - Begin
		manageBuildingExpenseButton.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent arg0) 
			{
				int selection = buildingTable.getSelectedRow();
				if (selection != -1)
				{
					Building building = company.getBuildingByCode(buildingTable.getValueAt(selection, 0).toString());
					new BuildingExpenseForm(thisFrame, company, building);
				}
			}
		});
		//Manage Expense Button - End
		
		//Calculate Cost Button - Begin
		calculateCostButton.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent arg0) 
			{
				int selection = buildingTable.getSelectedRow();
				if (selection != -1)
				{
					Building building = company.getBuildingByCode(buildingTable.getValueAt(selection, 0).toString());
					JOptionPane.showMessageDialog(thisFrame, "The total cost of this building is " + String.format("%.3f",company.calculateBuildingExpense(building)), "Total Expense Cost", JOptionPane.PLAIN_MESSAGE);
				}
			}
		});
		//Calculate Cost Button - End
		
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
		buttonPanel.add(Box.createVerticalStrut(40), c);
		buttonPanel.add(manageBuildingExpenseButton, c);
		buttonPanel.add(calculateCostButton, c);
		//Button Panel - End
		
		buildingPanel.add(buttonPanel, BorderLayout.EAST);
		
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public void loadExpenses(String filePath, Company company, Logger logger)
	{
		File file = null;
		BufferedReader reader = null;
		String line = null;
		
		logger.appendMessage("Begining Expense parsing");

		try
		{
			file = new File(filePath);
		}
		catch (NullPointerException e)
		{
			logger.appendMessage("File not found");
			return;
		}
		
		try
		{
			reader = new BufferedReader(new FileReader(file));
		}
		catch (FileNotFoundException e)
		{
			logger.appendMessage("Error opening file");
			return;
		}
		
		try
		{
			line = reader.readLine();
			logger.incrementLine();
			while(line != null)
			{
				if (line != null && line.matches("(?i)\\s*expense_type_list\\s*"))
				{
					line = reader.readLine();
					logger.incrementLine();
					if (line != null && line.matches("\\s*\\{\\s*"))
					{
						line = reader.readLine();
						logger.incrementLine();
						while (line != null && !line.matches("(?i)\\s*\\{\\s*|\\s*\\}\\s*|\\s*expense_type_list\\s*"))
						{
							if (line != null && line.matches("(?i)\\s*expense_type\\s*"))
							{
								Expense<?> expense = Expense.parse(reader, logger);
								if (expense != null && company.checkExpenseCodeAvailability(expense.getCode()))
								{
									company.addExpense(expense);
								}
								
								line = reader.readLine();
								logger.incrementLine();
							}
							else if (line != null && line.matches("\\s*"))
							{
								line = reader.readLine();
								logger.incrementLine();
							}
							else throw new IOException();
						}
						line = reader.readLine();
						logger.incrementLine();
					}
					else if (line != null && line.matches("\\s*"))
					{
						line = reader.readLine();
						logger.incrementLine();
					}
					else throw new IOException();
				}
				else if (line != null && line.matches("\\s*"))
				{
					line = reader.readLine();
					logger.incrementLine();
				}
				else throw new IOException();
			}
		}
		catch (IOException e)
		{
			logger.appendMessage("Error reading file");
		}
		finally
		{
			logger.appendMessage("Parsing ended");
			try
			{
				reader.close();
			}
			catch (IOException e)
			{
				logger.appendMessage("Error clossing file");
			}
		}
	}
	
	public void loadBuildings(String filePath, Company company, Logger logger)
	{
		File file = null;
		BufferedReader reader = null;
		String line = null;
		
		logger.appendMessage("Begining Building parsing");
		
		try
		{
			file = new File(filePath);
		}
		catch (NullPointerException e)
		{
			logger.appendMessage("File not found");
			return;
		}
		
		try
		{
			reader = new BufferedReader(new FileReader(file));
		}
		catch (FileNotFoundException e)
		{
			logger.appendMessage("Error opening file");
			return;
		}
		
		try
		{
			line = reader.readLine();
			logger.incrementLine();
			
			while (line != null)
			{
				if (line != null && line.matches("(?i)\\s*building_list\\s*"))
				{
					line = reader.readLine();
					logger.incrementLine();
					if (line != null && line.matches("\\s*\\{\\s*"))
					{
						line = reader.readLine();
						logger.incrementLine();
						while (line != null && !line.matches("(?i)\\s*\\{\\s*|\\s*\\}\\s*|\\s*building_list\\s*"))
						{
							if (line != null && line.matches("(?i)\\s*building\\s*"))
							{
								Building building = Building.parse(reader, company.getExpense(), logger);
								if (building != null && company.checkBuildingCodeAvailability(building.getCode()))
								{
									company.addBuilding(building);
								}
								
								line = reader.readLine();
								logger.incrementLine();
							}
							else if (line != null && line.matches("\\s*"))
							{
								line = reader.readLine();
								logger.incrementLine();
							}
							else throw new IOException();
						}
						line = reader.readLine();
						logger.incrementLine();
					}
					else if (line != null && line.matches("\\s*"))
					{
						line = reader.readLine();
						logger.incrementLine();
					}
					else throw new IOException();
				}
				else if (line != null && line.matches("\\s*"))
				{
					line = reader.readLine();
					logger.incrementLine();
				}
				else throw new IOException();
			}
		}
		catch (IOException e)
		{
			logger.appendMessage("Error reading file");
		}
		finally
		{
			logger.appendMessage("Parsing ended");
			try
			{
				reader.close();
			}
			catch (IOException e)
			{
				logger.appendMessage("Error closing file");
			}
		}
	}
	
	public void SaveExpenses(String filePath, Company company, Logger logger)
	{
		PrintWriter writer = null;
		
		try
		{
			writer = new PrintWriter(new FileWriter(filePath), true);
			writer.println("EXPENSE_TYPE_LIST");
			writer.println("{");
			for (Expense<?> expense : company.getExpense())
			{
				expense.printFile(writer);
			}
			writer.println("}");
			logger.appendMessage("Writing file successful.");
		}
		catch(IOException e)
		{
			logger.appendMessage("Error writing file.");
		}
		finally
		{
			writer.close();
		}
	}
	
	public void SaveBuildings(String filePath, Company company, Logger logger)
	{
		PrintWriter writer = null;
		
		try
		{
			writer = new PrintWriter(new FileWriter(filePath), true);
			writer.println("BUILDING_LIST");
			writer.println("{");
			for (Building building : company.getBuilding())
			{
				building.printBuilding(writer);
			}
			writer.println("}");
			logger.appendMessage("Writing file successful.");
		}
		catch(IOException e)
		{
			logger.appendMessage("Error writing file.");
		}
		finally
		{
			writer.close();
		}
	}
}
