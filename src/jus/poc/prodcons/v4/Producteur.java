package jus.poc.prodcons.v4;

import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Producteur;
import jus.poc.prodcons.v1.TestProdCons;

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
	 * Renvoie le nombre de messages restants a produire
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
				System.out.println("\t\tCreation : " + msg + " quantite : " + msg.getNbAConso());
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
		synchronized(this){
			TestProdCons.producteurAlive--;
		}
		Affichage.countProd();
		if(TestProdCons.producteurAlive == 0)
		{
			Affichage.printLastSurvivor(this);
			while(TestProdCons.consommateurAlive > 0)
			{
				try {
					MessageX msg = new MessageX(identification(),nbMsgProduit, 1, true);
					Affichage.printCreaMsgV4(msg, msg.getNbAConso());
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
		Affichage.printStop(this);
	}



}