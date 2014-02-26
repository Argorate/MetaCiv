package civilisation;

import java.util.Arrays;
import java.util.Random;

public class Utils {
	public static boolean DEBUG = true;
	public static int LVL = 3;
	
	public static void debug(int lvl, Object o)
	{
		if(DEBUG && lvl >= LVL)
		{
			if(o == null) System.out.println("NULL");
			else System.out.println(o.toString());
		}
	}
	
	public static void debug(Object o)
	{
		debug(1, o);
	}
	
	public static void debug_(Object o)
	{
		debug(o);
		System.exit(0);
	}
	
	/**
    * Trirage de chiffre al�atoire entre deux bornes, �quiprobable
    * @return un nombre entre min et max inclus
    */  
	public static int rand(int min, int max)
	{
		Random rand = new Random();
		return rand.nextInt((max + 1) - min) + min;
	}
	
	 
   /**
    * Donne la pile d'execution actuelle
    * @return La pile d'execution actuelle
    */
   public static StackTraceElement[] getTrace()
   {
      //On r�cup�re la pile d'excution
      Throwable throwable = new Throwable();
      StackTraceElement[] trace = throwable.getStackTrace();
      //On enl�ve le premier �l�ment qui est l'appel de cette m�thode
      StackTraceElement[] reponse = new StackTraceElement[trace.length - 1];
      System.arraycopy(trace, 1, reponse, 0, reponse.length);
      //On envoie la pile d'excution
      return reponse;
   }
   
   /**
    * Affiche la pile d'execution actuelle
    * @return La pile d'execution actuelle
    */
   public static void afficherTrace()
   {
      //On r�cup�re la pile d'�cution
      StackTraceElement[] trace = getTrace();
      //On affiche la pile, sauf le premier �l�ment qui est l'appel de cette
      // m�thode
      for(int i = 1; i < trace.length; i++)
      {
    	  Utils.debug(trace[i]);
      }
   }
   
   
   /***
	 * Teste si un élément est contenu dans un tableau
	 * @param needle : l'élément à rechercher
	 * @param haystack : la tableau
	 * @return true si présent, sinon false
	 */
	public static boolean in_array(Object needle, Object[] haystack) {
		return (Arrays.binarySearch(haystack, needle) >= 0);
	}
}

