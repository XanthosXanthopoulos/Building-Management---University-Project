package com.janthos.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class VariableBuildingExpense extends BuildingExpense<VariableBuildingExpense>
{
	private double consumption;
	
	public VariableBuildingExpense() { }
	
	public VariableBuildingExpense(Building building, Expense<VariableBuildingExpense> expense, double consumption)
	{
		super(building, expense);
		this.consumption = consumption;
	}
	
	public double getConsumption()
	{
		return consumption;
	}
	
	public void setConsumption(double consumption)
	{
		this.consumption = consumption;
	}
	
	public double calculateCost()
	{
		return getExpense().calculateExpense(this);
	}
	
	public String toString()
	{
		return super.toString() + String.format("  %.3f", consumption);
	}
	
	public VariableBuildingExpense parseBuildingExpense(BufferedReader reader, Expense<VariableBuildingExpense> expense, Building building, Logger logger) 
	{
		if (reader == null) return null;
		
		VariableBuildingExpense buildingExpense = null;
		String line = null;
		boolean typeMatch = true;
		double consumption = 0;
		
		try
		{
			line = reader.readLine();
			while(line != null && !line.matches("(?i)\\s*\\{\\s*|\\s*\\}\\s*|\\s*expense\\s*"))
			{
				if (line.matches("(?i)\\s*type\\s+\\S+\\s*"))
				{
					if (!line.trim().substring(5).trim().equalsIgnoreCase(expense.getClass().getSimpleName().substring(0, expense.getClass().getSimpleName().length() - 7)))
					{
						typeMatch = false;
						logger.appendMessage("Expense type and code doesn't match");
					}
				}
				else if (line.matches("(?i)\\s*consumption\\s+\\S+\\s*"))
				{
					try
					{
						System.out.println(line.trim().substring(12).trim());
					}
					catch (NumberFormatException e)
					{
						logger.appendMessage("Error converting input to number");
					}
				}
				line = reader.readLine();
			}
			if (typeMatch)
			{
				buildingExpense = new VariableBuildingExpense(building, expense, consumption);
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
		
		return buildingExpense;
	}
	
	public void printBuildingExpense(PrintWriter writer) 
	{
		writer.println("\t\t\tEXPENSE");
		writer.println("\t\t\t{");
		writer.println("\t\t\t\tEXPENSE_TYPE_CODE " + getExpense().getCode());
		writer.println("\t\t\t\tTYPE " + getExpense().getClass().getSimpleName().substring(0, getExpense().getClass().getSimpleName().length() - 7));
		writer.println("\t\t\t\tCONSUMPTION " + getConsumption());
		writer.println("\t\t\t}");
	}
}