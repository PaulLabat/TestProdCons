package jus.poc.prodcons.v2;

import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Message;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;

public class Consommateur extends Acteur implements _Consommateur {

	private int nbMsgProduit;
	private Tampon tampon;
	private Aleatoire alea;
	
	
	public Consommateur(Observateur observateur, int moyenneTempsDeTraitement, int deviationTempsDeTraitement, Tampon tampon, Aleatoire alea) throws ControlException {
		super(Acteur.typeConsommateur, observateur, moyenneTempsDeTraitement, deviationTempsDeTraitement);
		this.alea = alea;
		this.tampon = tampon;
		nbMsgProduit = 0;
	}
	/**
	 * retourne le nbre de message traites
	 */
	public int nombreDeMessages() {
		return nbMsgProduit;
	}
	
	
	public void run()
	{
		while(true)
		{
			try {
				Message msg = tampon.get(this);// recupere le message depuis le tampon
				System.out.println("Le consommateur "+identification() + " a lu le message "+msg);
				synchronized(this){
					nbMsgProduit++;
					int wait = 10*alea.next();
					System.out.println("Consommateur "+identification()+" wait " + wait);
					wait(wait);
				}
				
				
			} catch ( Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//
		}
	}
	
	

}
