package com.janthos.main;

import java.util.Scanner;

public class AccountingDepartment 
{
	private static Scanner keyboard = new Scanner(System.in);
	private static Company company;
	
	public static void main(String[] args) 
	{
		company = new Company("ABC Inc.");
		
		
		System.out.println("Company ABC Inc. Building Expense");
		printMenu();
		
		int option = keyboard.nextInt();
		keyboard.nextLine();
		
		while (option != 7)
		{
			switch (option)
			{
				case 1:
					if (company.addBuilding(createBuildingFromUserInput())) System.out.println("Building added successfully.");
					else System.out.println("An error occured.");
					break;
				case 2:
					if (company.addBuildingExpense(createBuildingExpenseFromUserInput())) System.out.println("Building expense added successfully.");
					else System.out.println("An error occured.");
					break;
				case 3:
					showBuildings(company);
					break;
				case 4:
					showBuildingExpense(company);
					break;
				case 5:
					showBuildingTotalExpense(company);
					break;
				case 6:
					showExpenseTotalCost(company);
					break;
				case 7:
					break;
				default:
					System.out.println("Error: Invalid option.");
			}
			printMenu();
			option = keyboard.nextInt();
			keyboard.nextLine();
		}
	}
	
	public static void printMenu()
	{
		System.out.println();
		System.out.println("Choose an action:");
		System.out.println("1: Add new building");
		System.out.println("2: Add new building expense");
		System.out.println("3: Show all buildings");
		System.out.println("4: Show expenses for a specific building");
		System.out.println("5: Calculate cost for a building");
		System.out.println("6: Calculate total cost for specific expense");
		System.out.println("7: Exit");
		System.out.println();
		System.out.print("Action: ");
	}
	
	public static Building createBuildingFromUserInput()
	{
		Building building = null;
		
		System.out.print("Enter building code: ");
		String code = keyboard.nextLine();
		while (!company.checkBuildingCodeAvailability(code))
		{
			System.out.print("Code already used. Try another one. Building code: ");
			code = keyboard.nextLine();
		}
		System.out.print("Enter building description: ");
		String description = keyboard.nextLine();
		System.out.print("Enter building address: ");
		String address = keyboard.nextLine();
		System.out.print("Enter building zone value: ");
		double zoneValue = keyboard.nextDouble();
		keyboard.nextLine();
		System.out.print("Enter building square meters: ");
		double area = keyboard.nextDouble();
		keyboard.nextLine();
		
		building = new Building(code, description, address, zoneValue, area);
		
		return building;
	}
	
	public static Expense createExpenseFromUserInput()
	{	
		Expense expense = null;
		
		System.out.println("Expense Type");
		System.out.println("1) Water");
		System.out.println("2) Telephone");
		System.out.println("3) Energy");
		System.out.println("4) Rent");
		System.out.println("5) Cleaning");
		System.out.print("Choose an expense type by number: ");
		int type = keyboard.nextInt();
		keyboard.nextLine();
		
		while (type < 1 || type > 5)
		{
			System.out.print("Invalid expense type try another one. Expense type: ");
			type = keyboard.nextInt();
			keyboard.nextLine();
		}
		
		System.out.print("Enter unique code: ");
		String code = keyboard.nextLine();
		while (!company.checkExpenseCodeAvailability(code))
		{
			System.out.print("Code already used. Try another one. Expense code: ");
			code = keyboard.nextLine();
		}
		System.out.print("Enter description: ");
		String description = keyboard.nextLine();
		
		switch (type)
		{
			case 1:
			{
				System.out.print("Enter price per cubic meter (<=100): ");
				double price1 = keyboard.nextDouble();
				keyboard.nextLine();
				System.out.print("Enter price per cubic meter (>100): ");
				double price2 = keyboard.nextDouble();
				keyboard.nextLine();
				System.out.print("Enter fixed cost: ");
				double fixedCost = keyboard.nextDouble();
				keyboard.nextLine();
				expense = new WaterExpense(code, description, price1, price2, fixedCost);
				break;
			}
			case 2:
			{
				System.out.print("Enter price per minute: ");
				double pricePerMinute = keyboard.nextDouble();
				keyboard.nextLine();
				System.out.print("Enter fixed cost: ");
				double fixedCost = keyboard.nextDouble();
				keyboard.nextLine();
				System.out.print("Enter telephone charges: ");
				double telephoneCharges = keyboard.nextDouble();
				keyboard.nextLine();
				expense = new TelephoneExpense(code, description, pricePerMinute, fixedCost, telephoneCharges);
				break;
			}
			case 3:
			{
				System.out.print("Enter price per kWh: ");
				double pricePerKWh = keyboard.nextDouble();
				keyboard.nextLine();
				System.out.print("Enter fixed cost: ");
				double fixedCost = keyboard.nextDouble();
				keyboard.nextLine();
				System.out.print("Enter ERT cost: ");
				double monthlyERTCost = keyboard.nextDouble();
				keyboard.nextLine();
				expense = new EnergyExpense(code, description, pricePerKWh, fixedCost, monthlyERTCost);
				break;
			}
			case 4:
			{
				System.out.print("Enter price per square meter: ");
				double pricePerSquareMeter = keyboard.nextDouble();
				keyboard.nextLine();
				expense = new FixedExpense(code, description, pricePerSquareMeter, ExpenseType.Rent);
				break;
			}
			case 5:
			{
				System.out.print("Enter price per square meter: ");
				double pricePerSquareMeter = keyboard.nextDouble();
				keyboard.nextLine();
				expense = new FixedExpense(code, description, pricePerSquareMeter, ExpenseType.Cleaning);
				break;
			}
		}
		
		return expense;
	}
	
