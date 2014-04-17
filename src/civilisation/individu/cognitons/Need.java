package civilisation.individu.cognitons;

import java.util.ArrayList;
import java.util.HashMap;

public class Need extends NCogniton
{
	private int value;
	private int decrease;
	public HashMap<String, Integer> satisfactions;
	
	
	public int getValue()
	{
		return value;
	}
	
	
	public void setValue(int v)
	{
		value = v;
	}
	
	
	public int getDecrease()
	{
		return decrease;
	}
	
	
	public void setDecrease(int v)
	{
		decrease = v;
	}
	
	
	public ArrayList<String> ressourcesConcern()
	{
		return new ArrayList<String>(satisfactions.keySet());
	}
}
