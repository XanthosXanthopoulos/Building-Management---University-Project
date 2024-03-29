package com.janthos.main;

public abstract class VariableExpense extends Expense<VariableBuildingExpense>
{
	private double pricePerUnit;
	private String unit;
	private double fixedCost;
	
	public VariableExpense() {}
	
	public VariableExpense(String code, String description, double pricePerUnit, String unit, double fixedCost)
	{
		super(code, description);
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
		return super.toString() + String.format("%.3f", pricePerUnit) + String.format(" per %-5s ", unit) + String.format("%.3f ", fixedCost);
	}
}