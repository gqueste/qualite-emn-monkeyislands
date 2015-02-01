package tinymonkeys.test;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import tinymonkeys.modele.BandeDeSingesErratiques;
import tinymonkeys.modele.Ile;
import tinymonkeys.modele.Pirate;
import tinymonkeys.modele.SingeErratique;

/**
 * Classe de test pour la classe SingeErratique en version mockée.
 * @author Gabriel Queste
 *
 */
public class TestSingeErratique extends TestCase{
	
	/**
	 * attribut ile.
	 */
	private Ile ile;
	
	/**
	 * attribut singe, sur lequel les tests sont focalisés.
	 */
	private SingeErratique singe;
	

	@Before
	/**
	 * Mocke l'ile et initialise le singe.
	 */
	public void setUp() throws Exception {
		this.ile = EasyMock.createMock(Ile.class);
		this.singe = new SingeErratique(10, 10, ile);
	}

	//versions non bouchonnée ensuite pour voir s'il n'y a pas des erreurs plus tard
	
	
	//--- Tests des déplacements permis
	@Test
	/**
	 * test si le déplacement est permis alors qu'il n'y a aucun singe placé
	 */
	public void testDeplacementSingeOK_OKAucunSinge() {
		int positionX = 5;
		int positionY = 5;
		EasyMock.expect(this.ile.estPositionSurTerre(positionX, positionY)).andReturn(true);
		EasyMock.expect(this.ile.getSingesErratiques()).andReturn(new BandeDeSingesErratiques(this.ile));
		EasyMock.replay(this.ile);
		
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
		BandeDeSingesErratiques bande = new BandeDeSingesErratiques(this.ile);
		SingeErratique s1 = new SingeErratique(6, 6, this.ile);
		bande.ajoutSinge(s1);

		EasyMock.expect(this.ile.estPositionSurTerre(positionX, positionY)).andReturn(true);
		EasyMock.expect(this.ile.getSingesErratiques()).andReturn(bande);
		EasyMock.replay(this.ile);
		
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
		BandeDeSingesErratiques bande = new BandeDeSingesErratiques(this.ile);
		SingeErratique s1 = new SingeErratique(positionX, positionY, this.ile);
		bande.ajoutSinge(s1);

		EasyMock.expect(this.ile.estPositionSurTerre(positionX, positionY)).andReturn(true);
		EasyMock.expect(this.ile.getSingesErratiques()).andReturn(bande);
		EasyMock.replay(this.ile);
		
		assertFalse("deplacement permis alors que singe bloquant",
				this.singe.deplacementSingeOk(positionX, positionY));
	}
	
	@Test
	/**
	 * test si le déplacement n'est pas permis parce qu'il y a de l'eau
	 */
	public void testDeplacementSingeOK_KOEau() {
		int positionX = 5;
		int positionY = 5;
		BandeDeSingesErratiques bande = new BandeDeSingesErratiques(this.ile);
		SingeErratique s1 = new SingeErratique(6, 6, this.ile);
		bande.ajoutSinge(s1);
		
		EasyMock.expect(this.ile.estPositionSurTerre(positionX, positionY)).andReturn(false);
		EasyMock.expect(this.ile.getSingesErratiques()).andReturn(bande);
		EasyMock.replay(this.ile);
		
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
		
		BandeDeSingesErratiques bande = new BandeDeSingesErratiques(this.ile);
		SingeErratique s1 = new SingeErratique(6, 6, this.ile);
		bande.ajoutSinge(s1);
		
		EasyMock.expect(this.ile.estPositionSurTerre(positionX, positionY)).andReturn(true);
		EasyMock.expect(this.ile.getSingesErratiques()).andReturn(bande);
		EasyMock.replay(this.ile);
		
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
		
		BandeDeSingesErratiques bande = new BandeDeSingesErratiques(this.ile);
		SingeErratique s1 = new SingeErratique(5, 5, this.ile);
		bande.ajoutSinge(s1);

		EasyMock.expect(this.ile.estPositionSurTerre(positionX, positionY)).andReturn(true);
		EasyMock.expect(this.ile.getSingesErratiques()).andReturn(bande);
		EasyMock.replay(this.ile);
		
		assertFalse("deplacement non effectué alors que permis", this.singe.deplacementFait(positionX, positionY));
		assertFalse("X changé", this.singe.getX() == positionX);
		assertFalse("Y changé", this.singe.getY() == positionY);
	}
	
