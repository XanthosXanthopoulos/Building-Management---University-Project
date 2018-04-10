package com.janthos.main;

public abstract class Expense 
{
	private String code;
	private String description;
	
	public Expense() {}
	
	public Expense(String code, String description)
	{
		this.code = code;
		this.description = description;
	}
	
	public Expense(Expense expense)
	{
		this.code = expense.code;
		this.description = expense.description;
	}
	
	public String getCode()
	{
		return code;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public void setCode(String code)
	{
		this.code = code;
	}
	
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	public String toString()
	{
		return String.format("%-10s", this.getClass().toString().substring(0, this.getClass().toString().length() - 6)) + String.format("%-8s", code) + String.format("%-30s", description);
	}
	
	public abstract double calculateExpense(BuildingExpense buildingExpense);
}
