package com.janthos.main;

public class EnergyExpense extends VariableExpense
{	
	private double monthlyERTCost;
	
	public EnergyExpense() {}
	
	public EnergyExpense(String code, String description, double pricePerUnit, double fixedCost, double monthlyERTCost)
	{
		super(code, description, pricePerUnit, "kWh", fixedCost);
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

	public double calculateExpense(VariableBuildingExpense buildingExpense) 
	{
		return getFixedCost() + monthlyERTCost + buildingExpense.getBuilding().getZoneValue() * buildingExpense.getBuilding().getArea() + getPricePerUnit() * buildingExpense.getConsumption();
	}
	
	public String toString()
	{
		return super.toString() + String.format("%5s ERT monthly cost: %.3f", "", monthlyERTCost);
	}
}