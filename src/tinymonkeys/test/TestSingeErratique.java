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
 * Classe de test.
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
	public void setUp() throws Exception {
		this.ile = EasyMock.createMock(Ile.class);
		this.singe = new SingeErratique(10, 10, ile);
	}

	//déplacement de chaque côté
	//blocage
	//équiprobabilité
	
	//est ce que le singe se déplace bien
	//1 seule posibilité à droite, à gauche,...
	
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
	 * 
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
		assertTrue("X n a pas changé", this.singe.getX() == positionX);
		assertTrue("Y n a pas changé", this.singe.getY() == positionY);
	}
	
	@Test
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
	
	@Test
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
		
		assertTrue("X changé", this.singe.getX() == positionX);
		assertTrue("Y a changé", this.singe.getY() == positionY);
	}
}
