package com.janthos.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class TelephoneExpense extends VariableExpense
{	
	private double telephoneCharges;
	
	public TelephoneExpense() {}
	
	public TelephoneExpense(String code, String description, String unit, double pricePerUnit, double fixedCost, double telephoneCharges)
	{
		super(code, description, pricePerUnit, unit, fixedCost);
		this.telephoneCharges = telephoneCharges;
	}
	
	public TelephoneExpense(TelephoneExpense telephoneExpense)
	{
		super(telephoneExpense);
		this.telephoneCharges = telephoneExpense.telephoneCharges;
	}

	public double getTelephoneCharges() 
	{
		return telephoneCharges;
	}

	public void setTelephoneCharges(double telephoneCharges) 
	{
		this.telephoneCharges = telephoneCharges;
	}

	public double calculateExpense(VariableBuildingExpense buildingExpense) 
	{
		return getFixedCost() + telephoneCharges + getPricePerUnit() * buildingExpense.getConsumption();
	}
	
	public String toString()
	{
		return super.toString() + String.format("%5s Monthly telephone charges: %.3f", "", telephoneCharges);
	}

	@Override
	public Expense<VariableBuildingExpense> parseExpense(BufferedReader reader, Logger logger) 
	{
		if (reader == null) return null;
		
		TelephoneExpense expense = new TelephoneExpense();
		String line = null;
		
		expense.setUnit("min");
		expense.setPricePerUnit(0.005);
		expense.setTelephoneCharges(10);
		expense.setFixedCost(18);
		
		try
		{
			line = reader.readLine();
			while(line != null && !line.matches("(?i)\\s*\\{\\s*|\\s*\\}\\s*|\\s*expense_type\\s*"))
			{
				if (line.matches("(?i)\\s*expense_type_code\\s+(\\S+\\s*)+"))
				{
					expense.setCode(line.trim().substring(18).trim());
				}
				else if (line.matches("(?i)\\s*expense_type_descr\\s+(\\S+\\s*)+"))
				{
					expense.setDescription(line.trim().substring(19).trim());
				}
				else if (line.matches("(?i)\\s*unit\\s+(\\S+\\s*)+"))
				{
					expense.setUnit(line.trim().substring(5).trim());
				}
				else if (line.matches("(?i)\\s*price_per_unit\\s+\\S+\\s*"))
				{
					try
					{
						expense.setPricePerUnit(Double.parseDouble(line.trim().substring(15).trim()));
					}
					catch (NumberFormatException e)
					{
						logger.appendMessage("Error converting input to number");
					}
				}
				else if (line.matches("(?i)\\s*telephone_charges\\s+\\S+\\s*"))
				{
					try
					{
						expense.setTelephoneCharges(Double.parseDouble(line.trim().substring(18).trim()));
					}
					catch (NumberFormatException e)
					{
						logger.appendMessage("Error converting input to number");
					}
				}
				else if (line.matches("(?i)\\s*fixed_cost\\s+\\S+\\s*"))
				{
					try
					{
						expense.setFixedCost(Double.parseDouble(line.trim().substring(11).trim()));
					}
					catch (NumberFormatException e)
					{
						logger.appendMessage("Error converting input to number");
					}
				}
				
				reader.mark(8192);
				line = reader.readLine();
			}
			
		}
		catch (IOException e)
		{
			logger.appendMessage("Error reading file");
			return null;
		}
		catch (NullPointerException e)
		{
			logger.appendMessage("Error reading number. Input is null");
			return null;
		}
		
		return expense;
	}
	
	public void printFile(PrintWriter writer) 
	{
		writer.println("\tEXPENSE_TYPE");
		writer.println("\t{");
		writer.println("\t\tEXPENSE_TYPE_CODE " + getCode());
		writer.println("\t\tEXPENSE_TYPE_DESCR " + getDescription());
		writer.println("\t\tTYPE Telephone");
		writer.println("\t\tUNIT " + getUnit());
		writer.println("\t\tPRICE_PER_UNIT " + getPricePerUnit());
		writer.println("\t\tFIXED_COST " + getFixedCost());
		writer.println("\t\tTELEPHONE_CHARGES " + getTelephoneCharges());
		writer.println("\t}");
	}
}