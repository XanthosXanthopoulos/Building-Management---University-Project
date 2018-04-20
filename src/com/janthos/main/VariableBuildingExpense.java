package com.janthos.main;

public class VariableBuildingExpense extends BuildingExpense<VariableBuildingExpense>
{
	private double consumption;
	
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
}