	//--- Tests des interactions avec le pirate
	@Test
	/**
	 * Tue le pirate s'il se trouve à l'endroit du déplacement
	 */
	public void testGereDeplacementSingePirate_OKTuePirate() {
		Pirate pirate = EasyMock.createMock(Pirate.class);
		
		EasyMock.expect(this.ile.getPirate()).andReturn(pirate);
		EasyMock.replay(this.ile);
		
		EasyMock.expect(pirate.getX()).andReturn(10);
		EasyMock.expect(pirate.getY()).andReturn(10);
		
		pirate.tuerPirate();
		EasyMock.expectLastCall().once();
		
		EasyMock.replay(pirate);
		
		singe.gereDeplacementSingePirate();
	}
	
	@Test
	/**
	 * Ne tue pas le pirate s'il ne se trouve pas à l'endroit du déplacement
	 */
	public void testGereDeplacementSingePirate_KOTuePirate() {
		//1
		Pirate pirate = EasyMock.createMock(Pirate.class);
		
		EasyMock.expect(this.ile.getPirate()).andReturn(pirate);
		EasyMock.expect(pirate.getX()).andReturn(10);
		EasyMock.expect(pirate.getY()).andReturn(5);
		
		EasyMock.expect(this.ile.getPirate()).andReturn(pirate);
		EasyMock.expect(pirate.getX()).andReturn(5);
		EasyMock.expect(pirate.getY()).andReturn(10);
		
		EasyMock.replay(this.ile);
		EasyMock.replay(pirate);
		
		singe.gereDeplacementSingePirate();
		singe.gereDeplacementSingePirate();
	}
	
	//--- Tests de l'aléatoire
	@Test
	/**
	 * Tests de l'aléatoire : Aucune position possible
	 */
	public void testDeplacerSinge_Blocage() {
		int positionX = 10;
		int positionY = 10;
		
		Pirate pirate = EasyMock.createMock(Pirate.class);
		
		EasyMock.expect(this.ile.estPositionSurTerre(positionX + 1, positionY)).andReturn(false).anyTimes();
		EasyMock.expect(this.ile.estPositionSurTerre(positionX - 1, positionY)).andReturn(false).anyTimes();
		EasyMock.expect(this.ile.estPositionSurTerre(positionX, positionY + 1)).andReturn(false).anyTimes();
		EasyMock.expect(this.ile.estPositionSurTerre(positionX, positionY - 1)).andReturn(false).anyTimes();
		
		EasyMock.expect(this.ile.getPirate()).andReturn(pirate);
		
		EasyMock.expect(pirate.getX()).andReturn(3);
		
		EasyMock.replay(this.ile);
		EasyMock.replay(pirate);
		singe.deplacerSinge();
		
		assertEquals("X changé", positionX, this.singe.getX());
		assertEquals("Y a changé", positionY, this.singe.getY());
	}
	
	@Test
	/**
	 * Tests de l'aléatoire : Seule position possible -> North
	 */
	public void testDeplacerSinge_South() {
		int positionX = 10;
		int positionY = 10;
		
		Pirate pirate = EasyMock.createMock(Pirate.class);
		
		EasyMock.expect(this.ile.estPositionSurTerre(positionX + 1, positionY)).andReturn(false).anyTimes();
		EasyMock.expect(this.ile.estPositionSurTerre(positionX - 1, positionY)).andReturn(false).anyTimes();
		EasyMock.expect(this.ile.estPositionSurTerre(positionX, positionY + 1)).andReturn(false).anyTimes();
		EasyMock.expect(this.ile.estPositionSurTerre(positionX, positionY - 1)).andReturn(true).anyTimes();
		
		BandeDeSingesErratiques bande = new BandeDeSingesErratiques(this.ile);
		SingeErratique s1 = new SingeErratique(5, 5, this.ile);
		bande.ajoutSinge(s1);
		
		EasyMock.expect(this.ile.getSingesErratiques()).andReturn(bande);
		
		EasyMock.expect(this.ile.getPirate()).andReturn(pirate);
		
		EasyMock.expect(pirate.getX()).andReturn(3);
		
		EasyMock.replay(this.ile);
		EasyMock.replay(pirate);
		singe.deplacerSinge();
		
		assertEquals("X changé", positionX, this.singe.getX());
		assertEquals("Y incorrect", positionY - 1, this.singe.getY());
	}
	
