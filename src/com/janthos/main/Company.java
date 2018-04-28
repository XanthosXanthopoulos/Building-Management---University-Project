package com.janthos.main;

import java.util.ArrayList;

public class Company 
{
	
	private String brandName;
	private ArrayList<Building> building;
	private ArrayList<Expense<?>> expense;
	private ArrayList<BuildingExpense<?>> buildingExpense;
	
	public Company()
	{
		building = new ArrayList<Building>();
		expense = new ArrayList<Expense<?>>();
		buildingExpense = new ArrayList<BuildingExpense<?>>();
	}
	
	public Company(String brandName)
	{
		this.brandName = brandName;
		building = new ArrayList<Building>();
		expense = new ArrayList<Expense<?>>();
		buildingExpense = new ArrayList<BuildingExpense<?>>();
	}
	
	public String getBrandName()
	{
		return brandName;
	}
	
	public ArrayList<Building> getBuilding()
	{
		return building;
	}
	
	public ArrayList<Expense<?>> getExpense()
	{
		return expense;
	}
	
 	public void setBrandName(String brandName)
	{
		this.brandName = brandName;
	}
	
	public boolean addBuilding(Building building)
	{
		if (building == null) return false;
		this.building.add(building);
		return true;
	}
	
	public boolean addExpense(Expense<?> expense) 
	{
		if (expense == null) return false;
		this.expense.add(expense);
		return true;
	}
	
	public boolean addBuildingExpense(BuildingExpense<?> buildingExpense)
	{
		if (buildingExpense == null || buildingExpense.getExpense() == null || buildingExpense.getBuilding() == null) return false;
		this.buildingExpense.add(buildingExpense);
		return true;
	}
	
	public ArrayList<Expense<?>> getBuildingExpense(Building building)
	{
		ArrayList<Expense<?>> expense = new ArrayList<Expense<?>>();
		
		for (BuildingExpense<?> buildExp : buildingExpense)
		{
			if (buildExp.getBuilding() == building) expense.add(buildExp.getExpense());
		}
		
		return expense;
	}
	
	public double calculateBuildingExpense(Building building)
	{
		if (building == null) return -1;
		
		double totalExpense = 0;
		
		for (BuildingExpense<?> buildExp : buildingExpense)
		{
			if (buildExp.getBuilding() == building) totalExpense += buildExp.calculateCost();
		}
		
		return totalExpense;
	}
	
	public double calculateTotalCostOfExpense(Expense<?> expense)
	{
		if (expense == null) return -1;
		
		double totalCost = 0;
		
		for (BuildingExpense<?> bExp: buildingExpense)
		{
			if (bExp.getExpense() == expense)
			{
				totalCost += bExp.calculateCost();
			}
		}
		
		return totalCost;
	}
	
	public boolean checkExpenseCodeAvailability(String code) //Will be used when we want to add the expenses from a file to check if duplicate codes exist
	{
		for (Expense<? extends BuildingExpense<?>> exp : expense)
		{
			if (exp.getCode().equals(code)) return false;
		}
		return true;
	}
	
	public boolean checkBuildingCodeAvailability(String code)
	{
		for (Building build : building) 
		{
			if (build.getCode().equals(code)) return false;
		}
		return true;
	}
	
	public Building getBuildingByCode(String code)
	{
		for (Building building : this.building)
		{
			if (building.getCode().equals(code)) return building;
		}
		return null;
	}
	
	public Expense<?> getExpenseByCode(String code)
	{
		for (Expense<?> expense : this.expense)
		{
			if (expense.getCode().equals(code)) return expense;
		}
		return null;
	}
	
	public ArrayList<Expense<?>> getAvailableExpense(Building building)
	{
		ArrayList<Expense<?>> expense = new ArrayList<Expense<?>>(this.expense);
		
		for (BuildingExpense<?> buildingExpense : this.buildingExpense)
		{
			if (buildingExpense.getBuilding() == building) expense.remove(buildingExpense.getExpense());
		}
		
		return expense;
	}
	
	public Expense<?> getAvailableExpenseByCode(Building building, String code)
	{
		ArrayList<Expense<?>> availableExpense = getAvailableExpense(building);
		
		for (Expense<?> expense : availableExpense)
		{
			if (expense.getCode().equals(code)) return expense;
		}
		return null;
	}
}