package com.janthos.main;

public class TelephoneExpense extends VariableExpense
{	
	private double telephoneCharges;
	
	public TelephoneExpense() {}
	
	public TelephoneExpense(String code, String description, double pricePerUnit, double fixedCost, double telephoneCharges)
	{
		super(code, description, pricePerUnit, "min", fixedCost, ExpenseType.Telephone);
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

	public double calculateExpense(double[] costParameter) 
	{
		return getFixedCost() + telephoneCharges + getPricePerUnit() * costParameter[2];
	}
	
	public String toString()
	{
		return super.toString() + String.format("Monthly telephone charges: %.3f", telephoneCharges);
	}
}
