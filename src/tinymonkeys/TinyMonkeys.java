package tinymonkeys;

import tinymonkeys.controleur.Controleur;

/**
 * Classe principale de TinyMonkeys.
 * 
 * @version 1.0
 * @author Camille Constant
 *
 */
final public class TinyMonkeys 
{

	/**
	 * Constructeur privé de TinyMonkeys.
	 * 
	 * Ce constructeur privé assure la non-instanciation de TinyMonkeys dans un programme.
	 * (TinyMonkeys est la classe principale du programme TinyMonkeys)
	 */
	private TinyMonkeys() 
	{
		// Constructeur privé pour assurer la non-instanciation de TinyMonkeys.
	}
	
	/**
	 * Main du programme.
	 * 
	 * @param args arguments.
	 */
	public static void main(String[] args)
	{
		final Controleur c = new Controleur();
		c.lanceEvolutionsPersonnages();
	}
	
}
