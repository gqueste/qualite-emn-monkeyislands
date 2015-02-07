package tinymonkeys.test;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import tinymonkeys.controleur.Controleur;
import tinymonkeys.modele.BandeDeSingesErratiques;
import tinymonkeys.modele.Ile;
import tinymonkeys.modele.SingeErratique;

public class TestSingeErratiqueWithoutMock extends TestCase {

	private Ile ile;
	private SingeErratique singe;
	private int positionX = 10;
	private int positionY = 10;

	@Before
	public void setUp() throws Exception {
		ile = new Ile();
		ile.creationCarte(Controleur.exempleCarte());
		ile.creationTresor();
		singe = new SingeErratique(positionX, positionY, ile);
		BandeDeSingesErratiques bande = ile.getSingesErratiques();
		bande.ajoutSinge(singe);
	}

	@Test
	public void testDeplacementSingeOK_OKAucunSinge() {
		int positionX = 5;
		int positionY = 5;

		assertTrue("deplacement non permis alors que coordonnées OK sans singes",
				this.singe.deplacementSingeOk(positionX, positionY));
	}

	@Test
	/**
	 * test si le déplacement est permis alors qu'il y a un singe,
	 * mais il ne bloque pas
	 */
	public void testDeplacementSingeOK_OKSingesExistant() {
		int positionX = 5;
		int positionY = 5;
		BandeDeSingesErratiques bande = ile.getSingesErratiques();
		SingeErratique s1 = new SingeErratique(6, 6, this.ile);
		bande.ajoutSinge(s1);

		assertTrue("deplacement non permis alors que coordonnées OK avec singes",
				this.singe.deplacementSingeOk(positionX, positionY));
	}

	@Test
	/**
	 * test si le déplacement n'est pas permis à cause d'un singe
	 */
	public void testDeplacementSingeOK_KOSingeBloquant() {
		int positionX = 5;
		int positionY = 5;
		BandeDeSingesErratiques bande = ile.getSingesErratiques();
		SingeErratique s1 = new SingeErratique(positionX, positionY, this.ile);
		bande.ajoutSinge(s1);

		assertFalse("deplacement permis alors que singe bloquant",
				this.singe.deplacementSingeOk(positionX, positionY));
	}

	@Test
	/**
	 * test si le déplacement n'est pas permis parce qu'il y a de l'eau
	 */
	public void testDeplacementSingeOK_KOEau() {
		int positionX = 0;
		int positionY = 1;
		BandeDeSingesErratiques bande = ile.getSingesErratiques();
		SingeErratique s1 = new SingeErratique(6, 6, this.ile);
		bande.ajoutSinge(s1);

		assertFalse("deplacement permis alors que eau présente",
				this.singe.deplacementSingeOk(positionX, positionY));
	}

	//--- Tests des déplacements effectués
	@Test
	/**
	 * test des déplacements effectués : deplacement permis
	 */
	public void testDeplacementFait_OKDeplacementPermis() {
		int positionX = 5;
		int positionY = 5;

		BandeDeSingesErratiques bande = this.ile.getSingesErratiques();
		SingeErratique s1 = new SingeErratique(6, 6, this.ile);
		bande.ajoutSinge(s1);

		assertTrue("deplacement non effectué alors que permis",
				this.singe.deplacementFait(positionX, positionY));
		assertEquals("X n a pas changé", positionX, this.singe.getX());
		assertEquals("Y n a pas changé", positionY, this.singe.getY());
	}

	@Test
	/**
	 * test des déplacements effectués : deplacement non permis
	 */
	public void testDeplacementFait_KODeplacementNonPermis() {
		int positionX = 5;
		int positionY = 5;

		BandeDeSingesErratiques bande = ile.getSingesErratiques();
		SingeErratique s1 = new SingeErratique(positionX, positionY, this.ile);
		bande.ajoutSinge(s1);

		assertFalse("deplacement non effectué alors que permis", this.singe.deplacementFait(positionX, positionY));
		assertFalse("X changé", this.singe.getX() == positionX);
		assertFalse("Y changé", this.singe.getY() == positionY);
	}

	//--- Tests de l'aléatoire
	@Test
	/**
	 * Tests de l'aléatoire : Aucune position possible
	 */
	public void testDeplacerSinge_Blocage() {
		int positionX = 10;
		int positionY = 10;

		SingeErratique s1 = new SingeErratique(positionX + 1, positionY, ile);
		SingeErratique s2 = new SingeErratique(positionX - 1, positionY, ile);
		SingeErratique s3 = new SingeErratique(positionX, positionY + 1, ile);
		SingeErratique s4 = new SingeErratique(positionX, positionY - 1, ile);
		BandeDeSingesErratiques bande = ile.getSingesErratiques();
		bande.ajoutSinge(s1);
		bande.ajoutSinge(s2);
		bande.ajoutSinge(s3);
		bande.ajoutSinge(s4);

		singe.deplacerSinge();

		assertEquals("X changé", positionX, this.singe.getX());
		assertEquals("Y a changé", positionY, this.singe.getY());
	}

