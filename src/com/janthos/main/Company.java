package com.janthos.main;

import java.util.ArrayList;

public class Company 
{
	
	private String brandName;
	private ArrayList<Building> building;
	private ArrayList<Expense> expense;
	private ArrayList<BuildingExpense> buildingExpense;
	
	public Company()
	{
		building = new ArrayList<Building>();
		expense = new ArrayList<Expense>();
		buildingExpense = new ArrayList<BuildingExpense>();
	}
	
	public Company(String brandName)
	{
		this.brandName = brandName;
		building = new ArrayList<Building>();
		expense = new ArrayList<Expense>();
		buildingExpense = new ArrayList<BuildingExpense>();
	}
	
	public String getBrandName()
	{
		return brandName;
	}
	
	public ArrayList<Building> getBuilding()
	{
		return building;
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
	
	public boolean addBuildingExpense(BuildingExpense buildingExpense)
	{
		if (buildingExpense == null || buildingExpense.getExpense() == null || buildingExpense.getBuilding() == null) return false;
		
		this.buildingExpense.add(buildingExpense);
		this.expense.add(buildingExpense.getExpense());
		return true;
	}
	
	public ArrayList<Expense> getBuildingExpense(Building building)
	{
		ArrayList<Expense> expense = new ArrayList<Expense>();
		
		for (BuildingExpense buildExp : buildingExpense)
		{
			if (buildExp.getBuilding() == building) expense.add(buildExp.getExpense());
		}
		
		return expense;
	}
	
	public double caclulateBuildingExpense(Building building)
	{
		double totalExpense = 0;
		
		for (BuildingExpense buildExp : buildingExpense)
		{
			if (buildExp.getBuilding() == building) totalExpense += buildExp.calculateCost();
		}
		
		return totalExpense;
	}
	
	public double calculateTotalCostOfExpense(ExpenseType expenseType)
	{
		double totalCost = 0;
		
		for (BuildingExpense bExp: buildingExpense)
		{
			if (bExp.getExpense().getExpenseType() == expenseType)
			{
				totalCost += bExp.calculateCost();
			}
		}
		
		return totalCost;
	}
	
	public boolean checkExpenseCodeAvailability(String code)
	{
		for (Expense exp : expense)
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
		for (Building build : building)
		{
			if (build.getCode().equals(code)) return build;
		}
		return null;
	}
}