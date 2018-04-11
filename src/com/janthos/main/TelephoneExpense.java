package com.janthos.main;

public class TelephoneExpense extends VariableExpense
{	
	private double telephoneCharges;
	
	public TelephoneExpense() {}
	
	public TelephoneExpense(String code, String description, double pricePerUnit, double fixedCost, double telephoneCharges)
	{
		super(code, description, pricePerUnit, "min", fixedCost);
		this.telephoneCharges = telephoneCharges;
	}
	
	public TelephoneExpense(TelephoneExpense telephoneExpense)
	{
		super(telephoneExpense);
		this.telephoneCharges = telephoneExpense.telephoneCharges;
	}

	public double getTelephoneCharges() 
	{
		return telephoneCharges;
	}

	public void setTelephoneCharges(double telephoneCharges) 
	{
		this.telephoneCharges = telephoneCharges;
	}

	public double calculateExpense(VariableBuildingExpense buildingExpense) 
	{
		return getFixedCost() + telephoneCharges + getPricePerUnit() * buildingExpense.getConsumption();
	}
	
	public String toString()
	{
		return super.toString() + String.format("Monthly telephone charges: %.3f", telephoneCharges);
	}
}
