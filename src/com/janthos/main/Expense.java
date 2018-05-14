package com.janthos.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public abstract class Expense <T extends BuildingExpense<T>>
{
	private String code;
	private String description;
	
	public Expense() {}
	
	public Expense(String code, String description)
	{
		this.code = code;
		this.description = description;
	}
	
	public Expense(Expense<T> expense)
	{
		this.code = expense.code;
		this.description = expense.description;
	}
	
	public String getCode()
	{
		return code;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public void setCode(String code)
	{
		this.code = code;
	}
	
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	public String toString()
	{
		return String.format("%-8s", code) + String.format("%-20s", description);
	}
	
	public abstract double calculateExpense(T buildingExpense);
	
	public abstract Expense<T> parseExpense(BufferedReader reader, Logger logger);
	
	public abstract void printFile(PrintWriter writer);
	
	public static Expense<?> parse(BufferedReader reader, Logger logger)
	{
		if (reader == null) return null;
		Expense<?> expense = null;
		String line = null, type = null;
		boolean hasCode, hasType, hasDescr, hasUniqueCode, hasUniqueType, hasUniqueDescr;
		
		try
		{
			line = reader.readLine();
			logger.incrementLine();
			if (line != null && line.matches("(?i)\\s*\\{\\s*"))
			{
				reader.mark(8192);
				line = reader.readLine();
				logger.incrementLine();
				hasCode = hasDescr = hasType = hasUniqueCode = hasUniqueType = hasUniqueDescr = false;
				type = null;
				while (line != null && !line.matches("(?i)\\s*\\{\\s*|\\s*\\}\\s*|\\s*expense_type\\s*"))
				{
					if (line.matches("(?i)\\s*type\\s+\\S+\\s*"))
					{
						if (hasUniqueType)
						{
							hasType = false;
						}
						else
						{
							hasType = hasUniqueType = true;
							type = line;
						}
					}
					else if (line.matches("(?i)\\s*expense_type_code\\s+(\\S+\\s*)+"))
					{
						if (hasUniqueCode)
						{
							hasCode = false;
						}
						else
						{
							hasCode = hasUniqueCode = true;
						}
					}
					else if (line.matches("(?i)\\s*expense_type_descr\\s+(\\S+\\s*)+"))
					{
						if (hasUniqueDescr)
						{
							hasDescr = false;
						}
						else
						{
							hasDescr = hasUniqueDescr = true;
						}
					}
					
					line = reader.readLine();
					logger.incrementLine();
				}
				
				if (line != null && line.matches("(?i)\\s*\\}\\s*|\\s*expense_type\\s*"))
				{
					if (hasCode && hasDescr && hasType)
					{
						if (type.matches("(?i)\\s*type\\s+waterexpense\\s*"))
						{
							reader.reset();
							expense = new WaterExpense().parseExpense(reader, logger);
						}
						else if (type.matches("(?i)\\s*type\\s+energyexpense\\s*"))
						{
							reader.reset();
							expense = new EnergyExpense().parseExpense(reader, logger);
						}
						else if (type.matches("(?i)\\s*type\\s+telephoneexpense\\s*"))
						{
							reader.reset();
							expense = new TelephoneExpense().parseExpense(reader, logger);
						}
						else if (type.matches("(?i)\\s*type\\s+rentexpense\\s*"))
						{
							reader.reset();
							expense = new RentExpense().parseExpense(reader, logger);
						}
						else if (type.matches("(?i)\\s*type\\s+cleaningexpense\\s*"))
						{
							reader.reset();
							expense = new CleaningExpense().parseExpense(reader, logger);
						}
						else
						{
							logger.appendMessage("Unknown type");
						}
					}
					
					if (line.matches("(?i)\\s*expense_type\\s*"))
					{
						reader.reset();
						logger.appendMessage("Missing closing bracket. Object added but with possible loss of data");
					}
				}
				else throw new IOException();
			}
			else if (line != null && line .matches("\\s*"))
			{
				line = reader.readLine();
				logger.incrementLine();
			}
			else throw new IOException();
		}
		catch (IOException e)
		{
			logger.appendMessage("Error reading file");
		}
		return expense;
	}
}