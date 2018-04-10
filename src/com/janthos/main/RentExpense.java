package com.janthos.main;

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
	
	public double calculateExpense(BuildingExpense buildingExpense) 
	{
		return getPricePerSquareMeter() * buildingExpense.getBuilding().getArea();
	}
}
