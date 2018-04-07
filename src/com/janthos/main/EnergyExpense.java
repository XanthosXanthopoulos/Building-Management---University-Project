package com.janthos.main;

public class EnergyExpense extends VariableExpense
{	
	private double monthlyERTCost;
	
	public EnergyExpense() {}
	
	public EnergyExpense(String code, String description, double pricePerUnit, double fixedCost, double monthlyERTCost)
	{
		super(code, description, pricePerUnit, "kWh", fixedCost, ExpenseType.Energy);
		this.monthlyERTCost = monthlyERTCost;
	}
	
	public EnergyExpense(EnergyExpense energyExpense)
	{
		super(energyExpense);
		this.monthlyERTCost = energyExpense.monthlyERTCost;
	}

	public double getMonthlyERTCost() 
	{
		return monthlyERTCost;
	}

	public void setMonthlyERTCost(double monthlyERTCost) 
	{
		this.monthlyERTCost = monthlyERTCost;
	}

	public double calculateExpense(double[] costParameter) 
	{
		return getFixedCost() + monthlyERTCost + costParameter[0] * costParameter[1] + getPricePerUnit() * costParameter[2];
	}
	
	public String toString()
	{
		return super.toString() + String.format("ERT monthly cost: %.3f", monthlyERTCost);
	}
}
