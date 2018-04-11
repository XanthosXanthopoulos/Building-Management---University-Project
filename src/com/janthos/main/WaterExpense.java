package com.janthos.main;

public class WaterExpense extends VariableExpense
{	
	private double pricePerUnit_2;
	
	public WaterExpense() {}
	
	public WaterExpense(String code, String description, double pricePerUnit, double pricePerUnit_2, double fixedCost)
	{
		super(code, description, pricePerUnit, "m^3", fixedCost);
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
		return super.toString() + String.format("Cost per %-5s (>100):  %.3f", getUnit(), pricePerUnit_2);
	}
}
