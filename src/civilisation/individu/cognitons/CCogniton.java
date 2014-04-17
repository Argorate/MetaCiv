package civilisation.individu.cognitons;

import java.util.HashMap;

import civilisation.individu.Esprit;


/**
 * CCogniton is the combination of a Cogniton and a hashmap to store data about the cogniton
 * Only CCogniton are instantiated after initialization. No more NCogniton.
 */

public class CCogniton {
	static int ALPHA_WEIGHT = 1;
	
	public NCogniton cogniton;
	public HashMap<String , Object> hashmap = new HashMap<String,Object>();
	public double weigth = 1.0;
	public Esprit esprit = null;
	
	public CCogniton (NCogniton cogniton) {
		this.cogniton = cogniton;
		needConstructor();
	}
	
	public CCogniton (NCogniton cogniton, Esprit esprit) {
		this.cogniton = cogniton;
		this.esprit = esprit;
		needConstructor();
	}
	
	public void needConstructor()
	{
		if(cogniton.getType().equals(TypeDeCogniton.NEED))
		{
			HashMap<String, Integer> hm = new HashMap<String, Integer>();
			hm.put("value", ((Need) cogniton).getValue());
			hm.put("decrease", ((Need) cogniton).getDecrease());
			hashmap.put("NEED", hm);	
		}
	}

	public NCogniton getCogniton() {
		return cogniton;
	}

	public void setCogniton(NCogniton cogniton) {
		this.cogniton = cogniton;
	}

	public HashMap<String, Object> getHashmap() {
		return hashmap;
	}

	public void setHashmap(HashMap<String, Object> hashmap) {
		this.hashmap = hashmap;
	}
	
	public double getWeigth() {
		return weigth;
	}

	public void setWeigth(double weigth) {
		this.weigth = weigth;
	}
	
	// Some functions from NCogniton for convenience
	public void mettreEnPlace(Esprit e){
		cogniton.mettreEnPlace(e , weigth);
	}
	
	public void appliquerPoids(Esprit e){
		cogniton.appliquerPoids(e , weigth);
	}
	
	public String toString () {
		return "C:"+ cogniton.toString();
	}

	
	//--------------------- NEED ----------------------
	
	
	public int getNeedValue()
	{
		return ((HashMap<String, Integer>) hashmap.get("NEED")).get("value");
	}
	
	
	public void setNeedValue(int v)
	{
		
		if(v < 0) v = 0;
		else if(v > 100) v = 100;
		
		((HashMap<String, Integer>) hashmap.get("NEED")).put("value", v);
		
		setWeigth(calculateWeight(ALPHA_WEIGHT));
		
		if(esprit != null)
			esprit.redefinirPoids();
	}
	
	
	public int getNeedDecrease()
	{
		return ((HashMap<String, Integer>) hashmap.get("NEED")).get("decrease");
	}
	
	
	public void setNeedDecrease(int v)
	{
		((HashMap<String, Integer>) hashmap.get("NEED")).put("decrease", v);
	}
	
	public void decreaseNeed()
	{
		setNeedValue(getNeedValue() - getNeedDecrease());
	}
	
	
	public int calculateWeight(int alpha)
	{
		return (int) (alpha * (1 + (( 100 - getNeedValue()) / 100)) * weigth);
	}
	
	
}
