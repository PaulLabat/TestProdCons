package jus.poc.prodcons.v3;

import jus.poc.prodcons.Message;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;

public class ProdCons implements Tampon {

	private Message[] msg;
	private int debut = 0;
	private int fin = 0;
	private int cpt = 0;
	
	// Creation des 3 Semaphores 
	public Semaphore consoLibre;
	public Semaphore prodLibre;
	public Semaphore mutex;
	public Observateur obs;
	
	public ProdCons(int taille, Observateur obsParam) {
		msg = new Message[taille];
		consoLibre = new Semaphore(0);
		prodLibre = new Semaphore(taille);
		mutex = new Semaphore(1);
		this.obs = obsParam;
	}

	/**
	 * Nombre de message dans le buffer
	 */
	@Override
	public int enAttente() {
		return cpt;
	}

	@Override
	public Message get(_Consommateur arg0) throws Exception,InterruptedException {
		Message m;
		consoLibre.p(); // on verifie la presence de ressources
		mutex.p(); // acce unique au buffer
		m = msg[debut];
		obs.retraitMessage(arg0, m);
		debut = (debut + 1) % taille();
		cpt--;
		mutex.v(); // deblocage de l'acce au buffer
		prodLibre.v(); // pour avertir les producteurs
		return m;
	}

	@Override
	public void put(_Producteur arg0, Message arg1) throws Exception,	InterruptedException {
		prodLibre.p();
		mutex.p(); // blocage du buffer
		msg[fin] = arg1;
		obs.depotMessage(arg0, arg1);
		fin = (fin + 1) % taille();
		cpt++;
		mutex.v(); // deblocage du buffer
		consoLibre.v(); // pour avertir les consommateurs
	}

	/**
	 * Taille du buffer
	 */
	@Override
	public int taille() {
		return msg.length;
	}

	/**
	 * Renvoie vrai si le buffer est plein
	 * @return cpt == taille()
	 */
	private boolean isPlein()
	{
		return cpt == taille();
	}
	/**
	 * Renvoie vrai si le buffer est vide
	 * @return cpt == 0
	 */
	private boolean isVide()
	{
		return cpt == 0;
	}
	

}
