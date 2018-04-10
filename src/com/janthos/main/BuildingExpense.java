package com.janthos.main;

public class BuildingExpense 
{
	private Building building;
	private Expense expense;
	private double consumption;
	
	public BuildingExpense() {}
	
	public BuildingExpense(Building building, Expense expense, double consumption)
	{
		this.building = building;
		this.expense = expense;
		this.consumption = consumption;
	}
	
	public Building getBuilding()
	{
		return building;
	}
	
	public Expense getExpense()
	{
		return expense;
	}
	
	public double getConsumption()
	{
		return consumption;
	}
	
	public void setBuilding(Building building)
	{
		this.building = building;
	}
	
	public void setExpense(Expense expense)
	{
		this.expense = expense;
	}
	
	public void setConsumption(double consumption)
	{
		this.consumption = consumption;
	}
	
	public double calculateCost()
	{
		return expense.calculateExpense(this);
	}
}
