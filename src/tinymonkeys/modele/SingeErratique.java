package tinymonkeys.modele;

import java.util.Random;

/**
 * Classe du singe erratique.
 * 
 * @version 1.0
 * @author Camille Constant
 * @author Gabriel Queste
 *
 */

public class SingeErratique extends Singe
{
	/**
	 * Constructeur de la classe SingeErratique. 
	 * 
	 * @param x la coordonnee en abscisse du singe.
	 * @param y la coordonnee en ordonnee du singe.
	 * @param ile l'ile sur laquelle vit le singe.
	 */
	public SingeErratique(int x, int y, Ile ile)	
	{
		super(x, y, ile);
	}
	
	/**
	 * Deplacement aleatoire du singe erratique.
	 */
	public void deplacerSinge()
	{
		final Random rand = new Random();
		boolean deplacement = false;
		int nbEssai = 0;
		final int nbEssaiMax = 20;
		final int nbPossibilites = 4;
		final int south = 1;
		final int north = 2;
		final int west = 3;
		final int east = 4;
		int direction = 0;
		
		while (!deplacement) {	// Tant qu'un déplacement possible n'est pas trouvé
			nbEssai++;			
			if (nbEssai <= nbEssaiMax) { // Si l'on a effectué moins de 20 essais
				direction = rand.nextInt(nbPossibilites) + 1;	// retry
			} else {			
				direction = 0;			// déplacement impossible
			}
			
			switch (direction) { // Gère les déplacements
				case 0 :	// Déplacement considéré impossible au bout de nbEssai
					deplacement = true;
					break;
				case south :	// Déplacement vers le bas
					deplacement = this.deplacementFait(this.getX(), this.getY() - 1);
					break;
				case north :	// Déplacement vers le haut
					deplacement = this.deplacementFait(this.getX(), this.getY() + 1);
					break;
				case west :	// Déplacement vers la gauche
					deplacement = this.deplacementFait(this.getX() - 1, this.getY());
					break;
				case east :	// Déplacement vers la droite
					deplacement = this.deplacementFait(this.getX() + 1, this.getY());
					break;
				default :
					break;
			}
		}
		// Si le singe atteint le pirate il le tue
		this.gereDeplacementSingePirate();
	}
	
	/**
	 * Gere le pirate par rapport au deplacement du singe.
	 */
	public void gereDeplacementSingePirate()
	{
		final Pirate pirate = this.getMonkeyIsland().getPirate();
		if (this.getX() == pirate.getX() 
				&& this.getY() == pirate.getY()) {
			pirate.tuerPirate();
		}		
	}

	/**
	 * Fait le déplacement du singe si cela est possible.
	 * @param x coordonnée x
	 * @param y coordonnée y
	 * @return True si le déplacement a été fait
	 */
	public boolean deplacementFait(int x, int y)
	{
		boolean ret = false;
		if (this.deplacementSingeOk(x, y)) {
			this.setPosition(x, y);
			ret = true;
		}
		return ret;		
	}
	
	/**
	 * Teste si le singe peut se deplacer sans arriver sur un autre singe ou de l'eau.
	 * @param x coordonnée x
	 * @param y coordonnée y
	 * @return True si le déplacement est possible
	 */
	public boolean deplacementSingeOk(int x, int y)
	{
		boolean ret = true;
		if (!this.getMonkeyIsland().estPositionSurTerre(x, y)) {
			ret = false;
		} else {
			for (SingeErratique singe : this.getMonkeyIsland().getSingesErratiques().getSingesErratiques()) {
				if (singe.coordonneesEgales(x, y)) {
					ret = false;
					break;
				}
			}
		}
		
		return ret;
	}
}
