package com.janthos.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Building 
{
	private String code;
	private String description;
	private String address;
	private double zoneValue;
	private double area;

	private ArrayList<BuildingExpense<?>> buildingExpense;
	
	public Building() 
	{
		buildingExpense = new ArrayList<BuildingExpense<?>>();
	}
	
	public Building(String code, String description, String address, double zoneValue, double area)
	{
		buildingExpense = new ArrayList<BuildingExpense<?>>();
		this.code = code;
		this.description = description;
		this.address = address;
		this.zoneValue = zoneValue;
		this.area = area;
	}
	
	public Building(Building building)
	{
		buildingExpense = building.getBuildingExpense();
		this.code = building.code;
		this.description = building.description;
		this.address = building.address;
		this.zoneValue = building.zoneValue;
		this.area = building.area;
	}
	
	public String getCode()
	{
		return code;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public String getAddress()
	{
		return address;
	}
	
	public double getZoneValue()
	{
		return zoneValue;
	}
	
	public double getArea()
	{
		return area;
	}
	
	public ArrayList<BuildingExpense<?>> getBuildingExpense()
	{
		return buildingExpense;
	}
	
	public void setCode(String code)
	{
		this.code = code;
	}
	
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	public void setAddress(String address)
	{
		this.address = address;
	}
	
	public void setZoneValue(double zoneValue)
	{
		this.zoneValue = zoneValue;
	}
	
	public void setArea(double area)
	{
		this.area = area;
	}
	
	public boolean addBuildingExpense(BuildingExpense<?> buildingExpense)
	{
		if (buildingExpense == null) return false;
		return this.buildingExpense.add(buildingExpense);
	}
	
	public boolean removeBuildingExpense(BuildingExpense<?> buildingExpense) 
	{
		if (buildingExpense == null) return false;
		return this.buildingExpense.remove(buildingExpense);
	}
	
	public String toString()
	{
		return String.format("%-8s", code) + String.format("%-20s", description) + String.format("%-20s", address) + String.format("%8.3f %3s", zoneValue, "") + String.format("%8.3f %3s", area, "");		
	}
	
	public static Building parse(BufferedReader reader, ArrayList<Expense<?>> expense, Logger logger)
	{
		if (reader == null) return null;
		
		Building building = new Building();
		String line  = null, code, description, address;
		double area, zoneValue = 0;
		boolean hasCode, hasDescr, hasArea, hasAddress, hasUniqueCode, hasUniqueDescr, hasUniqueArea, hasUniqueAddress, hasUniqueExpense;
		ArrayList<BuildingExpense<?>> buildingExpense = null;
		
		try 
		{
			line = reader.readLine();
			logger.incrementLine();
			if (line != null && line.matches("(?i)\\s*\\{\\s*"))
			{
				line = reader.readLine();
				logger.incrementLine();
				hasCode = hasDescr = hasArea = hasAddress = hasUniqueCode = hasUniqueDescr = hasUniqueArea = hasUniqueAddress = hasUniqueExpense = false;
				code = description = address = null;
				area = zoneValue = 0;
				while (line != null && !line.matches("(?i)\\s*\\{\\s*|\\s*\\}\\s*|\\s*building\\s*"))
				{
					if (line.matches("(?i)\\s*building_code\\s+(\\S+\\s*)+"))
					{
						if (hasUniqueCode)
						{
							hasCode = false;
						}
						else
						{
							hasCode = hasUniqueCode = true;
							code = line.trim().substring(14).trim();
						}
					}
					else if (line.matches("(?i)\\s*building_descr\\s+(\\S+\\s*)+"))
					{
						if (hasUniqueDescr)
						{
							hasDescr = false;
						}
						else
						{
							hasDescr = hasUniqueDescr = true;
							description = line.trim().substring(15).trim();
						}
					}
					else if (line.matches("(?i)\\s*address\\s+(\\S+\\s*)+"))
					{
						if (hasUniqueAddress)
						{
							hasAddress = false;
						}
						else
						{
							hasAddress = hasUniqueAddress = true;
							address = line.trim().substring(8).trim();
						}
					}
					else if (line.matches("(?i)\\s*surface\\s+\\S+\\s*"))
					{
						if (hasUniqueArea)
						{
							hasArea = false;
						}
						else
						{
							hasArea = hasUniqueArea = true;
							try
							{
								area = Double.parseDouble(line.trim().substring(8).trim());
							}
							catch (NumberFormatException e)
							{
								logger.appendMessage("Error converting input to number");
							}
						}
					}
					else if (line.matches("(?i)\\s*expenses\\s*"))
					{
						buildingExpense = BuildingExpense.parse(reader, building, expense, logger);
						
						if (hasUniqueExpense)
						{
							logger.appendMessage("More than one occurencies of EXPENSES tag. Last occurence added. Possible loss of data");
						}
						else
						{
							hasUniqueExpense = true;
						}
					}
					else if (line.matches("(?i)\\s*price\\s+\\S+\\s*"))
					{
						zoneValue = Double.parseDouble(line.trim().substring(6).trim());
					}
					
					reader.mark(8192);
					line = reader.readLine();
					logger.incrementLine();
				}
				
				if (line != null && line.matches("(?i)\\s*\\}\\s*|\\s*building\\s*"))
				{
					if (hasCode && hasDescr && hasAddress && hasArea)
					{
						building.setCode(code);
						building.setDescription(description);
						building.setAddress(address);
						building.setArea(area);
						building.setZoneValue(zoneValue);
						for (BuildingExpense<?> buildExp : buildingExpense)
						{
							building.addBuildingExpense(buildExp);
						}
					}
					else
					{
						logger.appendMessage("Missing BUILDING_CODE, BUILDING_DESCR, ADDRESS or SURFACE");
						building = null;
					}
					
					if (line.matches("(?i)\\s*building\\s*"))
					{
						reader.reset();
						logger.appendMessage("Missing closing bracket. Object added but with possible loss of data");
					}
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
		catch (IOException e)
		{
			logger.appendMessage("Error reading file");
		}
		catch (NullPointerException e)
		{
			logger.appendMessage("Error reading number. Input is null");
			return null;
		}
		
		return building;
	}
	
	public void printBuilding(PrintWriter writer)
	{
		writer.println("\tBUILDING");
		writer.println("\t{");
		writer.println("\t\tBUILDING_CODE " + getCode());
		writer.println("\t\tBUILDING_DESCR " + getDescription());
		writer.println("\t\tADDRESS " + getAddress());
		writer.println("\t\tSURFACE " + getArea());
		writer.println("\t\tPRICE " + getZoneValue());
		writer.println("\t\tEXPENSES");
		writer.println("\t\t{");
		for (BuildingExpense<?> buildingExpense : this.buildingExpense)
		{
			buildingExpense.printBuildingExpense(writer);
		}
		writer.println("\t\t}");
		writer.println("\t}");
	}
}