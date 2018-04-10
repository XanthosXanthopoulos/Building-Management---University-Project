package com.janthos.main;

public class CleaningExpense extends FixedExpense
{
	public CleaningExpense() {}
	
	public CleaningExpense(String code, String description, double pricePerSquareMeter)
	{
		super(code, description, pricePerSquareMeter);
	}
	
	public CleaningExpense(CleaningExpense cleaningExpense)
	{
		super(cleaningExpense);
	}
	
	
	public double calculateExpense(BuildingExpense buildingExpense) 
	{
		return getPricePerSquareMeter() * buildingExpense.getBuilding().getArea();
	}
}
