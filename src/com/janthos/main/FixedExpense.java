package com.janthos.main;

public class FixedExpense extends Expense
{
	private double pricePerSquareMeter;
	
	public FixedExpense() {}
	
	public FixedExpense(String code, String description, double pricePerSquareMeter, ExpenseType expenseType)
	{
		super(code, description, expenseType);
		this.pricePerSquareMeter = pricePerSquareMeter;
	}
	
	public FixedExpense(FixedExpense fixedExpense)
	{
		super(fixedExpense);
		this.pricePerSquareMeter = fixedExpense.pricePerSquareMeter;
	}
	
	public double getPricePerSquareMeter()
	{
		return pricePerSquareMeter;
	}
	
	public void setPricePerSquareMeter(double pricePerSquareMeter)
	{
		this.pricePerSquareMeter = pricePerSquareMeter;
	}

	public String toString()
	{
		return super.toString() + String.format("%.3f per m^2", pricePerSquareMeter);
	}
	
	public double calculateExpense(BuildingExpense buildingExpense) 
	{
		return pricePerSquareMeter * buildingExpense.getBuilding().getArea();
	}
}
