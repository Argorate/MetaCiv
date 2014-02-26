package civilisation.commerce;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import civilisation.individu.Humain;
import civilisation.individu.cognitons.CCogniton;
import civilisation.individu.cognitons.Need;
import civilisation.individu.cognitons.TypeDeCogniton;
import civilisation.inventaire.Item;
import civilisation.inventaire.NInventaire;
import civilisation.world.World;

public class Marketer 
{
	static private Marketer singleton;
	public Humain proposerAgent;
	public Humain receiverAgent;
	private int toleranceTick = 1000;
	private int toleranceExchangeRate = 3;
	
	
	static public Marketer getInstance()
	{
		if(singleton == null)
			singleton = new Marketer();

		return singleton;
	}
	
	
	public void setToleranceExchangeRate(int t)
	{
		toleranceExchangeRate = t;
	}
	
	
	public void setToleranceTick(int newToleranceTick)
	{
		toleranceTick = newToleranceTick;
	}
	
	
	public int getTickLimit()
	{
		return World.tick() - toleranceTick; 
	}
	
	
	public double[] whereCanIFind(String ressourceNeeded)
	{
		HashMap<Integer, Object> transactions = (HashMap<Integer, Object>) proposerAgent.getMemory().get("transactions");
		HashMap<String, Integer> counter 	  = new HashMap<String, Integer>();
		
		if(transactions.containsKey(ressourceNeeded))
		{
			for(Object[] transaction : (ArrayList<Object[]>) transactions.get(ressourceNeeded)) 
			{
				if((int) transaction[3] >= getTickLimit())
				{
					double[] coordinates = (double[]) transaction[2];
					String coordinatesKey  = coordinates[0] + "_" + coordinates[1];
					
					if(!counter.containsKey(coordinatesKey))
						counter.put(coordinatesKey, 0);
					
					Integer value = counter.get(coordinatesKey);						
					value++;
					counter.put(coordinatesKey, value);
				}
			}

			
			//on prend le patch qui a le plus de fois donner des transactions
			Integer maximum = 0;
			String keyCoordinatesSelected = null;
			for(Entry<String, Integer>  entry : counter.entrySet())
			{
				if(entry.getValue() > maximum)
					maximum = entry.getValue();
				keyCoordinatesSelected = entry.getKey();
			}
			
			if(keyCoordinatesSelected != null)
			{
				String[] tmp = keyCoordinatesSelected.split("_");
				double[] coordinatesSelected = { Double.parseDouble(tmp[0]), Double.parseDouble(tmp[0]) };
				return coordinatesSelected;
			}
				
		}
		
		return null;
	}
	
	
	public boolean isAnAcceptableExchangeRate(float individualEstimationOfExchangeRate, float proposedExchangeRate)
	{
		return proposedExchangeRate >= individualEstimationOfExchangeRate - toleranceExchangeRate && proposedExchangeRate <= individualEstimationOfExchangeRate + toleranceExchangeRate;
	}
	
	
	public void historize(Humain individual, Transaction transaction)
	{
		Object[] transactionToStore			 = { transaction.getQuantityRessourceSold() / transaction.getQuantityRessourceSold(), transaction.getQuantityRessourceSold(), transaction.getCoordinates(), transaction.getTick() };
		HashMap<String, Object> transactions = (HashMap<String, Object>) individual.getMemory().get("transactions");
		
		if(!transactions.containsKey(transaction.getNameRessourceBought()))
			((HashMap<String, Object>) transactions).put(transaction.getNameRessourceBought(), new HashMap<String, Object>());
			
		ArrayList<Object[]> ressourceBoughtTransactions = (ArrayList<Object[]>) transactions.get(transaction.getNameRessourceBought());
		ressourceBoughtTransactions.add(transactionToStore);
	}
	
	
	public boolean dealWith(Transaction transaction)
	{	
		if(transaction != null)
		{
			float exchangeRateTransaction = transaction.negociate(proposerAgent, receiverAgent, 1); 
			float exchangeRateAgent1 	  = finalExchangeRate(proposerAgent, transaction.getNameRessourceBought(), transaction.getNameRessourceSold(), 1);
			float exchangeRateAgent2 	  = finalExchangeRate(receiverAgent, transaction.getNameRessourceBought(), transaction.getNameRessourceSold(), 1);
	
			//DEAL IS OK
			if(isAnAcceptableExchangeRate(exchangeRateAgent1, exchangeRateTransaction) && isAnAcceptableExchangeRate(exchangeRateAgent2, exchangeRateTransaction))
			{
				historize(proposerAgent, transaction);
				historize(receiverAgent, transaction);
				transaction.apply(proposerAgent, receiverAgent);
				return true;
			}
		}
		
		return false;
	}
	
	
	public Transaction createTransacton(Need need)
	{
		ArrayList<String> ressources 		= need.ressourcesConcern();
		NInventaire inventory 		 		= proposerAgent.getInventaire();
		NInventaire otherInventory 		 	= receiverAgent.getInventaire();
		Item itemSold 				 		= inventory.findItemWithMaximumQuantity(null, ressources);	
		int satisfactionMissing 	 		= 100 - need.getValue();
		int maxSatisfaction 		 		= 0;
		String nameRessourceMaxSatisfaction = null;
		
		for(Entry<String, Integer> entry : need.satisfactions.entrySet())
		{
			if(entry.getValue() > maxSatisfaction) 
			{
				maxSatisfaction = entry.getValue();
				nameRessourceMaxSatisfaction = entry.getKey();
			}
		}
		
		int quantityRessourceBoughtReceiverAgent = otherInventory.findQuantityItem(nameRessourceMaxSatisfaction);
		if(quantityRessourceBoughtReceiverAgent == 0) return null; //transaction abordé, l'autre n'a pas de ressource que l'on demande
				
		double[] coordinates 			= { proposerAgent.getX(), proposerAgent.getY() };
		int QuantityRessourceBought 	= Math.min(satisfactionMissing / maxSatisfaction, quantityRessourceBoughtReceiverAgent);
		String nameRessourceBought  	= nameRessourceMaxSatisfaction;	
		String nameRessourceSold 		= itemSold.getNom();
		float proposerAgentExchangeRate = finalExchangeRate(proposerAgent, nameRessourceSold, nameRessourceSold, 1);
		int QuantityRessourceSold 		= (int) (proposerAgentExchangeRate * QuantityRessourceBought);
		
		//si l'accord prévoi de vendre plus que ce que l'agent possède vraiment, on recalcule:
		if(inventory.findQuantityItem(nameRessourceSold) < QuantityRessourceSold)
		{
			QuantityRessourceSold   = inventory.findQuantityItem(nameRessourceSold);
			QuantityRessourceBought = (int) (QuantityRessourceSold / proposerAgentExchangeRate);
		}
		
		return new Transaction(QuantityRessourceBought, nameRessourceBought, QuantityRessourceSold, nameRessourceSold, coordinates, World.tick());
	}
	
	
	public boolean proposeAndDealTransaction(Need need)
	{	
		return dealWith(createTransacton(need));
	}
	
	
	public int ressourceUtility(Humain individual, String ressourceName)
	{
		int utility = 0;
		ArrayList<CCogniton> listCNeed = individual.getEsprit().getCognitonsOfType(TypeDeCogniton.NEED);
		for(int i = 0 ; i < listCNeed.size() ; i++)
		{
			Need needCogniton = (Need) listCNeed.get(i).getCogniton();
			int needValue 	  = needCogniton.getValue();
			
			if(needCogniton.ressourcesConcern().contains(ressourceName))
				utility += needValue * needCogniton.satisfactions.get(ressourceName);
		}
		
		return utility;
	}
	
	
	public float exchangeRate(Humain individual, String Ressource1, String Ressource2)
	{
		return ressourceUtility(individual, Ressource1) / ressourceUtility(individual, Ressource2);
	}
	
	
	/* STRUCTURE :
	 Memory
	  	["transactions"]
			["nomRessourceAcheté"]
				ArrayList<Object[]> => [QteRessourceVendu, "NomRessourceVendu", [x, y], tick] // tjs pour 1 de Qte de la RessourceAcheté
	 */
	public float memoryExchangeRate(Humain individual, String nameRessourceBought, String nameRessourceSold)
	{
		float exchangeRate = 0;
		float nb 		   = 0;
		Object[] transaction;
		HashMap<String, Object> transactions 			  = (HashMap<String, Object>) individual.getMemory().get("transaction");
		ArrayList<Object[]> transactionsByRessourceBought = (ArrayList<Object[]>) transactions.get(nameRessourceBought);
		
		Iterator<Object[]> Iterate = transactionsByRessourceBought.iterator();
		while(Iterate.hasNext()) 
		{
			transaction = Iterate.next();
			if(((String) transaction[1]).equals(nameRessourceSold))
			{
				float exchangeRateTmp = (float) transaction[0];
				int ponderation 	  = Math.round(100 - (exchangeRateTmp * 100 / toleranceTick));
				
				exchangeRate += exchangeRateTmp * ponderation;
				nb += ponderation;
			}
		}

		return exchangeRate / nb;
	}
	
	
	public float finalExchangeRate(Humain individual, String offerRessource, String demandRessource, int alpha)
	{
		return alpha * exchangeRate(individual, offerRessource, demandRessource) + (1-alpha) * memoryExchangeRate(individual, offerRessource, demandRessource);
	}
	
}