	@Test
	/**
	 * Tests de l'aléatoire : Seule position possible -> South
	 */
	public void testDeplacerSinge_South() {
		int positionX = 10;
		int positionY = 10;

		SingeErratique s1 = new SingeErratique(positionX + 1, positionY, ile);
		SingeErratique s2 = new SingeErratique(positionX - 1, positionY, ile);
		SingeErratique s3 = new SingeErratique(positionX, positionY + 1, ile);
		BandeDeSingesErratiques bande = ile.getSingesErratiques();
		bande.ajoutSinge(s1);
		bande.ajoutSinge(s2);
		bande.ajoutSinge(s3);

		singe.deplacerSinge();

		assertEquals("X changé", positionX, this.singe.getX());
		assertEquals("Y incorrect", positionY - 1, this.singe.getY());
	}

	@Test
	/**
	 * Tests de l'aléatoire : Seule position possible -> North
	 */
	public void testDeplacerSinge_North() {
		int positionX = 10;
		int positionY = 10;

		SingeErratique s1 = new SingeErratique(positionX + 1, positionY, ile);
		SingeErratique s2 = new SingeErratique(positionX - 1, positionY, ile);
		SingeErratique s4 = new SingeErratique(positionX, positionY - 1, ile);
		BandeDeSingesErratiques bande = ile.getSingesErratiques();
		bande.ajoutSinge(s1);
		bande.ajoutSinge(s2);
		bande.ajoutSinge(s4);

		singe.deplacerSinge();

		assertEquals("X changé", positionX, this.singe.getX());
		assertEquals("Y incorrect", positionY + 1, this.singe.getY());
	}

	@Test
	/**
	 * Tests de l'aléatoire : Seule position possible -> West
	 */
	public void testDeplacerSinge_West() {
		int positionX = 10;
		int positionY = 10;

		SingeErratique s1 = new SingeErratique(positionX + 1, positionY, ile);
		SingeErratique s3 = new SingeErratique(positionX, positionY + 1, ile);
		SingeErratique s4 = new SingeErratique(positionX, positionY - 1, ile);
		BandeDeSingesErratiques bande = ile.getSingesErratiques();
		bande.ajoutSinge(s1);
		bande.ajoutSinge(s3);
		bande.ajoutSinge(s4);
		singe.deplacerSinge();

		assertEquals("X incorrect", positionX - 1, this.singe.getX());
		assertEquals("Y incorrect", positionY, this.singe.getY());
	}

	@Test
	/**
	 * Tests de l'aléatoire : Seule position possible -> East
	 */
	public void testDeplacerSinge_East() {
		int positionX = 10;
		int positionY = 10;

		SingeErratique s2 = new SingeErratique(positionX - 1, positionY, ile);
		SingeErratique s3 = new SingeErratique(positionX, positionY + 1, ile);
		SingeErratique s4 = new SingeErratique(positionX, positionY - 1, ile);
		BandeDeSingesErratiques bande = ile.getSingesErratiques();
		bande.ajoutSinge(s2);
		bande.ajoutSinge(s3);
		bande.ajoutSinge(s4);
		singe.deplacerSinge();

		assertEquals("X incorrect", positionX + 1, this.singe.getX());
		assertEquals("Y incorrect", positionY, this.singe.getY());
	}

	@Test
	/**
	 * Tests de l'aléatoire : Equiprobabilité des possibilités
	 */
	public void testDeplacerSinge_Equiprobabilite() {
		int deplacement_North = 0;
		int deplacement_South = 0;
		int deplacement_West = 0;
		int deplacement_East = 0;

		int positionX = 10;
		int positionY = 10;

		int nbEssais = 10000;
		double nbAttendu = nbEssais / 4;
		double margeSup = nbAttendu + 200;	
		double margeInf = nbAttendu - 200;	

		for (int i = 0; i < nbEssais; i++) {
			this.singe.setPosition(positionX, positionY);

			singe.deplacerSinge();

			if (singe.getX() == positionX && singe.getY() == positionY + 1) {
				deplacement_North ++;
			}
			else if (singe.getX() == positionX && singe.getY() == positionY - 1) {
				deplacement_South ++;
			}
			else if (singe.getX() == positionX + 1 && singe.getY() == positionY) {
				deplacement_East ++;
			}
			else if (singe.getX() == positionX - 1 && singe.getY() == positionY) {
				deplacement_West ++;
			}
		}

		assertTrue("North pas équiprobable", deplacement_North >= margeInf && deplacement_North <= margeSup);
		assertTrue("South pas équiprobable", deplacement_South >= margeInf && deplacement_South <= margeSup);
		assertTrue("East pas équiprobable", deplacement_East >= margeInf && deplacement_East <= margeSup);
		assertTrue("West pas équiprobable", deplacement_West >= margeInf && deplacement_West <= margeSup);
	}

}
