package com.janthos.main;

public abstract class VariableExpense extends Expense
{
	private double pricePerUnit;
	private String unit;
	private double fixedCost;
	private double costCoefficient;
	
	public VariableExpense() {}
	
	public VariableExpense(String code, String description, double pricePerUnit, String unit, double fixedCost, ExpenseType expenseType)
	{
		super(code, description, expenseType);
		this.pricePerUnit = pricePerUnit;
		this.unit = unit;
		this.fixedCost = fixedCost;
	}
	
	public VariableExpense(VariableExpense variableExpense)
	{
		super(variableExpense);
		this.pricePerUnit = variableExpense.pricePerUnit;
		this.unit = variableExpense.unit;
		this.fixedCost = variableExpense.fixedCost;
	}
	
	public double getPricePerUnit()
	{
		return pricePerUnit;
	}
	
	public String getUnit()
	{
		return unit;
	}
	
	public double getFixedCost()
	{
		return fixedCost;
	}
	
	public void setPricePerUnit(double pricePerUnit)
	{
		this.pricePerUnit = pricePerUnit;
	}
	
	public void setUnit(String unit)
	{
		this.unit = unit;
	}
	
	public void setFixedCost(double fixedCost)
	{
		this.fixedCost = fixedCost;
	}
	
	public String toString()
	{
		return super.toString() + pricePerUnit + " " + unit + " " + fixedCost + " " + costCoefficient; 
	}
}
