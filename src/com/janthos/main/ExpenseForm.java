package com.janthos.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class ExpenseForm extends JDialog
{
	private static final long serialVersionUID = 6185103984262482927L;
	private JLabel codeLabel = null;
	private JLabel descriptionLabel = null;
	private JLabel priceLabel = null;
	private JLabel fixedCostLabel = null;
	private JLabel additionalCostLabel = null;
	private JLabel codeInfoLabel = null;
	private JLabel descriptionInfoLabel = null;
	private JLabel priceInfoLabel = null;
	private JLabel fixedCostInfoLabel = null;
	private JLabel additionalCostInfoLabel = null;
	private JButton calculateButton = null;
	private JDialog thisFrame = this;
	
	public ExpenseForm(JFrame parent, Company company, Expense<?> expense)
	{
		setTitle("Expense " + expense.getCode());
		setModal(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(300, 200));
		setResizable(false);
		
		//Initialize - Begin
		codeLabel = new JLabel("Code: ");
		descriptionLabel = new JLabel("Description: ");
		priceLabel = new JLabel("Price per Unit: ");
		fixedCostLabel = new JLabel("Fixed Cost: ");
		additionalCostLabel = new JLabel("Additional Cost: ");
		codeInfoLabel = new JLabel();
		descriptionInfoLabel = new JLabel();
		priceInfoLabel = new JLabel();
		fixedCostInfoLabel = new JLabel();
		additionalCostInfoLabel = new JLabel();
		calculateButton = new JButton();
		//Initialize - End
		
		//Set labels - Begin
		codeInfoLabel.setText(expense.getCode());
		descriptionInfoLabel.setText(expense.getDescription());
		if (expense instanceof FixedExpense)
		{
			priceInfoLabel.setText(((FixedExpense) expense).getPricePerSquareMeter() + " per m^2");
		}
		else
		{
			if (expense instanceof EnergyExpense)
			{
				priceInfoLabel.setText(String.format("%.3f", ((EnergyExpense)expense).getPricePerUnit()) + " per " + ((EnergyExpense)expense).getUnit());
				fixedCostInfoLabel.setText(String.format("%.3f", ((EnergyExpense)expense).getFixedCost()));
				additionalCostInfoLabel.setText(" Monthly ERT Cost " + String.format("%.3f", ((EnergyExpense)expense).getMonthlyERTCost()));
			}
			else if (expense instanceof TelephoneExpense)
			{
				priceInfoLabel.setText(String.format("%.3f", ((TelephoneExpense)expense).getPricePerUnit()) + " per " + ((TelephoneExpense)expense).getUnit());
				fixedCostInfoLabel.setText(String.format("%.3f", ((TelephoneExpense)expense).getFixedCost()));
				additionalCostInfoLabel.setText(" Monthly Telephone Charges " + String.format("%.3f", ((TelephoneExpense)expense).getTelephoneCharges()));
			}
			else if (expense instanceof WaterExpense)
			{
				priceInfoLabel.setText(String.format("%.3f", ((WaterExpense)expense).getPricePerUnit()) + " per " + ((WaterExpense)expense).getUnit());
				fixedCostInfoLabel.setText(String.format("%.3f", ((WaterExpense)expense).getFixedCost()));
				additionalCostInfoLabel.setText(String.format("%.3f", ((WaterExpense)expense).getPricePerUnit_2()) + " per " + ((WaterExpense)expense).getUnit() + " (above 100)");
			}
		}
		//Set labels - End		
		
		//calculateButton - Begin
		calculateButton.setText("Calculate Expense Cost");
		calculateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				JOptionPane.showMessageDialog(thisFrame, "The total cost of this expense is " + String.format("%.3f",company.calculateTotalCostOfExpense(expense)), "Total Expense Cost", JOptionPane.PLAIN_MESSAGE);
			}
		});
		//calculateButton - End
		
		GridBagLayout gridBag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		setLayout(gridBag);
		
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(5, 0, 5, 5);
		add(codeLabel, c);
		
		c.gridx = 1;
		add(codeInfoLabel, c);
		
		c.gridx = 0;
		add(descriptionLabel, c);
		
		c.gridx = 1;
		add(descriptionInfoLabel, c);
		
		c.gridx = 0;
		add(priceLabel, c);
		
		c.gridx = 1;
		add(priceInfoLabel, c);
		
		c.gridx = 0;
		add(fixedCostLabel, c);
		
		c.gridx = 1;
		add(fixedCostInfoLabel, c);
		
		c.gridx = 0;
		add(additionalCostLabel, c);
		
		c.gridx = 1;
		add(additionalCostInfoLabel, c);
		
		c.gridx = 1;
		add(calculateButton, c);
		
		pack();
		setLocationRelativeTo(parent);
		setVisible(true);
	}
}
