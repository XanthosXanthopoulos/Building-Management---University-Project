package com.janthos.main;

public abstract class BuildingExpense<T extends BuildingExpense<T>>
{
	private Building building;
	private Expense<T> expense;
	
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
	
	public abstract double calculateCost();
}