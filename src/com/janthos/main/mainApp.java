package com.janthos.main;

import java.io.*;
import java.util.Scanner;

public class mainApp
{
	private static Scanner keyboard = new Scanner(System.in);
	private static Company company;
	
	public static void main(String[] args) 
	{
		company = new Company("ABC Inc.");
		
		loadExpenses("Expenses.txt");
		loadBuildings("Buildings.txt");
		
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
		
		System.out.print("Save changes (Anything other than Y or y cancels addition)? ");
		if (keyboard.nextLine().equalsIgnoreCase("y"))
		{
			SaveExpenses("Expenses.txt");
			SaveBuildings("Buildings.txt");
			System.out.println("Files saved.");
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
		if (company.getBuilding().size() == 0)
		{
			System.out.println("There are no buildings added yet.");
			return;
		}
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
		if (company.getBuilding().size() == 0)
		{
			System.out.println("There are no buildings added yet.");
			return;
		}
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
		if (company.getExpense().size() == 0)
		{
			System.out.println("There are no expenses added yet.");
			return;
		}
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
	
	public static void loadExpenses(String filePath)
	{
		File file = null;
		BufferedReader reader = null;
		String line = null;
		
		Logger logger = new Logger();
		logger.appendMessage("Begining Expense parsing");

		try
		{
			file = new File(filePath);
		}
		catch (NullPointerException e)
		{
			logger.appendMessage("File not found");
			return;
		}
		
		try
		{
			reader = new BufferedReader(new FileReader(file));
		}
		catch (FileNotFoundException e)
		{
			logger.appendMessage("Error opening file");
			return;
		}
		
		try
		{
			line = reader.readLine();
			logger.incrementLine();
			while(line != null)
			{
				if (line != null && line.matches("(?i)\\s*expense_type_list\\s*"))
				{
					line = reader.readLine();
					logger.incrementLine();
					if (line != null && line.matches("\\s*\\{\\s*"))
					{
						line = reader.readLine();
						logger.incrementLine();
						while (line != null && !line.matches("(?i)\\s*\\{\\s*|\\s*\\}\\s*|\\s*expense_type_list\\s*"))
						{
							if (line != null && line.matches("(?i)\\s*expense_type\\s*"))
							{
								Expense<?> expense = Expense.parse(reader, logger);
								if (expense != null && company.checkExpenseCodeAvailability(expense.getCode()))
								{
									company.addExpense(expense);
								}
								
								line = reader.readLine();
								logger.incrementLine();
							}
							else if (line != null && line.matches("\\s*"))
							{
								line = reader.readLine();
								logger.incrementLine();
							}
							else throw new IOException();
						}
						line = reader.readLine();
						logger.incrementLine();
					}
					else if (line != null && line.matches("\\s*"))
					{
						line = reader.readLine();
						logger.incrementLine();
					}
					else throw new IOException();
				}
				else if (line != null && line.matches("\\s*"))
				{
					line = reader.readLine();
					logger.incrementLine();
				}
				else throw new IOException();
			}
		}
		catch (IOException e)
		{
			logger.appendMessage("Error reading file");
		}
		finally
		{
			logger.appendMessage("Parsing ended");
			System.out.println(logger.getLog());
			try
			{
				reader.close();
			}
			catch (IOException e)
			{
				logger.appendMessage("Error clossing file");
			}
		}
	}
	
	public static void loadBuildings(String filePath)
	{
		File file = null;
		BufferedReader reader = null;
		String line = null;
		
		Logger logger = new Logger();
		logger.appendMessage("Begining Building parsing");
		
		try
		{
			file = new File(filePath);
		}
		catch (NullPointerException e)
		{
			logger.appendMessage("File not found");
			return;
		}
		
		try
		{
			reader = new BufferedReader(new FileReader(file));
		}
		catch (FileNotFoundException e)
		{
			logger.appendMessage("Error opening file");
			return;
		}
		
		try
		{
			line = reader.readLine();
			logger.incrementLine();
			
			while (line != null)
			{
				if (line != null && line.matches("(?i)\\s*building_list\\s*"))
				{
					line = reader.readLine();
					logger.incrementLine();
					if (line != null && line.matches("\\s*\\{\\s*"))
					{
						line = reader.readLine();
						logger.incrementLine();
						while (line != null && !line.matches("(?i)\\s*\\{\\s*|\\s*\\}\\s*|\\s*building_list\\s*"))
						{
							if (line != null && line.matches("(?i)\\s*building\\s*"))
							{
								Building building = Building.parse(reader, company.getExpense(), logger);
								if (building != null && company.checkBuildingCodeAvailability(building.getCode()))
								{
									company.addBuilding(building);
								}
								
								line = reader.readLine();
								logger.incrementLine();
							}
							else if (line != null && line.matches("\\s*"))
							{
								line = reader.readLine();
								logger.incrementLine();
							}
							else throw new IOException();
						}
						line = reader.readLine();
						logger.incrementLine();
					}
					else if (line != null && line.matches("\\s*"))
					{
						line = reader.readLine();
						logger.incrementLine();
					}
					else throw new IOException();
				}
				else if (line != null && line.matches("\\s*"))
				{
					line = reader.readLine();
					logger.incrementLine();
				}
				else throw new IOException();
			}
		}
		catch (IOException e)
		{
			logger.appendMessage("Error reading file");
		}
		finally
		{
			logger.appendMessage("Parsing ended");
			System.out.println(logger.getLog());
			try
			{
				reader.close();
			}
			catch (IOException e)
			{
				logger.appendMessage("Error closing file");
			}
		}
	}
	
	public static void SaveExpenses(String filePath)
	{
		PrintWriter writer = null;
		
		try
		{
			writer = new PrintWriter(new FileWriter(filePath), true);
			writer.println("EXPENSE_TYPE_LIST");
			writer.println("{");
			for (Expense<?> expense : company.getExpense())
			{
				expense.printFile(writer);
			}
			writer.println("}");
		}
		catch(IOException e)
		{
			System.out.println("Error writing file");
		}
		finally
		{
			writer.close();
		}
	}
	
	public static void SaveBuildings(String filePath)
	{
		PrintWriter writer = null;
		
		try
		{
			writer = new PrintWriter(new FileWriter(filePath), true);
			writer.println("BUILDING_LIST");
			writer.println("{");
			for (Building building : company.getBuilding())
			{
				building.printBuilding(writer);
			}
			writer.println("}");
		}
		catch(IOException e)
		{
			System.out.println("Error writing file");
		}
		finally
		{
			writer.close();
		}
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