package com.janthos.main;

public abstract class FixedExpense extends Expense<FixedBuildingExpense>
{
	private double pricePerSquareMeter;
	
	public FixedExpense() {}
	
	public FixedExpense(String code, String description, double pricePerSquareMeter)
	{
		super(code, description);
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
}
