package jus.poc.prodcons.v6;

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
	private ObservationControle obst;

	public Producteur(Observateur observateur, int moyenneTempsDeTraitement,
			int deviationTempsDeTraitement, int nbMessage, Tampon tampon,
			Aleatoire alea, ObservationControle obsP) throws ControlException {
		super(Acteur.typeProducteur, observateur, moyenneTempsDeTraitement,
				deviationTempsDeTraitement);
		this.nbMessage = nbMessage;
		this.alea = alea;
		this.tampon = tampon;
		nbMsgProduit = 0;
		this.obst = obsP;
	}

	/**
	 * Renvoie le nombre de messages restants à produire
	 * 
	 * @return nbMessage - nbMsgProduit
	 */
	@Override
	public int nombreDeMessages() {
		return nbMessage - nbMsgProduit;
	}

	public void run() {
		while (nbMsgProduit < nbMessage)// la garde
		{
			try {
				Message msg = new MessageX(identification(), nbMsgProduit,
						false);
				int wait = 10 * alea.next();
				observateur.productionMessage(this, msg, wait);
				obst.productionMessage(this, msg, wait);
				Affichage.printCreaMsg(msg);
				tampon.put(this, msg);

				nbMsgProduit++;
				sleep(wait);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// code qui tue les consommateurs
		TestProdCons.producteurAlive--;
		Affichage.countProd();
		if (TestProdCons.producteurAlive == 0) {
			Affichage.printLastSurvivor(this);
			while (TestProdCons.consommateurAlive > 0) {
				try {
					Message pill = new MessageX(identification(), nbMsgProduit,
							true);
					Affichage.printCreaMsg(pill);
					int wait = 10 * alea.next();
					observateur.productionMessage(this, pill, wait);
					obst.productionMessage(this, pill, wait);

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

		Affichage.printStop(this);
	}

}