package com.janthos.main;

public class Logger 
{
	private int lineCounter;
	private String log;
	
	public Logger()
	{
		lineCounter = 0;
		log = "";
	}
	
	public void incrementLine()
	{
		++lineCounter;
	}
	
	public void appendMessage(String message)
	{
		log = log.concat(message + " at line " + lineCounter + ".\r\n");
	}
	
	public int getLineCounter()
	{
		return lineCounter;
	}
	
	public String getLog()
	{
		return log;
	}
}
