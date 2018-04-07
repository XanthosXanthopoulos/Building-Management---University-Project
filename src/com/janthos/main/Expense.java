package com.janthos.main;

public abstract class Expense 
{
	private String code;
	private String description;
	private ExpenseType expenseType;
	
	public Expense() {}
	
	public Expense(String code, String description, ExpenseType expenseType)
	{
		this.code = code;
		this.description = description;
		this.expenseType = expenseType;
	}
	
	public Expense(Expense expense)
	{
		this.code = expense.code;
		this.description = expense.description;
		this.expenseType = expense.expenseType;
	}
	
	public String getCode()
	{
		return code;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public ExpenseType getExpenseType()
	{
		return expenseType;
	}
	
	public void setCode(String code)
	{
		this.code = code;
	}
	
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	public void setExpenseType(ExpenseType expenseType)
	{
		this.expenseType = expenseType;
	}
	
	public String toString()
	{
		return String.format("%-10s", expenseType.toString()) + String.format("%-8s", code) + String.format("%-30s", description);
	}
	
	public abstract double calculateExpense(double[] costParameter);
}
