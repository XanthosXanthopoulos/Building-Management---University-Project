package com.janthos.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class RentExpense extends FixedExpense
{
	public RentExpense() {}
	
	public RentExpense(String code, String description, double pricePerSquareMeter)
	{
		super(code, description, pricePerSquareMeter);
	}
	
	public RentExpense(RentExpense rentExpense)
	{
		super(rentExpense);
	}
	
	public double calculateExpense(FixedBuildingExpense buildingExpense) 
	{
		return getPricePerSquareMeter() * buildingExpense.getBuilding().getArea();
	}

	@Override
	public Expense<FixedBuildingExpense> parseExpense(BufferedReader reader, Logger logger) 
	{
		if (reader == null) return null;
		
		RentExpense expense = new RentExpense();
		String line = null;
		
		expense.setPricePerSquareMeter(2);
		
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
				else if (line.matches("(?i)\\s*price_per_square_meter\\s+\\S+\\s*"))
				{
					try 
					{
						expense.setPricePerSquareMeter(Double.parseDouble(line.trim().substring(23).trim()));
					}
					catch (NumberFormatException e)
					{
						logger.appendMessage("Error converting input to number");
						return null;
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
		writer.println("\t\tTYPE Rent");
		writer.println("\t\tPRICE_PER_SQUARE_METER " + getPricePerSquareMeter());
		writer.println("\t}");
	}
}