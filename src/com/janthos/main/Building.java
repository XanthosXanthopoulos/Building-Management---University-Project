package com.janthos.main;

public class Building 
{
	private String code;
	private String description;
	private String address;
	private double zoneValue;
	private double area;

	public Building() {}
	
	public Building(String code, String description, String address, double zoneValue, double area)
	{
		this.code = code;
		this.description = description;
		this.address = address;
		this.zoneValue = zoneValue;
		this.area = area;
	}
	
	public Building(Building building)
	{
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
	
	public String toString()
	{
		return code + " " + description + " " + address + " " + zoneValue + " " + area;		
	}
}