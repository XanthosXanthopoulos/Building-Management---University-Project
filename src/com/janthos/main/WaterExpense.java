package com.janthos.main;

import java.io.*;

public class WaterExpense extends VariableExpense
{	
	private double pricePerUnit_2;
	
	public WaterExpense() {}
	
	public WaterExpense(String code, String description, String unit, double pricePerUnit, double pricePerUnit_2, double fixedCost)
	{
		super(code, description, pricePerUnit, unit, fixedCost);
		this.pricePerUnit_2 = pricePerUnit_2;
	}
	
	public WaterExpense(WaterExpense waterExpense)
	{
		super(waterExpense);
		this.pricePerUnit_2 = waterExpense.pricePerUnit_2;
	}

	public double getPricePerUnit_2()
	{
		return pricePerUnit_2;
	}
	
	public void setPricePerUnit_2(double pricePerUnit_2)
	{
		this.pricePerUnit_2 = pricePerUnit_2;
	}
	
	public double calculateExpense(VariableBuildingExpense buildingExpense) 
	{
		if (buildingExpense.getConsumption() <= 100)
		{
			return getFixedCost() + getPricePerUnit() * buildingExpense.getConsumption();
		}
		else
		{
			return getFixedCost() + pricePerUnit_2 * buildingExpense.getConsumption();
		}
	}
	
	public String toString()
	{
		return super.toString() + String.format("%5s Cost per %-5s (>100):  %.3f", "", getUnit(), pricePerUnit_2);
	}

	public Expense<VariableBuildingExpense> parseExpense(BufferedReader reader, Logger logger) 
	{
		if (reader == null) return null;
		
		WaterExpense expense = new WaterExpense();
		String line = null;
		
		expense.setUnit("m^3");
		expense.setPricePerUnit(0.005);
		expense.setPricePerUnit_2(0.008);
		expense.setFixedCost(15);
		
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
				else if (line.matches("(?i)\\s*price_per_unit_2\\s+\\S+\\s*"))
				{
					try
					{
						expense.setPricePerUnit_2(Double.parseDouble(line.trim().substring(17).trim()));
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
		writer.println("\t\tTYPE WaterExpense");
		writer.println("\t\tUNIT " + getUnit());
		writer.println("\t\tPRICE_PER_UNIT " + getPricePerUnit());
		writer.println("\t\tPRICE_PER_UNIT_2 " + getPricePerUnit_2());
		writer.println("\t\tFIXED_COST " + getFixedCost());
		writer.println("\t}");
	}
}
