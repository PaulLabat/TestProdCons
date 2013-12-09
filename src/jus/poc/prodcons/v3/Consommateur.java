package jus.poc.prodcons.v3;

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
				System.out.println("\t\tLecture IDCons "+identification() + " : "+msg);

				//code pour quitter la boucle si le conso est tué
				if(msg.toString().contains("poisonPill true"))
				{
					break;
				}



				synchronized(this){
					nbMsgProduit++;
					int wait = 10*alea.next();
					observateur.consommationMessage(this, msg, wait);
					sleep(wait);
				}


			} catch ( Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//
		}
		System.out.println("Stop consommateur "+identification() + " ayant lu " + nombreDeMessages() + " messages");
	}



}