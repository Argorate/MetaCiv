package civilisation.commerce;

import civilisation.individu.Humain;
import civilisation.individu.cognitons.CCogniton;
import civilisation.inventaire.NInventaire;

public class Transaction 
{
	int QuantityRessourceBought; 
	String nameRessourceBought;
	int QuantityRessourceSold;
	String nameRessourceSold;
	double[] coordinates;
	int tick;
	
	
	public Transaction(int QuantityRessourceBought, String nameRessourceBought, int QuantityRessourceSold, String nameRessourceSold, double[] coordinates, int tick){
		this.QuantityRessourceBought = QuantityRessourceBought;
		this.nameRessourceBought 	 = nameRessourceBought;
		this.QuantityRessourceSold 	 = QuantityRessourceSold;
		this.nameRessourceSold 		 = nameRessourceSold;
		this.coordinates 			 = coordinates;
		this.tick 					 = tick;
	}
	
	
	public int getQuantityRessourceBought() {
		return QuantityRessourceBought;
	}
	
	
	public String getNameRessourceBought() {
		return nameRessourceBought;
	}
	
	
	public int getQuantityRessourceSold() {
		return QuantityRessourceSold;
	}

	
	public String getNameRessourceSold() {
		return nameRessourceSold;
	}
		
	
	public double[] getCoordinates() {
		return coordinates;
	}

	public int getTick() {
		return tick;
	}

	//return an exchange rate after negociation between Agent1 and Agent2
	public float negociate(Humain Agent1, Humain Agent2, float alpha) 
	{
		CCogniton tradeCognitonOfAgent1 = Agent1.getEsprit().getCogniton("trade");
		CCogniton tradeCognitonOfAgent2 = Agent2.getEsprit().getCogniton("trade");
		double skillNegociationAgent1 	= 0;
		double skillNegociationAgent2 	= 0;
		Marketer marketer				= Marketer.getInstance();
		float exchangeRateAgent1		= marketer.finalExchangeRate(Agent1, getNameRessourceBought(), getNameRessourceSold(), 1);
		float exchangeRateAgent2		= marketer.finalExchangeRate(Agent2, getNameRessourceBought(), getNameRessourceSold(), 1);
		float trueExchangeRateAgent1	= exchangeRateAgent1; // ne varie pas
		float trueExchangeRateAgent2	= exchangeRateAgent2; // ne varie pas

		if(tradeCognitonOfAgent1 != null) skillNegociationAgent1 = tradeCognitonOfAgent1.weigth;
		if(tradeCognitonOfAgent2 != null) skillNegociationAgent2 = tradeCognitonOfAgent2.weigth;
		
		int i 		 			 = 0;
		int j 		 			 = 0;
		float exchangeRateResult = 0;
		boolean agent1AcceptDeal = false;
		boolean agent2AcceptDeal = false;
		
		while(exchangeRateAgent1 > exchangeRateAgent2 && (skillNegociationAgent1 > i ||  skillNegociationAgent2 > j))
		{	
			// on le fait en premier dans la boucle pour testé l'offre de départ sans modif du premier "round de negos"
			if(marketer.isAnAcceptableExchangeRate(trueExchangeRateAgent2, exchangeRateAgent1))
				agent1AcceptDeal = true;

			if(marketer.isAnAcceptableExchangeRate(trueExchangeRateAgent1, exchangeRateAgent2))
				agent2AcceptDeal = true;
			
			
			//DEAL
			if(agent1AcceptDeal || agent2AcceptDeal)
			{
				//les deux taux sont accepter en meme temps
				if(agent1AcceptDeal && agent2AcceptDeal)
				{
					if(skillNegociationAgent2 > skillNegociationAgent1) exchangeRateResult = exchangeRateAgent2;
					else exchangeRateResult = exchangeRateAgent1;
				}
				else
				{
					if(agent1AcceptDeal) exchangeRateResult = exchangeRateAgent2;
					else exchangeRateResult = exchangeRateAgent1;
				}
				
				return exchangeRateResult; 
			}
			
			//NEGOS <=> change les taux de change progressivement
			if(skillNegociationAgent1 > i)
			{
				exchangeRateAgent1 = (float) (exchangeRateAgent1 * (1 + (alpha / skillNegociationAgent1)));
				i++;
			}
			
			if(skillNegociationAgent2 > j)
			{
				exchangeRateAgent2 = (float) (exchangeRateAgent2 * (1 + (alpha / skillNegociationAgent2)));
				j++;
			}
			
		}
		
		//intersection?
		if(exchangeRateAgent1 <= exchangeRateAgent2) 
			exchangeRateResult = (exchangeRateAgent1 + exchangeRateAgent2)/2;
		
		return exchangeRateResult;
	}
	
	
	public void apply(Humain proposerAgent, Humain receiverAgent)
	{
		NInventaire inventoryProposerAgent = proposerAgent.getInventaire();
		NInventaire inventoryReceiverAgent = receiverAgent.getInventaire();
		
		inventoryProposerAgent.removeItem( inventoryProposerAgent.findItem(nameRessourceSold), QuantityRessourceSold );
		inventoryProposerAgent.addItem( inventoryProposerAgent.findItem(nameRessourceBought), QuantityRessourceBought );
		
		inventoryReceiverAgent.removeItem( inventoryReceiverAgent.findItem(nameRessourceBought), QuantityRessourceBought );
		inventoryReceiverAgent.addItem( inventoryReceiverAgent.findItem(nameRessourceSold), QuantityRessourceSold );
	}
}
