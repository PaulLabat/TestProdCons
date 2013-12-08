package jus.poc.prodcons.v6;

import java.util.HashMap;
import java.util.HashSet;

import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Message;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;

public class ObservationControle {

	private int nbConso, nbProd, nbBuffer;

	private HashSet<_Producteur> producteurs; 
	private HashSet<_Consommateur> consommateurs;
	private HashSet<Message> msgWait;

	private HashMap<_Consommateur, Message> msgNonConso;
	private HashMap<_Producteur, Message> msgNonDepot;

	private void testProducteurExiste(_Producteur p) throws ControlException {
		if (!producteurs.contains(p)) {
			throw new ControlException(p.getClass(), "testProducteurExiste");

		}
	}


	private void testConsommateurExiste(_Consommateur c) throws ControlException {
		if (!consommateurs.contains(c)) {
			throw new ControlException(c.getClass(), "testConsommateurExiste");
		}
	}

	public void init(int nbProduc, int nbConsom, int nbBuf) throws ControlException {
		System.out.println("init " + nbProduc + " " + nbConsom + " " + nbBuf);
		this.nbBuffer = nbBuf;
		this.nbConso = nbConsom;
		this.nbProd = nbProduc;

		producteurs = new HashSet();
		consommateurs = new HashSet();
		msgWait = new HashSet();

		msgNonConso = new HashMap();
		msgNonDepot = new HashMap();
	}
	
    public void newProducteur(_Producteur p) throws ControlException {
        producteurs.add(p);
        if (producteurs.size() > nbProd) {
            throw new ControlException(this.getClass(), "newProducteur");
            //on a un peu trop de prod
        }
    }

    public void newConsommateur(_Consommateur c) throws ControlException {
        consommateurs.add(c);
        if (consommateurs.size() > nbConso) {
            throw new ControlException(this.getClass(), "newConsommateur");
            //on a un peu trop de conso cette fois :D
        }
    }

	public void productionMessage(_Producteur p, Message m, int tps) throws ControlException {
		// On verifie l'existence du producteur en premier lieu
		testProducteurExiste(p);
		if (msgNonDepot.containsKey(p)) {
			throw new ControlException(p.getClass(), "productionMessage");
			//Erreur, le producteur n'a pas déposé le dernier message produit
		}
		msgNonDepot.put(p, m);
	}

	public void depotMessage(_Producteur p, Message m) throws ControlException {
		// On verifie l'existence du producteur en premier lieu
		testProducteurExiste(p);
		Message temp = msgNonDepot.remove(p);
		if (temp == null || m != temp) {
			throw new ControlException(p.getClass(), "depotMessage");
			//Notre prod n'a pas produit de msg ou ne l'a pas depose.
		}
		if (nbBuffer < msgWait.size()) {
			throw new ControlException(p.getClass(), "depotMessage");
			//on a depasser la taille...
		}

		msgWait.add(m);
	}

	public void retraitMessage(_Consommateur c, Message m) throws ControlException {
		// on verifie d'abord l'existence du consommateur
		testConsommateurExiste(c);
		if (!msgWait.remove(m)) {
			throw new ControlException(c.getClass(), "retraitMessage");
			// msg qui n'est pas dans le tampon 
		}
		if (msgNonConso.containsKey(c)) {
			throw new ControlException(c.getClass(), "retraitMessage");
			//notre consommateur n'a pas consomme le msg qu'il a retire avant
		}

		msgNonConso.put(c, m);
	}

	public void consommationMessage(_Consommateur c, Message m, int tempsDeTraitement) throws ControlException {
		// On verifie l'existence
		testConsommateurExiste(c);
		Message temp = msgNonConso.remove(c);
		if (temp == null || m != temp) {
			throw new ControlException(c.getClass(), "consommationMessage");
			// notre conso a pas retirer ce message, ou il l'a deja consomme, ou bien il a pas consomme le msg precedent
		}
	}
	


}