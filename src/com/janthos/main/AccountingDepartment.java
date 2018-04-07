package com.janthos.main;

import java.util.Scanner;

public class AccountingDepartment 
{
	private static Scanner keyboard = new Scanner(System.in);
	private static Company company;
	
	public static void main(String[] args) 
	{
		company = new Company("ABC Inc.");
		
		init();
		
		System.out.println("Company " + company.getBrandName() + " Building Expense");
		
		printMenu();
		int option = keyboard.nextInt();
		keyboard.nextLine();
		
		while (option != 7)
		{
			switch (option)
			{
				case 1:
					if (company.addBuilding(createBuildingFromUserInput())) System.out.println("Building added successfully.");
					else System.out.println("Addition failed.");
					break;
				case 2:
					if (company.addBuildingExpense(createBuildingExpenseFromUserInput())) System.out.println("Building expense added successfully.");
					else System.out.println("Addition failed.");
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
		
		System.out.println(building.toString());
		System.out.print("Add building Y/N? ");
		if (keyboard.nextLine().equals("N")) return null;
		
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
		
		System.out.println(expense.toString());
		System.out.print("Add expense Y/N? ");
		if (keyboard.nextLine().equals("N")) return null;
		
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
		if (expense == null) return null;
		
		double consumption = 0;
		
		if (expense instanceof VariableExpense) 
		{
			System.out.print("Enter consumption: ");
			consumption = keyboard.nextDouble();
			keyboard.nextLine();
		}
		
		buildingExpense = new BuildingExpense(building, expense, consumption);
		
		System.out.println(buildingExpense.toString());
		System.out.print("Add building expense Y/N? ");
		if (keyboard.nextLine().equals("N")) return null;
		
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
		System.out.println("The total cost for " +  expenseType.toString() + " is: " + company.calculateTotalCostOfExpense(expenseType) + " euros.");
	}
	
	public static void init()
	{
		company.addBuilding(new Building("B001", "University", "Athens A", 10, 400));
		company.addBuilding(new Building("B002", "Library", "Athens B", 5, 200));
		company.addBuilding(new Building("B003", "Prof Office", "Piraeus", 10, 50));
		company.addBuilding(new Building("B004", "Lab 1", "Thessaloniki A", 20, 100));
		company.addBuilding(new Building("B005", "Lab 2", "Thessaloniki B", 20, 150));
		
		company.addBuildingExpense(new BuildingExpense(company.getBuilding().get(0), new WaterExpense("W001", "EYDAP", 0.005, 0.008, 15), 40));
		company.addBuildingExpense(new BuildingExpense(company.getBuilding().get(1), new WaterExpense("W002", "EYDAP", 0.007, 0.01, 15), 15));
		company.addBuildingExpense(new BuildingExpense(company.getBuilding().get(2), new WaterExpense("W003", "EYDAP", 0.01, 0.015, 15), 5));
		company.addBuildingExpense(new BuildingExpense(company.getBuilding().get(3), new WaterExpense("W004", "EYDAP", 0.005, 0.008, 15), 120));
		
		company.addBuildingExpense(new BuildingExpense(company.getBuilding().get(0), new EnergyExpense("E001", "DEH", 0.005, 30, 15.5), 1600));
		company.addBuildingExpense(new BuildingExpense(company.getBuilding().get(1), new EnergyExpense("E002", "DEH", 0.008, 30, 15.5), 1000));
		company.addBuildingExpense(new BuildingExpense(company.getBuilding().get(3), new EnergyExpense("E003", "HRON", 0.007, 20, 15.5), 150));
		company.addBuildingExpense(new BuildingExpense(company.getBuilding().get(4), new EnergyExpense("E004", "HRON", 0.007, 20, 15.5), 200));
		
		company.addBuildingExpense(new BuildingExpense(company.getBuilding().get(1), new TelephoneExpense("T001", "Wind", 0.001, 18, 5.5), 600));
		company.addBuildingExpense(new BuildingExpense(company.getBuilding().get(2), new TelephoneExpense("T002", "OTE", 0.002, 30, 10), 800));
		company.addBuildingExpense(new BuildingExpense(company.getBuilding().get(3), new TelephoneExpense("T003", "Vodafone", 0.0015, 20, 10), 100));
		company.addBuildingExpense(new BuildingExpense(company.getBuilding().get(4), new TelephoneExpense("T004", "Vodafone", 0.0015, 20, 10), 300));
		
		company.addBuildingExpense(new BuildingExpense(company.getBuilding().get(0), new FixedExpense("R001", "Rent description 1", 0.2, ExpenseType.Rent), 0));
		company.addBuildingExpense(new BuildingExpense(company.getBuilding().get(2), new FixedExpense("R001", "Rent description 2", 0.2, ExpenseType.Rent), 0));
		company.addBuildingExpense(new BuildingExpense(company.getBuilding().get(3), new FixedExpense("R001", "Rent description 3", 0.25, ExpenseType.Rent), 0));
		company.addBuildingExpense(new BuildingExpense(company.getBuilding().get(4), new FixedExpense("R001", "Rent description 4", 0.5, ExpenseType.Rent), 0));
		
		company.addBuildingExpense(new BuildingExpense(company.getBuilding().get(0), new FixedExpense("C001", "Cleaning description 1", 0.2, ExpenseType.Cleaning), 0));
		company.addBuildingExpense(new BuildingExpense(company.getBuilding().get(1), new FixedExpense("C002", "Cleaning description 2", 0.15, ExpenseType.Cleaning), 0));
		company.addBuildingExpense(new BuildingExpense(company.getBuilding().get(2), new FixedExpense("C003", "Cleaning description 3", 0.3, ExpenseType.Cleaning), 0));
		company.addBuildingExpense(new BuildingExpense(company.getBuilding().get(4), new FixedExpense("C004", "Cleaning description 4", 0.5, ExpenseType.Cleaning), 0));

	}
}