	@Test
	/**
	 * Tests de l'aléatoire : Seule position possible -> South
	 */
	public void testDeplacerSinge_North() {
		int positionX = 10;
		int positionY = 10;
		
		Pirate pirate = EasyMock.createMock(Pirate.class);
		
		EasyMock.expect(this.ile.estPositionSurTerre(positionX + 1, positionY)).andReturn(false).anyTimes();
		EasyMock.expect(this.ile.estPositionSurTerre(positionX - 1, positionY)).andReturn(false).anyTimes();
		EasyMock.expect(this.ile.estPositionSurTerre(positionX, positionY + 1)).andReturn(true).anyTimes();
		EasyMock.expect(this.ile.estPositionSurTerre(positionX, positionY - 1)).andReturn(false).anyTimes();
		
		BandeDeSingesErratiques bande = new BandeDeSingesErratiques(this.ile);
		SingeErratique s1 = new SingeErratique(5, 5, this.ile);
		bande.ajoutSinge(s1);
		
		EasyMock.expect(this.ile.getSingesErratiques()).andReturn(bande);
		
		EasyMock.expect(this.ile.getPirate()).andReturn(pirate);
		
		EasyMock.expect(pirate.getX()).andReturn(3);
		
		EasyMock.replay(this.ile);
		EasyMock.replay(pirate);
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
		
		Pirate pirate = EasyMock.createMock(Pirate.class);
		
		EasyMock.expect(this.ile.estPositionSurTerre(positionX + 1, positionY)).andReturn(false).anyTimes();
		EasyMock.expect(this.ile.estPositionSurTerre(positionX - 1, positionY)).andReturn(true).anyTimes();
		EasyMock.expect(this.ile.estPositionSurTerre(positionX, positionY + 1)).andReturn(false).anyTimes();
		EasyMock.expect(this.ile.estPositionSurTerre(positionX, positionY - 1)).andReturn(false).anyTimes();
		
		BandeDeSingesErratiques bande = new BandeDeSingesErratiques(this.ile);
		SingeErratique s1 = new SingeErratique(5, 5, this.ile);
		bande.ajoutSinge(s1);
		
		EasyMock.expect(this.ile.getSingesErratiques()).andReturn(bande);
		
		EasyMock.expect(this.ile.getPirate()).andReturn(pirate);
		
		EasyMock.expect(pirate.getX()).andReturn(3);
		
		EasyMock.replay(this.ile);
		EasyMock.replay(pirate);
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
		
		Pirate pirate = EasyMock.createMock(Pirate.class);
		
		EasyMock.expect(this.ile.estPositionSurTerre(positionX + 1, positionY)).andReturn(true).anyTimes();
		EasyMock.expect(this.ile.estPositionSurTerre(positionX - 1, positionY)).andReturn(false).anyTimes();
		EasyMock.expect(this.ile.estPositionSurTerre(positionX, positionY + 1)).andReturn(false).anyTimes();
		EasyMock.expect(this.ile.estPositionSurTerre(positionX, positionY - 1)).andReturn(false).anyTimes();
		
		BandeDeSingesErratiques bande = new BandeDeSingesErratiques(this.ile);
		SingeErratique s1 = new SingeErratique(5, 5, this.ile);
		bande.ajoutSinge(s1);
		
		EasyMock.expect(this.ile.getSingesErratiques()).andReturn(bande);
		
		EasyMock.expect(this.ile.getPirate()).andReturn(pirate);
		
		EasyMock.expect(pirate.getX()).andReturn(3);
		
		EasyMock.replay(this.ile);
		EasyMock.replay(pirate);
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
		
		Pirate pirate = EasyMock.createMock(Pirate.class);
		
		EasyMock.expect(this.ile.estPositionSurTerre(positionX + 1, positionY)).andReturn(true).anyTimes();
		EasyMock.expect(this.ile.estPositionSurTerre(positionX - 1, positionY)).andReturn(true).anyTimes();
		EasyMock.expect(this.ile.estPositionSurTerre(positionX, positionY + 1)).andReturn(true).anyTimes();
		EasyMock.expect(this.ile.estPositionSurTerre(positionX, positionY - 1)).andReturn(true).anyTimes();
		
		BandeDeSingesErratiques bande = new BandeDeSingesErratiques(this.ile);
		SingeErratique s1 = new SingeErratique(5, 5, this.ile);
		bande.ajoutSinge(s1);
		
		EasyMock.expect(this.ile.getSingesErratiques()).andReturn(bande).anyTimes();;
		
		EasyMock.expect(this.ile.getPirate()).andReturn(pirate).anyTimes();;
		
		EasyMock.expect(pirate.getX()).andReturn(3).anyTimes();;
		
		EasyMock.replay(this.ile);
		EasyMock.replay(pirate);
		
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
