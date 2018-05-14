package com.janthos.main;

import java.io.*;

public class FixedBuildingExpense extends BuildingExpense<FixedBuildingExpense>
{
	public FixedBuildingExpense() { }
	
	public FixedBuildingExpense(Building building, Expense<FixedBuildingExpense> expense)
	{
		super(building, expense);
	}
	
	public double calculateCost() 
	{
		return getExpense().calculateExpense(this);
	}

	public FixedBuildingExpense parseBuildingExpense(BufferedReader reader, Expense<FixedBuildingExpense> expense, Building building, Logger logger) 
	{
		if (reader == null) return null;
		
		FixedBuildingExpense buildingExpense = null;
		String line = null;
		boolean typeMatch = true;
		
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
				line = reader.readLine();
			}
			
			if (typeMatch)
			{
				buildingExpense = new FixedBuildingExpense(building, expense);
			}
		}
		catch (IOException e)
		{
			logger.appendMessage("Error reading file");
			return null;
		}
		
		return buildingExpense;
	}

	@Override
	public void printBuildingExpense(PrintWriter writer) 
	{
		writer.println("\t\t\tEXPENSE");
		writer.println("\t\t\t{");
		writer.println("\t\t\t\tEXPENSE_TYPE_CODE " + getExpense().getCode());
		writer.println("\t\t\t\tTYPE " + getExpense().getClass().getSimpleName().substring(0, getExpense().getClass().getSimpleName().length() - 7));
		writer.println("\t\t\t}");
	}
}