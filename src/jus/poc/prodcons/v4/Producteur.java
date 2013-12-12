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
	private int affichage;

	public Producteur(Observateur observateur, int moyenneTempsDeTraitement, int deviationTempsDeTraitement, int nbMessage, Tampon tampon, Aleatoire alea, Aleatoire nbMes, int affichage) throws ControlException {
		super(Acteur.typeProducteur, observateur, moyenneTempsDeTraitement, deviationTempsDeTraitement);
		this.nbMessage = nbMessage;
		this.alea = alea;
		this.tampon = tampon;
		this.nbMsg = nbMes;
		nbMsgProduit = 0;
		this.affichage = affichage;
	}

	/**
	 * Renvoie le nombre de messages restants a produire
	 * @return nbMessage - nbMsgProduit
	 */
	@Override
	public int nombreDeMessages() {
		return nbMessage - nbMsgProduit;
	}

	public boolean check(){
		return (nbMsgProduit+1) < nbMessage;
	}


	public void run()
	{
		while(nbMsgProduit < nbMessage)//la garde
		{
			try {
				MessageX msg = new MessageX(identification(),nbMsgProduit, nbMsg.next(), false);
				if(affichage == 1){
					System.out.println("\t\tCreation : " + msg + " quantite : " + msg.getNbAConso());
				}
				int wait = 10*alea.next();
				observateur.productionMessage(this, msg, wait);
				tampon.put(this, msg);

				nbMsgProduit++; 

				//System.out.println("Creation : Producteur "+identification()+" a produit le msg en quantite " + msg.getNbAConso() + " : "+msg);
				//System.out.println("Producteur" + identification()+ " wait "+wait);
				sleep(wait);


			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		//code qui tue les consommateurs

		//TestProdCons.producteurAlive--;
		if(TestProdCons.producteurAlive == 0)
		{
			if(affichage == 1){
				System.out.println("Je suis le dernier prod, je tue tous le monde : id " + this.identification());
			}
			while(TestProdCons.consommateurAlive > 0)
			{
				try {
					MessageX msg = new MessageX(identification(),nbMsgProduit, 1, true);
					if(affichage == 1){
						System.out.println("\t\tCreation : : " + msg);
					}
					int wait = 10*alea.next();
					observateur.productionMessage(this, msg, wait);
					tampon.put(this, msg);

					nbMsgProduit++; 

					//observateur.productionMessage(this, msg, wait);
					//System.out.println("Creation : Producteur "+identification()+" a produit le msg en quantite " + msg.getNbAConso() + " : "+msg);
					//System.out.println("Producteur" + identification()+ " wait "+wait);
					sleep(wait);


				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				TestProdCons.consommateurAlive--;
			}
		}
		if(affichage == 1){
			System.out.println("Stop : producteur " + identification());
		}
	}



}