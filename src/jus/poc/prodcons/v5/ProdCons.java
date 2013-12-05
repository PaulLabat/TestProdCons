package jus.poc.prodcons.v5;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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

	final Lock verouille;
	final Condition nonPlein; 
	final Condition nonVide;

	public ProdCons(int taille, Observateur obsParam) {
		msg = new Message[taille];
		consoLibre = new Semaphore(0);
		prodLibre = new Semaphore(taille);
		mutex = new Semaphore(1);
		this.obs = obsParam;
		this.verouille = new ReentrantLock();
		this.nonPlein = verouille.newCondition();
		this.nonVide = verouille.newCondition();
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
		verouille.lock();
		try{
			while(this.isVide()){
				nonVide.await();
			}
			m = (MessageX) msg[debut];
			obs.retraitMessage(arg0, m);
			debut = (debut + 1) % taille();
			cpt--;
			System.out.println("\t\tConsommateur "+arg0.identification()+" a recupere le msg : "+m);
			nonPlein.signal();
			return m;
		}finally{
			verouille.unlock();
		}
	}

	@Override
	public void put(_Producteur arg0, Message arg1) throws Exception,	InterruptedException {
		verouille.lock();
		try{
			while(this.isPlein()){
				nonPlein.await();
			}
			msg[fin] = arg1;
			obs.depotMessage(arg0, arg1);
			fin = (fin + 1) % taille();
			cpt++;
			System.out.println("\tProducteur "+arg0.identification()+" a depose le msg : "+arg1);
			nonVide.signal();
		}finally{
			verouille.unlock();
		}
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
