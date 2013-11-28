package jus.poc.prodcons.v4;

import jus.poc.prodcons.Message;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;

public class ProdCons implements Tampon {

	private MessageX[] msg;
	private int debut = 0;
	private int fin = 0;
	private int cpt = 0;
	
	// Creation des 3 Semaphores 
	public Semaphore consoLibre;
	public Semaphore prodLibre;
	public Semaphore mutex;
	public Observateur obs;
	public Semaphore[] nbMessages;
	public boolean stop = false;
	
	public ProdCons(int taille, Observateur obsParam) {
		msg = new MessageX[taille];
		consoLibre = new Semaphore(0);
		prodLibre = new Semaphore(taille);
		mutex = new Semaphore(1);
		nbMessages = new Semaphore[taille];
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
		MessageX m;
		consoLibre.p(); // on verifie la presence de ressources
		if (stop) { // si on a stoppé le tampon, on rend la sémaphore passante et on retourne null
            consoLibre.v();
            return null;
        }
		mutex.p(); // acce unique au buffer
		int stock = debut; //stockage de la valeur debut
		m = msg[debut];
		obs.retraitMessage(arg0, m);
		m.consommationCopie();
		System.out.println("test1");
		
		if(m.getNbConso() > 0){
			System.out.println("test2");
			prodLibre.v();
			System.out.println("test3");
			mutex.v();
			System.out.println("test4");
			nbMessages[stock].p();
			System.out.println("test5");
			mutex.p();
			nbMessages[stock].v();
		}else{
			System.out.println("test3");
			msg[debut] = null;
			debut = (debut + 1) % taille();
			nbMessages[stock].v();
			consoLibre.v();
		}
		mutex.v();
		return m;
	}

	@Override
	public void put(_Producteur arg0, Message arg1) throws Exception,	InterruptedException {
		if(!(arg1 instanceof MessageX)){
			throw new Exception("Le message n'est pas un MessageX");
		}
		MessageX m = (MessageX) arg1;
        if (stop) { // si tampon annulé, on ne fait rien
            return;
        }
		prodLibre.p();
		mutex.p(); // blocage du buffer
		msg[fin] = m;
		obs.depotMessage(arg0, arg1);
		nbMessages[fin] = new Semaphore(0);
		int stock = fin;
		fin = (fin + 1) % taille();
		cpt++;
		mutex.v(); // deblocage du buffer
		consoLibre.v(); // pour avertir les consommateurs
		
		nbMessages[stock].p();
		nbMessages[stock].v();
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
