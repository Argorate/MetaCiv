package civilisation.individu.plan.action;

import java.util.ArrayList;

import civilisation.individu.Humain;
import civilisation.inventaire.Item;

public class A_GetItem extends Action{

	Item obj;
	
	@Override
	public Action effectuer(Humain h) {

		System.out.println(obj.getNom()); //TODO
		return nextAction;
	}

	@Override
	public void parametrerOption(OptionsActions option){
		super.parametrerOption(option);
		
		if (option.getParametres().get(0).getClass().equals(Item.class)){
			obj = (Item) option.getParametres().get(0);
		}
		else
		{
			System.out.println("Mauvaise initialisation d'une action!");
		}

	}
	

	/**
	 * Retourne la structure des param�tres.
	 * Permet de d�terminer la pr�sentation de la fen�tre de r�glages.
	 */
	@Override
	public ArrayList<String[]> getSchemaParametres(){
		
		if (schemaParametres == null){
			schemaParametres = new ArrayList<String[]>();
			String[] objet = {"**Objet**" , "ObjetGagne"};
			schemaParametres.add(objet);
		}
		return schemaParametres;	
	}
	
}
