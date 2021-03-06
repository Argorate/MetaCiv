package civilisation.amenagement;

import java.awt.Color;
import java.awt.Graphics;

import edu.turtlekit2.kernel.environment.Patch;

public class Amenagement_Route extends AmenagementPublic {
	static String[] cognitonsLies = {};

	
	public Amenagement_Route(Patch p)
	{
		super(p);
	}
	
	@Override
	public void dessiner(Graphics g,int x,int y,int cellS)
	{
		g.setColor(Color.GRAY);
		
		g.drawLine(x, y+(cellS/2)    , x+cellS-1, y+(cellS/2)    );
		g.drawLine(x, y+(cellS/2) + 1, x+cellS-1, y+(cellS/2) + 1);
		
		g.drawLine(x+(cellS/2)    , y, x+(cellS/2)    , y+cellS-1);
		g.drawLine(x+(cellS/2) + 1, y, x+(cellS/2) + 1, y+cellS-1);

	}
	
	@Override
	public String getNom()
	{
		return "Route";
	}
	
	@Override
	public String[] cognitonsLies()
	{
		return cognitonsLies;
	}
	
}
