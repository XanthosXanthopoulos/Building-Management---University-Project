package com.janthos.main;

import java.util.Scanner;

public class mainApp
{
	private static Scanner keyboard = new Scanner(System.in);
	private static Company company;
	
	public static void main(String[] args) 
	{
		company = new Company("ABC Inc.");
		
		init();
		
		System.out.println("Company " + company.getBrandName() + " Building Expense");
		
		printMenu();
		int option = ValidInt(1, 7, "Action: ");
		
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
					showBuildings();
					break;
				case 4:
					showBuildingExpense();
					break;
				case 5:
					showBuildingTotalExpense();
					break;
				case 6:
					showExpenseTotalCost();
					break;
				default:
					System.out.println("Error: Invalid option.");
			}
			printMenu();
			option = ValidInt(1, 7, "Action: ");
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
	
	public static Building createBuildingFromUserInput() //Creates an instance of Building based on user input
	{
		Building building = null;

		System.out.print("Enter building code: ");
		String code = keyboard.nextLine();
		while (!company.checkBuildingCodeAvailability(code) || code.equals(""))
		{
			System.out.print("Code already used or is invalid. Try another one. Building code: ");
			code = keyboard.nextLine();
		}
		
		System.out.print("Enter building description: ");
		String description = keyboard.nextLine();
		System.out.print("Enter building address: ");
		String address = keyboard.nextLine();
		System.out.print("Enter building zone value: ");
		double zoneValue = validDouble(0, "Enter building zone value: ");
		System.out.print("Enter building square meters: ");
		double area = validDouble(0, "Enter building square meters: ");
		
		building = new Building(code, description, address, zoneValue, area);
		
		System.out.println(building.toString());
		System.out.print("Add building (Anything other than Y or y cancels addition)? ");
		if (!keyboard.nextLine().equalsIgnoreCase("y")) return null;
		
		return building;
	}
	
	public static BuildingExpense<?> createBuildingExpenseFromUserInput() //Creates an instance of BuildingExpense based on user input
	{
		BuildingExpense<?> buildingExpense = null;
		
		showBuildings();
		
		System.out.print("Enter building code: ");
		String code = keyboard.nextLine();
		Building building = company.getBuildingByCode(code);
		while(building == null)
		{
			System.out.print("Code doesn't exist try another one. Building code: ");
			code = keyboard.nextLine();
			building = company.getBuildingByCode(code);
		}
		
		System.out.println();
		
		for (Expense<?> expense : company.getAvailableExpense(building))
		{
			System.out.println(expense);
		}
		
		System.out.print("Enter expense code: ");
		code = keyboard.nextLine();
		Expense<?> expense = company.getAvailableExpenseByCode(building, code);
		while(expense == null)
		{
			System.out.print("Code doesn't exist try another one. Building code: ");
			code = keyboard.nextLine();
			expense = company.getAvailableExpenseByCode(building, code);
		}

		System.out.println();
		
		if (expense instanceof VariableExpense) 
		{
			System.out.print("Enter consumption in " + ((VariableExpense) expense).getUnit() + ": ");
			double consumption = validDouble(0, "Enter consumption: ");
			buildingExpense = new VariableBuildingExpense(building, (VariableExpense)expense, consumption);
		}
		else if (expense instanceof FixedExpense)
		{
			buildingExpense = new FixedBuildingExpense(building, (FixedExpense)expense);
		}
		
		System.out.println();
		
		System.out.println(buildingExpense);
		System.out.print("Add building expense (Anything other than Y or y cancels addition)? ");
		if (!keyboard.nextLine().equalsIgnoreCase("y")) return null;
		
		return buildingExpense;
	}
	
	public static void showBuildings()
	{
		System.out.println(String.format("%-8s", "Code") + String.format("%-20s", "Description") + String.format("%-20s", "Address") + String.format("%11s", "Zone Value") + String.format("%8s", "Area"));
		for (Building building : company.getBuilding())
		{
			System.out.println(building);
		}
	}
	
	public static void showBuildingExpense()
	{
		showBuildings();
		
		System.out.print("Enter building code: ");
		String code = keyboard.nextLine();
		Building building = company.getBuildingByCode(code);
		while(building == null)
		{
			System.out.print("Code doesn't exist try another one. Building code: ");
			code = keyboard.nextLine();
			building = company.getBuildingByCode(code);
		}
		
		System.out.println();

		System.out.println(String.format("%-8s", "Code") + String.format("%-20s", "Description") + String.format("%-16s", "Price per unit") + String.format("%-13s", "Fixed cost") + String.format("%8s", "Additional cost"));
		for (Expense<?> expense : company.getBuildingExpense(building))
		{
			System.out.println(expense);
		}
	}
	
	public static void showBuildingTotalExpense()
	{
		showBuildings();
		
		System.out.print("Enter building code: ");
		String code = keyboard.nextLine();
		Building building = company.getBuildingByCode(code);
		while(building == null)
		{
			System.out.print("Code doesn't exist try another one. Building code: ");
			code = keyboard.nextLine();
			building = company.getBuildingByCode(code);
		}
		
		System.out.println();
		
		System.out.println("The total expense of " + building.getCode() + " is: " + String.format("%.3f", company.calculateBuildingExpense(building)) + " euros.");
	}
	
	public static void showExpenseTotalCost()
	{
		System.out.println(String.format("%-8s", "Code") + String.format("%-20s", "Description") + String.format("%-16s", "Price per unit") + String.format("%-13s", "Fixed cost") + String.format("%8s", "Additional cost"));
		for (Expense<?> expense : company.getExpense())
		{
			System.out.println(expense);
		}
		
		System.out.print("Enter expense code: ");
		String code = keyboard.nextLine();
		Expense<?> expense = company.getExpenseByCode(code);
		while(expense == null)
		{
			System.out.print("Code doesn't exist try another one. Building code: ");
			code = keyboard.nextLine();
			expense = company.getExpenseByCode(code);
		}

		System.out.println();
		System.out.println("The total cost for " +  expense.getDescription() + " is: " + String.format("%.3f" , company.calculateTotalCostOfExpense(expense)) + " euros.");
	}
	
