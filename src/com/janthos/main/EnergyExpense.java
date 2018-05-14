package com.janthos.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class EnergyExpense extends VariableExpense
{	
	private double monthlyERTCost;
	
	public EnergyExpense() {}
	
	public EnergyExpense(String code, String description, String unit, double pricePerUnit, double fixedCost, double monthlyERTCost)
	{
		super(code, description, pricePerUnit, unit, fixedCost);
		this.monthlyERTCost = monthlyERTCost;
	}
	
	public EnergyExpense(EnergyExpense energyExpense)
	{
		super(energyExpense);
		this.monthlyERTCost = energyExpense.monthlyERTCost;
	}

	public double getMonthlyERTCost() 
	{
		return monthlyERTCost;
	}

	public void setMonthlyERTCost(double monthlyERTCost) 
	{
		this.monthlyERTCost = monthlyERTCost;
	}

	public double calculateExpense(VariableBuildingExpense buildingExpense) 
	{
		return getFixedCost() + monthlyERTCost + buildingExpense.getBuilding().getZoneValue() * buildingExpense.getBuilding().getArea() + getPricePerUnit() * buildingExpense.getConsumption();
	}
	
	public String toString()
	{
		return super.toString() + String.format("%5s ERT monthly cost: %.3f", "", monthlyERTCost);
	}

	@Override
	public Expense<VariableBuildingExpense> parseExpense(BufferedReader reader, Logger logger) 
	{
		if (reader == null) return null;
		
		EnergyExpense expense = new EnergyExpense();
		String line = null;
		
		expense.setUnit("kWh");
		expense.setPricePerUnit(0.007);
		expense.setMonthlyERTCost(20);
		expense.setFixedCost(40);
		
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
				else if (line.matches("(?i)\\s*monthly_ert_cost\\s+\\S+\\s*"))
				{
					try
					{
						expense.setMonthlyERTCost(Double.parseDouble(line.trim().substring(17).trim()));
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
		writer.println("\t\tTYPE Energy");
		writer.println("\t\tUNIT " + getUnit());
		writer.println("\t\tPRICE_PER_UNIT " + getPricePerUnit());
		writer.println("\t\tFIXED_COST " + getFixedCost());
		writer.println("\t\tMONTHLY_ERT_COST " + getMonthlyERTCost());
		writer.println("\t}");
	}
}