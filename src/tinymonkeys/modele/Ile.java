package tinymonkeys.modele;

import java.util.Random;

import javax.swing.event.EventListenerList;

/**
 * Classe Ile. 
 * 
 * @version 1.0
 * @author Camille Constant
 * @author Gabriel Queste
 *
 */
public class Ile 
{	
	/**
	 * La carte de l'ile : une matrice indiquant si chaque case est de type mer (0) ou terre (1).
	 */
	private int[][] carte;
	
	/**
	 * Les singes erratiques.
	 */
	private BandeDeSingesErratiques erratiques;
	
	/**
	 * Le tresor.
	 */
	private Tresor tresor;
	
	/**
	 * Le pirate du joueur.
	 */
	private Pirate pirate;
	
	/**
	 * Liste des écouteurs sur l'ile.
	 */
	final private EventListenerList ileEcouteurs;
	
	/**
	 * Constructeur de la classe Ile. 
	 */
	public Ile()
	{
		this.carte = null;
		this.erratiques = new BandeDeSingesErratiques(this);
		this.tresor = null;
		this.pirate = new Pirate(this);
		this.ileEcouteurs = new EventListenerList();
	}
	
	
	/**
	 * Indique la largeur de la carte en nombre de cases.
	 * 
	 * @return la largeur de la carte.
	 */
	public int getLargeurCarte()
	{
		return this.carte.length;
	}
	
	/**
	 * Indique la longueur de la carte en nombre de cases.
	 * 
	 * @return la longueur de la carte.
	 */
	public int getLongueurCarte()
	{
		return this.carte[0].length;
	}
	
	/**
	 * Permet l'acces en lecture a la valeur de la carte aux coordonnees indiquees.
	 * 
	 * @param x la coordonnee en abscisse.
	 * @param y la coordonnee en ordonnee.
	 * @return la valeur de la case de la carte aux coordonnees indiquees.
	 */
	public int valeurCarte(int x, int y)
	{
		return this.carte[x][y];
	}
	
	/**
	 * Creation de la carte.
	 * 
	 * @param carte la matrice terre-mer. 
	 */
	public void creationCarte(int[][] carte)
	{
		this.setCarte(carte);

		// Transfere les ecouteurs
		for (IleEcouteur e: this.ileEcouteurs.getListeners(IleEcouteur.class)) {
			e.creationCarte(this.carte);
		}
	}
	
	/**
	 * Mise à jour de la carte.
	 * 
	 * @param carte la matrice terre-mer. 
	 */
	public void setCarte(int[][] carte)
	{
		this.carte = new int[carte.length][carte[0].length];
		for (int i = 0; i < carte.length; i++) {
			for (int j = 0; j < carte[0].length; j++) {
				this.carte[i][j] = carte[i][j];
			}
		}
	}
	
	/**
	 * Accesseur en lecture du pirate de l'ile.
	 * 
	 * @return le pirate.
	 */
	public Pirate getPirate()
	{
		return this.pirate;
	}
	
	/**
	 * Creation du pirate sur l'ile.
	 * 
	 * @param avatar le lien vers l'image du pirate.
	 */
	public void ajoutPirate(String avatar)
	{
		final Random rand = new Random();
		int x = 0;
		int y = 0;
		do {
			x = rand.nextInt(this.getLongueurCarte());
			y = rand.nextInt(this.getLargeurCarte());
		} while (!this.estPositionSurTerre(x, y));
				
		this.pirate.positionInitiale(x, y);	
	}
	
	/**
	 * Methode permettant de faire la demande de deplacement du pirate. 
	 * Cette methode fait suite a un appui sur une fleche directionnelle du clavier.
	 * 
	 * @param dx la direction en abscisse.
	 * @param dy la direction en ordonnee.
	 */
	public void demandeDeplacementPirate(int dx, int dy)
	{
		this.pirate.demandeDeplacement(dx, dy);
	}
	

	
	/**
	 * Accesseur en lecture de la bande de singes erratiques.
	 * 
	 * @return la bande de singes erratiques.
	 */
	public BandeDeSingesErratiques getSingesErratiques()
	{
		return this.erratiques;
	}
	
	/**
	 * Ajout du nombre indique de singes erratiques dans la liste des singes erratiques.
	 * 
	 * @param n le nombre de singes erratiques a ajouter.
	 */
	public void ajoutSingesErratiques(int n)
	{
		this.erratiques.ajoutSingesErratiques(n);
	}
	
	
	/**
	 * Accesseur en lecture du tresor.
	 * 
	 * @return le tresor.
	 */
	public Tresor getTresor()
	{
		return this.tresor;
	}
	
	/**
	 * Creation du tresor a une position aleatoire.
	 */
	public void creationTresor()
	{
		final Random rand = new Random();
		boolean positionOK = false;
		int x = 0;
		int y = 0;
		while (!positionOK) {
			x = rand.nextInt(this.getLongueurCarte());
			y = rand.nextInt(this.getLargeurCarte());
			if (this.estPositionSurTerre(x, y)) {
				positionOK = true;
			}
		}
		if (this.carte[x][y] == 1) {
			this.tresor = new Tresor(x, y);
			for (IleEcouteur e: this.ileEcouteurs.getListeners(IleEcouteur.class)) {
				e.creationTresor(x, y);
			}
		} else {
			this.creationTresor();
		}
	}
	
	
	/**
	 * Suppression du tresor.  
	 */ 
	public void suppressionTresor()
	{		
		this.tresor = null;
		for (IleEcouteur e: this.ileEcouteurs.getListeners(IleEcouteur.class)) {
			e.suppressionTresor();
		}
	}
	

	/**
	 * Enregistre dans la liste des ecouteurs de l'ile l'ecouteur passe en parametre.
	 * @param ecouteur ecouteur de l'ile.
	 */
	public void enregistreEcIle(IleEcouteur ecouteur)
	{
		this.ileEcouteurs.add(IleEcouteur.class, ecouteur);
	}
	
	/**
	 * Teste si la position demandée est bien sur terre.
	 * @param x coordonnée X de la case à tester
	 * @param y coordonnée Y de la case à tester
	 * @return True si la case est Terre. False si c'est de l'eau
	 */
	public boolean estPositionSurTerre(int x, int y)
	{
		boolean ret = false;
		if (this.carte[x][y] > 0) {
			ret = true;
		}
		return ret;
	}
	
	
}
