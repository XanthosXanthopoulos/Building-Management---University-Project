package com.janthos.main;

public class FixedBuildingExpense extends BuildingExpense<FixedBuildingExpense>
{	
	public FixedBuildingExpense(Building building, FixedExpense expense)
	{
		super(building, expense);
	}
	
	public double calculateCost() 
	{
		return getExpense().calculateExpense(this);
	}
}
