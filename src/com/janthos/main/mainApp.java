package com.janthos.main;

public class mainApp
{
	private static Company company;
	
	public static void main(String[] args) 
	{		
		company = new Company("ABC Inc.");
		new MainForm(company);
	}
}