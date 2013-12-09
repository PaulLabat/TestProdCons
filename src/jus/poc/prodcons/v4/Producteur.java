package jus.poc.prodcons.v4;

import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Producteur;

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
				MessageX msg = new MessageX(identification(),nbMsgProduit, nbMsg.next(), false);
				System.out.println("\tCreation : " + msg + " quantite : " + msg.getNbAConso());
				int wait = 10*alea.next();
				observateur.productionMessage(this, msg, wait);
				tampon.put(this, msg);
				synchronized(this){
					nbMsgProduit++; 

					//System.out.println("Creation : Producteur "+identification()+" a produit le msg en quantite " + msg.getNbAConso() + " : "+msg);
					//System.out.println("Producteur" + identification()+ " wait "+wait);
					sleep(wait);
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		//code qui tue les consommateurs
				TestProdCons.producteurAlive--;
				System.out.println("producteurAlive : "+TestProdCons.producteurAlive);
				if(TestProdCons.producteurAlive == 0)
				{
					System.out.println("Je suis le dernier prod, je tue tous le monde : id "+ this.identification());
					while(TestProdCons.consommateurAlive > 0)
					{
						try {
							MessageX msg = new MessageX(identification(),nbMsgProduit, 1, true);
							System.out.println("\tCreation : "+msg);
							int wait = 10*alea.next();
							observateur.productionMessage(this, msg, wait);
							tampon.put(this, msg);
							synchronized(this){
								nbMsgProduit++; 
								
								//observateur.productionMessage(this, msg, wait);
								//System.out.println("Creation : Producteur "+identification()+" a produit le msg en quantite " + msg.getNbAConso() + " : "+msg);
								//System.out.println("Producteur" + identification()+ " wait "+wait);
								sleep(wait);
							}
							
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						TestProdCons.consommateurAlive--;
					}
				}
		System.out.println("Stop : producteur  " + identification());
	}
	
	

}