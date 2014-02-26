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
	
	
	public void decrease()
	{
		value -= decrease;
	}
	
	
	public int calculateWeight(int alpha)
	{
		return alpha * (1 + (( 100 - value) / 100)) /** weight*/; //le poids doit etre propre a chaque need, hors pour l'instant un Need est un NCogniton et les NCogniton sont des sorte de singleton commun à tous les agents, alors qu'il faudrait des instances pour chaque
	}
	
	
	public ArrayList<String> ressourcesConcern()
	{
		return new ArrayList<String>(satisfactions.keySet());
	}
}