	public static BuildingExpense createBuildingExpenseFromUserInput()
	{
		BuildingExpense buildingExpense = null;
		
		System.out.print("Enter building code: ");
		String code = keyboard.nextLine();
		Building building = company.getBuildingByCode(code);
		while(building == null)
		{
			System.out.print("Code doesn't exist try another one. Building code: ");
			code = keyboard.nextLine();
			building = company.getBuildingByCode(code);
		}
		
		Expense expense = createExpenseFromUserInput();
		double consumption = 0;
		
		if (expense instanceof VariableExpense) 
		{
			System.out.print("Enter consumption: ");
			consumption = keyboard.nextDouble();
			keyboard.nextLine();
		}
		
		buildingExpense = new BuildingExpense(building, expense, consumption);
		
		return buildingExpense;
	}
	
	public static void showBuildings(Company company)
	{
		for (Building building : company.getBuilding())
		{
			System.out.println(building.toString());
		}
	}
	
	public static void showBuildingExpense(Company company)
	{
		System.out.print("Enter building code: ");
		String code = keyboard.nextLine();
		Building building = company.getBuildingByCode(code);
		while(building == null)
		{
			System.out.print("Code doesn't exist try another one. Building code: ");
			code = keyboard.nextLine();
			building = company.getBuildingByCode(code);
		}
		
		for (Expense expense : company.getBuildingExpense(building))
		{
			System.out.println(expense.toString());
		}
	}
	
	public static void showBuildingTotalExpense(Company company)
	{
		showBuildings(company);
		
		System.out.print("Enter building code: ");
		String code = keyboard.nextLine();
		Building building = company.getBuildingByCode(code);
		while(building == null)
		{
			System.out.print("Code doesn't exist try another one. Building code: ");
			code = keyboard.nextLine();
			building = company.getBuildingByCode(code);
		}
		
		System.out.println("The total expense of " + building.getCode() + " is: " + company.caclulateBuildingExpense(building) + " euros.");
	}
	
	public static void showExpenseTotalCost(Company company)
	{
		for (ExpenseType expenseType : ExpenseType.values())
		{
			System.out.println((expenseType.ordinal() + 1) + ") " + expenseType.toString());
		}
		
		System.out.print("Choose an expense type by number: ");
		int type = keyboard.nextInt();
		keyboard.nextLine();
		
		while (type < 1 || type > 5)
		{
			System.out.print("Invalid expense type try another one. Expense type: ");
			type = keyboard.nextInt();
			keyboard.nextLine();
		}
		ExpenseType expenseType = ExpenseType.values()[type - 1];
		System.out.println("The total cost of " +  expenseType.toString() + " is: " + company.calculateTotalCostOfExpense(expenseType));
	}
}
