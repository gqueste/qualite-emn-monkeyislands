package tinymonkeys.modele;

import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

import javax.swing.event.EventListenerList;

/**
 * Classe d'une bande de singes erratiques.
 * 
 * @version 1.0
 * @author Camille Constant
 * @author Gabriel Queste
 *
 */
public class BandeDeSingesErratiques extends Thread
{
	
	/**
	 * Vecteur contenant l'ensemble des singes erratiques.
	 */
	private Vector<SingeErratique> erratiques;
	
	/**
	 * L'ile.
	 */
	private Ile monkeyIsland;
	
	/**
	 * Liste des écouteurs sur la bande de singes erratiques.
	 */
	final private EventListenerList bandeSingesEcouteurs;
	
	/**
	 * Temps d'attente avant le déplacement de la bande de singes.
	 */
	final static private int TIMER = 1000;
	
	/**
	 * Constructeur d'une bande de singes erratiques vide.
	 * 
	 * @param ile l'ile contenant l'ensemble des elements de celle-ci.
	 */
	public BandeDeSingesErratiques(Ile ile)
	{
		super();
		this.erratiques = new Vector<SingeErratique>();
		this.monkeyIsland = ile;
		this.bandeSingesEcouteurs = new EventListenerList();
	}
	
	/**
	 * Accesseur en lecture a l'ensemble des singes erratiques.
	 * 
	 * @return le vecteur de singes erratiques.
	 */
	public Vector<SingeErratique> getSingesErratiques()
	{
		return this.erratiques;
	}

	
	/**
	 * Ajout du nombre indique de singes erratiques a des positions libres aleatoires.
	 * 
	 * @param n le nombre de singes a ajouter.
	 */
	public void ajoutSingesErratiques(final int n)
	{
		
		for (int i = 0; i < n; i++) {
			this.ajoutSingleSingesErratiques();
		}
		
	}
	
	/**
	 * Ajoute un signe erratique a une position libre aleatoire.
	 */
	public void ajoutSingleSingesErratiques()
	{
		final Random rand = new Random();
		boolean positionOK = false;
		while (!positionOK) {
			final int x = rand.nextInt(this.monkeyIsland.getLongueurCarte());
			final int y = rand.nextInt(this.monkeyIsland.getLargeurCarte());
			
			if (this.estPositionLibre(x, y) && this.monkeyIsland.estPositionSurTerre(x, y)) {
				positionOK = true;
				final SingeErratique singe = new SingeErratique(x, y, this.monkeyIsland);
				this.ajoutSinge(singe);
			}
		}
	}
	
	/**
	 * Ajout un unique singe erratique à la bande.
	 * @param singe le singe à ajouter
	 */
	public void ajoutSinge(SingeErratique singe)
	{
		this.erratiques.add(singe);
		for (BandeDeSingesErratiquesEcouteur e: this.bandeSingesEcouteurs
				.getListeners(BandeDeSingesErratiquesEcouteur.class)) {
			e.creationSingeErratique(this.erratiques.indexOf(singe), singe.getX(), singe.getY());
		}
	}
	
	
	/**
	 * Teste si la position n'est pas un singe.
	 * @param x coordonnée x de la case à tester
	 * @param y coordonnée y de la case à tester
	 * @return True si la case est libre
	 */
	public boolean estPositionLibre(int x, int y)
	{
		boolean ret = true;
		final Iterator<SingeErratique> iteratorSinge = this.erratiques.iterator();
		while (iteratorSinge.hasNext()) {
			final SingeErratique singeErratique = iteratorSinge.next();
			ret = !singeErratique.coordonneesEgales(x, y);
			if (!ret) {
				break;
			}
		}
		return ret;		
	}

	
	
	/**
	 * Enregistre dans la liste des ecouteurs de bande de singes l'ecouteur passe en parametre.
	 * @param ecouteur ecouteur de la bande de singes.
	 */
	public void enregistreEcBandeSinges(BandeDeSingesErratiquesEcouteur ecouteur)
	{
		this.bandeSingesEcouteurs.add(BandeDeSingesErratiquesEcouteur.class, ecouteur);
	}

	@Override
	public void run() 
	{
		while (true) {
			int id = 0;
			for (final SingeErratique singe : this.getSingesErratiques()) {
				singe.deplacerSinge();
				for (final BandeDeSingesErratiquesEcouteur bandeEcouteur : this.bandeSingesEcouteurs
						.getListeners(BandeDeSingesErratiquesEcouteur.class)) {
					bandeEcouteur.deplacementSingeErratique(id, singe.getX(), singe.getY());
				}
				id++;
			}
			try {
				Thread.sleep(BandeDeSingesErratiques.TIMER);
			} 
			catch (InterruptedException e) {
				System.err.println("Oups there is a bad mistake in the monkey's thread !");
			}
		}
	}

}
