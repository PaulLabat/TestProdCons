package jus.poc.prodcons.v2;

import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Message;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Producteur;

public class Producteur extends Acteur implements _Producteur {

	private int nbMessage;
	private int nbMsgProduit;
	private Tampon tampon;
	private Aleatoire alea;
	private int affichage;

	public Producteur(Observateur observateur, int moyenneTempsDeTraitement, int deviationTempsDeTraitement, int nbMessage, Tampon tampon, Aleatoire alea, int affichage) throws ControlException {
		super(Acteur.typeProducteur, observateur, moyenneTempsDeTraitement, deviationTempsDeTraitement);
		this.nbMessage = nbMessage;
		this.alea = alea;
		this.tampon = tampon;
		nbMsgProduit = 0;
		this.affichage = affichage;
	}

	/**
	 * Renvoie le nombre de messages restants à produire
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
				Message msg = new MessageX(identification(),nbMsgProduit, false);
				if(affichage == 1){
					System.out.println("\t\tCreation : "+msg);
				}
				int wait = 10*alea.next();
				tampon.put(this, msg);

				nbMsgProduit++; 
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
				System.out.println("Je suis le dernier prod, je tue tous le monde : id "+ this.identification());
			}
			while(TestProdCons.consommateurAlive > 0)
			{
				try {
					Message pill = new MessageX(identification(),nbMsgProduit, true);
					if(affichage == 1){
						System.out.println("\t\tCreation : "+pill );
					}
					int wait = 10*alea.next();
					tampon.put(this, pill);


					nbMsgProduit++; 

					sleep(wait);


				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				TestProdCons.consommateurAlive--;
			}
		}
		if(affichage == 1){
			System.out.println("Stop : producteur : " + identification());
		}
	}



}
