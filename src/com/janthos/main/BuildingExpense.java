package com.janthos.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public abstract class BuildingExpense<T extends BuildingExpense<T>>
{
	private Building building;
	private Expense<T> expense;
	
	public BuildingExpense() { }
	
	public BuildingExpense(Building building, Expense<T> expense)
	{
		this.building = building;
		this.expense = expense;
	}
	
	public Building getBuilding()
	{
		return building;
	}
	
	public Expense<T> getExpense()
	{
		return expense;
	}
	
	public void setBuilding(Building building)
	{
		this.building = building;
	}
	
	public void setExpense(Expense<T> expense)
	{
		this.expense = expense;
	}
	
	public String toString()
	{
		return getExpense().toString();
	}
	
	public abstract double calculateCost();
	
	public abstract T parseBuildingExpense(BufferedReader reader, Expense<T> expense, Building building, Logger logger);
	
	public abstract void printBuildingExpense(PrintWriter writer);
	
	public static ArrayList<BuildingExpense<?>> parse(BufferedReader reader, Building building, ArrayList<Expense<?>> expenses, Logger logger)
	{
		if (reader == null) return null;
		
		ArrayList<BuildingExpense<?>> buildingExpense = new ArrayList<BuildingExpense<?>>();
		String line = null, code = null;
		boolean hasCode, hasUniqueCode;
		
		try
		{
			line = reader.readLine();
			logger.incrementLine();
			if (line != null && line.matches("(?i)\\s*\\{\\s*"))
			{
				line = reader.readLine();
				logger.incrementLine();
				while (line != null && !line.matches("(?i)\\s*\\{\\s*|\\s*\\}\\s*|\\s*expenses\\s*"))
				{
					if (line != null && line.matches("(?i)\\s*expense\\s*"))
					{
						line = reader.readLine();
						logger.incrementLine();
						if (line != null && line.matches("(?i)\\s*\\{\\s*"))
						{
							reader.mark(8192);
							line = reader.readLine();
							logger.incrementLine();
							hasCode = hasUniqueCode = false;
							while (line != null && !line.matches("(?i)\\s*\\{\\s*|\\s*\\}\\s*|\\s*expense\\s*"))
							{
								if (line != null && line.matches("(?i)\\s*expense_type_code\\s+(\\S+\\s*)+"))
								{
									if (hasUniqueCode)
									{
										hasCode = false;
									}
									else
									{
										hasCode = hasUniqueCode = true;
										code = line.trim().substring(18).trim();
									}
								}
								
								line = reader.readLine();
								logger.incrementLine();
							}
							
							if (line != null && line.matches("(?i)\\s*\\}\\s*|\\s*expense\\s*"))
							{
								if (hasCode)
								{
									Expense<?> expense = null;
									for (Expense<?> exp : expenses)
									{
										if (exp.getCode().equals(code)) expense = exp;
									}
									for (BuildingExpense<?> buildExp : buildingExpense)
									{
										if (buildExp.getExpense() == expense) expense = null;
									}
									if (expense != null)
									{
										reader.reset();
										if (expense instanceof FixedExpense)
										{
											FixedBuildingExpense fixedBuildingExpense = new FixedBuildingExpense().parseBuildingExpense(reader, (FixedExpense)expense, building, logger);
											if (fixedBuildingExpense != null)
											{
												buildingExpense.add(fixedBuildingExpense);
											}
										}
										else if (expense instanceof VariableExpense)
										{
											VariableBuildingExpense variableBuildingExpense = new VariableBuildingExpense().parseBuildingExpense(reader, (VariableExpense)expense, building, logger);
											if (variableBuildingExpense != null)
											{
												buildingExpense.add(variableBuildingExpense);
											}
										}
									}
								}
								if (line != null && line.matches("(?i)\\s*\\}\\s*"))
								{
									line = reader.readLine();
									logger.incrementLine();
								}
								else if (line != null && line.matches("(?i)\\s*expense\\s*"))
								{
									//reader.reset();
									System.out.println("Missing closing bracket. Object added but with possible loss of data.");
								}
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
					else if (line != null && line .matches("\\s*"))
					{
						line = reader.readLine();
						logger.incrementLine();
					}
					else throw new IOException();
				}
			}
			else if (line != null && line.matches("\\s*"))
			{
				line = reader.readLine();
				logger.incrementLine();
			}
			else throw new IOException();
			
		}
		catch (IOException e)
		{
			System.out.println("Error reading file");
		}
		
		return buildingExpense;
	}
	
}