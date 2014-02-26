package civilisation.inventaire;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class NInventaire {

	public HashMap<Item, Integer> listeObjets;

	public NInventaire(){
		listeObjets = new HashMap<Item, Integer>();
	}
	
	
	public void addItem(Item item , int nb)
	{
		if (!listeObjets.containsKey(item))
			listeObjets.put(item, 0);
		
		Integer qte = listeObjets.get(item);
		qte += nb;
		
	}
	
	
	public void removeItem(Item item , int nb)
	{
		if(listeObjets.get(item) - nb <= 0)
			listeObjets.remove(item);
		else
		{
			Integer qte = listeObjets.get(item);
			qte -= nb;
		}
	}
	
	
	public Item findItem(String name)
	{
		for(Entry<Item, Integer> entry : (listeObjets.entrySet()))
		{
			Item item   = entry.getKey();
	
			if(item.nom.equals(name)) return item;
		}
		
		return null;
	}
	
	public int findQuantityItem(String name)
	{
		for(Entry<Item, Integer> entry : (listeObjets.entrySet()))
		{
			Item item   = entry.getKey();
			Integer qte = entry.getValue();
			
			if(item.nom.equals(name)) return qte;
		}
		
		return 0;
	}
	
	// retourne l'item qu'on a en plus grande quantité parmi la liste itemNameAccepted
	// Si itemNameAccepted == null, il regarde parmi tous l'inventaire
	// la liste itemNameRefused filtre et réduit les ressources comparé
	public Item findItemWithMaximumQuantity(ArrayList<String> itemNameAccepted, ArrayList<String> itemNameRefused)
	{
		Integer max  = 0;
		Item maxItem = null;
		for(Entry<Item, Integer> entry : (listeObjets.entrySet()))
		{
			Item item   = entry.getKey();
			Integer qte = entry.getValue();
			
			if( (itemNameAccepted == null || itemNameAccepted.contains(item.nom))
			 && ( itemNameRefused == null || !itemNameRefused.contains(item.nom)))
			{
				if(qte > max)
				{
					max = qte;
					maxItem = item;
				}
			}
		}
		
		return maxItem;
	}
	
	
	public Item findItemWithMaximumQuantity(ArrayList<String> itemNameAccepted)
	{
		return findItemWithMaximumQuantity(itemNameAccepted, null);
	}
	
	
	public Item findItemWithMaximumQuantity()
	{
		return findItemWithMaximumQuantity(null, null);
	}
}
