package civilisation.inspecteur.simulation.objets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import civilisation.Configuration;
import civilisation.inventaire.Item;

public class ActionsToolBarListeObjets implements ActionListener{

	PanelListeObjets p;
	int index;
	
	public ActionsToolBarListeObjets(PanelListeObjets p, int i)
	{
		this.p = p;
		index = i;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (index == 0){
			System.out.println("--New item--");
			Item o = new Item("Default_" + Configuration.objets.size());
			Configuration.objets.add(o);
			p.addObjet(o);
			
		}
	}

}
