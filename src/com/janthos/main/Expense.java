package com.janthos.main;

public abstract class Expense <T extends BuildingExpense<T>>
{
	private String code;
	private String description;
	
	public Expense() {}
	
	public Expense(String code, String description)
	{
		this.code = code;
		this.description = description;
	}
	
	public Expense(Expense<T> expense)
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
		return String.format("%-8s", code) + String.format("%-20s", description);
	}
	
	public abstract double calculateExpense(T buildingExpense);
}