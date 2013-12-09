package jus.poc.prodcons.v4;

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
	//private int tBuffer = 0;
	
	// Creation des 3 Semaphores 
	public Semaphore consoLibre;
	public Semaphore prodLibre;
	public Semaphore mutex;
	public Observateur obs;
	public Semaphore lecProd;
	public int taille;
	
	public ProdCons(int taille2, Observateur obsParam) {
		msg = new Message[taille2];
		consoLibre = new Semaphore(0);
		prodLibre = new Semaphore(taille2);
		this.taille = taille2;
		mutex = new Semaphore(1);
		lecProd = new Semaphore(0);
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
		mutex.p(); // acce unique au buffer
		m = (MessageX) msg[debut];
		m.consommation();
		obs.retraitMessage(arg0, m);
		//System.out.println("\t\tConsommateur "+arg0.identification()+" a recupere le msg : "+m);
		if(m.destruction()){
			debut = (debut + 1) % taille;
			//cpt++;
			//tBuffer--;
			System.out.println("\t\tRecuperation IDCons "+arg0.identification()+" : "+m);
			mutex.v(); // Lib�ration de l'acce au buffer
			prodLibre.v(); //Avertissement des producteurs
			lecProd.v(); // Liberation des prod bloques
		}else{
			System.out.println("\t\tRecuperation IDCons "+arg0.identification()+" : "+m);
			mutex.v();
			consoLibre.v();
		}
		
		return m;
	}

	@Override
	public void put(_Producteur arg0, Message arg1) throws Exception,	InterruptedException {
		prodLibre.p();
		mutex.p(); // blocage du buffer
		msg[fin] = arg1;
		obs.depotMessage(arg0, arg1);
		fin = (fin + 1) % taille();
		//cpt++;
		System.out.println("\tDepot : "+arg1);
		mutex.v(); // deblocage du buffer
		consoLibre.v(); // pour avertir les consommateurs
		lecProd.p(); // blocage du producteur tant qu'un message n'est pas lu X fois.
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