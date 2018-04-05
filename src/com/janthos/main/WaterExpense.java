package com.janthos.main;

public class WaterExpense extends VariableExpense
{	
	private double pricePerUnit_2;
	
	public WaterExpense() {}
	
	public WaterExpense(String code, String description, double pricePerUnit, double pricePerUnit_2, double fixedCost)
	{
		super(code, description, pricePerUnit, "Cubic Meters", fixedCost, ExpenseType.Water);
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
	
	public double calculateExpense(double[] costParameter) 
	{
		if (costParameter[2] <= 100)
		{
			return getFixedCost() + getPricePerUnit() * costParameter[2];
		}
		else
		{
			return getFixedCost() + pricePerUnit_2 * costParameter[2];
		}
	}
}
