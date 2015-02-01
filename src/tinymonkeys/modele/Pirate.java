package tinymonkeys.modele;

import javax.swing.event.EventListenerList;

/**
 * Classe d'un pirate. 
 * 
 * @version 1.0
 * @author Camille Constant
 * @author Gabriel Queste
 *
 */
public class Pirate 
{

	/**
	 * Le chemin vers l'image du pirate.
	 */
	private String avatar;
	
	/**
	 * Coordonnee en abscisse du pirate (indice dans la carte).
	 */
	private int x;
	
	/**
	 * Coordonnee en ordonnee du pirate (indice dans la carte).
	 */
	private int y;
	
	/**
	 * Ile aux singes.
	 */
	private Ile monkeyIsland;
	
	/**
	 * Liste des écouteurs sur le pirate.
	 */
	final private EventListenerList pirateEcouteurs;
	
	/**
	 * Constructeur du pirate sans position ni nom renseignes mais avec l'ile associee.
	 * 
	 * @param ile l'ile contenant tous les elements de celle-ci.
	 */
	public Pirate(Ile ile)
	{
		this.monkeyIsland = ile;
		this.pirateEcouteurs = new EventListenerList();
	}
	
	/**
	 * Constructeur du pirate avec le nom renseigne.
	 * 
	 * @param ile l'ile contenant tous les elements de celle-ci.
	 * @param avatar le lien vers l'avatar du pirate.
	 */
	public Pirate(Ile ile, String avatar)
	{
		this.monkeyIsland = ile;
		this.avatar = avatar;
		this.pirateEcouteurs = new EventListenerList();
	}
		
	/**
	 * Accesseur en lecture de la position en abscisse du pirate.
	 * @return la coordonnee en abscisse.
	 */
	public int getX()
	{
		return this.x;
	}
	
	
	/**
	 * Accesseur en lecture de la position en ordonnee du pirate.
	 * @return la coordonnee en ordonnee.
	 */
	public int getY()
	{
		return this.y;
	}
	
	
	/**
	 * Place le pirate a sa position initiale.
	 * 
	 * @param x la coordonnee initiale en abscisse.
	 * @param y la coordonnee initiale en ordonnee.
	 */
	public void positionInitiale(int x, int y)
	{	
		this.x = x;
		this.y = y;
		
		this.gerePositionPirateSingesInitiale();
	}
	
	/**
	 * Verifie si le pirate ne se trouve pas sur la position d'un singe.
	 * Ajoute le pirate si ce n'est pas le cas
	 * Sinon gère la mort
	 */
	private void gerePositionPirateSingesInitiale()
	{
		boolean death = false;
		
		for (SingeErratique singe : this.monkeyIsland.getSingesErratiques().getSingesErratiques()) {
			if (singe.coordonneesEgales(this.x, this.y)) {
				death = true;
				break;
			}
		}

		for (PirateEcouteur e: this.pirateEcouteurs.getListeners(PirateEcouteur.class)) {
			if (death) {
				e.mortPirate(0);
			} else { 
				e.ajoutPirate(0, this.x, this.y, this.avatar);
				e.liberationClavier();
			}
		}
		
	}

	/**
	 * Methode deplacant le pirate selon la direction demandee.
	 * Si le deplacement indique positionne le pirate sur un singe, le pirate meurt.
	 * Si le deplacement indique positionne le pirate sur le tresor, le tresor est detruit.
	 * 
	 * @param dx la direction en abscisse (comprise entre -1 et 1).
	 * @param dy la direction en ordonnee (comprise entre -1 et 1).
	 */
	public void demandeDeplacement(int dx, int dy)
	{
		final int newX = this.x + dx;
		final int newY = this.y + dy;
		
		if (this.monkeyIsland.estPositionSurTerre(newX, newY)) {
			this.x = newX;
			this.y = newY;
		}		
		this.gerePositionPirateTresor();
		this.gerePositionPirateSinges();
	}
	
	/**
	 * Verifie si le pirate ne se trouve pas sur la position d'un singe.
	 * Deplace le pirate si ce n'est pas le cas
	 * Sinon gère la mort
	 */
	private void gerePositionPirateSinges()
	{
		boolean death = false;
		
		for (SingeErratique singe : this.monkeyIsland.getSingesErratiques().getSingesErratiques()) {
			if (singe.coordonneesEgales(this.x, this.y)) {
				death = true;
				break;
			}
		}

		for (PirateEcouteur e: this.pirateEcouteurs.getListeners(PirateEcouteur.class)) {
			if (death) {
				e.mortPirate(0);
			} else { 
				e.deplacementPirate(0, this.x, this.y);
				e.liberationClavier();
			}
		}
		
	}
	
	/**
	 * Verifie si le pirate ne se trouve pas sur la position du trésor.
	 * Gere si c'est le cas 
	 */
	private void gerePositionPirateTresor()
	{
		if (this.monkeyIsland.getTresor().coordonneesEgales(this.x, this.y)) {
			this.monkeyIsland.suppressionTresor();
			this.monkeyIsland.creationTresor();
		}	
	}
	
	
	/**
	 * Accesseur en lecture de l'avatar.
	 * 
	 * @return le chemin vers l'image.
	 */
	public String getAvatar()
	{
		return this.avatar;
	}
	
	/**
	 * Accesseur en ecriture de l'avatar du pirate.
	 * 
	 * @param avatar l'avatar du pirate.
	 */
	public void setAvatar(String avatar)
	{
		this.avatar = avatar;
	}
		
	
	/**
	 * Enregistre dans la liste des ecouteurs de pirate l'ecouteur passe en parametre.
	 * 
	 * @param ecouteur ecouteur du pirate.
	 */
	public void enregistreEcPirate(PirateEcouteur ecouteur)
	{
		this.pirateEcouteurs.add(PirateEcouteur.class, ecouteur);
	}
	
	/**
	 * Tue le pirate.
	 */
	public void tuerPirate()
	{	
		for (PirateEcouteur e: this.pirateEcouteurs.getListeners(PirateEcouteur.class)) {
			e.mortPirate(0);
		}
	}
}
