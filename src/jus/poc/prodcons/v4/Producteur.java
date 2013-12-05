package jus.poc.prodcons.v4;

import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Message;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Producteur;
import jus.poc.prodcons.v4.MessageX;

public class Producteur extends Acteur implements _Producteur {

	private int nbMessage;
	private int nbMsgProduit;
	private Tampon tampon;
	private Aleatoire alea;
	private Aleatoire nbMsg;
	
	public Producteur(Observateur observateur, int moyenneTempsDeTraitement, int deviationTempsDeTraitement, int nbMessage, Tampon tampon, Aleatoire alea, Aleatoire nbMes) throws ControlException {
		super(Acteur.typeProducteur, observateur, moyenneTempsDeTraitement, deviationTempsDeTraitement);
		this.nbMessage = nbMessage;
		this.alea = alea;
		this.tampon = tampon;
		this.nbMsg = nbMes;
		nbMsgProduit = 0;
	}
	
	/**
	 * Renvoie le nombre de messages restants à produire
	 * @return nbMessage - nbMsgProduit
	 */
	@Override
	public int nombreDeMessages() {
		return nbMessage - nbMsgProduit;
	}
	
	public void run()
	{
		while(nbMsgProduit < nbMessage)//la garde
		{
			try {
				MessageX msg = new MessageX(identification(),nbMsgProduit, nbMsg.next());
				System.out.println("Creation : Producteur "+identification()+" a produit le msg en quantite " + msg.getNbAConso() + " : "+msg);
				tampon.put(this, msg);
				synchronized(this){
					nbMsgProduit++; 
					int wait = 10*alea.next();
					observateur.productionMessage(this, msg, wait);
					//System.out.println("Creation : Producteur "+identification()+" a produit le msg en quantite " + msg.getNbAConso() + " : "+msg);
					//System.out.println("Producteur" + identification()+ " wait "+wait);
					wait(wait);
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		System.out.println("Stop : producteur  " + identification());
	}
	
	

}