	public static void init()
	{
		//Adding buildings
		company.addBuilding(new Building("B001", "University", "Athens A", 10, 400));
		company.addBuilding(new Building("B002", "Library", "Athens B", 5, 200));
		company.addBuilding(new Building("B003", "Prof Office", "Piraeus", 10, 50));
		company.addBuilding(new Building("B004", "Lab 1", "Thessaloniki A", 20, 100));
		company.addBuilding(new Building("B005", "Lab 2", "Thessaloniki B", 20, 150));
		
		//Adding expense types
		company.addExpense(new WaterExpense("W001", "EYDAP", 0.005, 0.008, 15));
		company.addExpense(new EnergyExpense("E001", "DEH", 0.005, 30, 15.5));
		company.addExpense(new EnergyExpense("E002", "HRON", 0.007, 20, 15.5));
		company.addExpense(new TelephoneExpense("T001", "OTE", 0.002, 30, 10));
		company.addExpense(new TelephoneExpense("T002", "Vodafone", 0.0015, 20, 10));
		company.addExpense(new RentExpense("R001", "Rent Comp", 0.2));
		company.addExpense(new CleaningExpense("C001", "Cleaning comp", 0.2));
		
		//Adding expenses
		company.addBuildingExpense(new VariableBuildingExpense(company.getBuilding().get(0), (VariableExpense)company.getExpense().get(0), 40));
		company.addBuildingExpense(new VariableBuildingExpense(company.getBuilding().get(0), (VariableExpense)company.getExpense().get(1), 200));
		company.addBuildingExpense(new VariableBuildingExpense(company.getBuilding().get(0), (VariableExpense)company.getExpense().get(2), 70));
		company.addBuildingExpense(new VariableBuildingExpense(company.getBuilding().get(0), (VariableExpense)company.getExpense().get(3), 600));
		company.addBuildingExpense(new VariableBuildingExpense(company.getBuilding().get(1), (VariableExpense)company.getExpense().get(0), 30));
		company.addBuildingExpense(new VariableBuildingExpense(company.getBuilding().get(1), (VariableExpense)company.getExpense().get(1), 170));
		company.addBuildingExpense(new VariableBuildingExpense(company.getBuilding().get(1), (VariableExpense)company.getExpense().get(2), 20));
		company.addBuildingExpense(new VariableBuildingExpense(company.getBuilding().get(1), (VariableExpense)company.getExpense().get(4), 460));
		company.addBuildingExpense(new VariableBuildingExpense(company.getBuilding().get(2), (VariableExpense)company.getExpense().get(1), 30));
		company.addBuildingExpense(new VariableBuildingExpense(company.getBuilding().get(2), (VariableExpense)company.getExpense().get(2), 5));
		company.addBuildingExpense(new VariableBuildingExpense(company.getBuilding().get(2), (VariableExpense)company.getExpense().get(3), 230));
		company.addBuildingExpense(new VariableBuildingExpense(company.getBuilding().get(2), (VariableExpense)company.getExpense().get(4), 60));
		company.addBuildingExpense(new VariableBuildingExpense(company.getBuilding().get(3), (VariableExpense)company.getExpense().get(2), 140));
		company.addBuildingExpense(new VariableBuildingExpense(company.getBuilding().get(3), (VariableExpense)company.getExpense().get(3), 40));
		company.addBuildingExpense(new VariableBuildingExpense(company.getBuilding().get(3), (VariableExpense)company.getExpense().get(4), 70));
		company.addBuildingExpense(new FixedBuildingExpense(company.getBuilding().get(3), (FixedExpense)company.getExpense().get(5)));
		company.addBuildingExpense(new VariableBuildingExpense(company.getBuilding().get(4), (VariableExpense)company.getExpense().get(1), 230));
		company.addBuildingExpense(new VariableBuildingExpense(company.getBuilding().get(4), (VariableExpense)company.getExpense().get(3), 30));
		company.addBuildingExpense(new VariableBuildingExpense(company.getBuilding().get(4), (VariableExpense)company.getExpense().get(4), 70));
		company.addBuildingExpense(new FixedBuildingExpense(company.getBuilding().get(4), (FixedExpense)company.getExpense().get(5)));
	}
	
	public static int ValidInt(int low, int high, String prompt) //Low and High are inclusive
	{
		int number;
        do 
        {
            while (!keyboard.hasNextInt()) 
            {
                String input = keyboard.next();
                System.out.printf("\"%s\" is not a valid number.\n", input);
                System.out.print(prompt);
            }
            number = keyboard.nextInt();
            
            if (number < low || number > high) 
            {
            	System.out.printf("\"%d\" is not a valid number.\n", number);
            	System.out.print(prompt);
            }
            
        } while (number < low || number > high);

		keyboard.nextLine();
        return number;
	}
	
	public static double validDouble(double low, String prompt) //Low is exclusive
	{
		double number;
        do 
        {
            while (!keyboard.hasNextDouble()) 
            {
                String input = keyboard.next();
                System.out.printf("\"%s\" is not a valid number.\n", input);
                System.out.print(prompt);
            }
            number = keyboard.nextDouble();
            
            if (number <= low) 
            {
            	System.out.printf("\"%f\" is not a valid number.\n", number);
            	System.out.print(prompt);
            }
            
        } while (number <= low);

		keyboard.nextLine();
        return number;
	}
}