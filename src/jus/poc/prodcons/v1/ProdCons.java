package jus.poc.prodcons.v1;

import jus.poc.prodcons.Message;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;

public class ProdCons implements Tampon {

	private Message[] msg;
	private int debut = 0;
	private int fin = 0;
	private int cpt = 0;


	public ProdCons(int taille) {
		msg = new Message[taille];
	}

	/**
	 * Nombre de message dans le buffer
	 */
	@Override
	public int enAttente() {
		return cpt;
	}

	@Override
	public synchronized Message get(_Consommateur arg0) throws Exception,InterruptedException {
		while(isVide())//tant que le buffer est vide, on attend
		{
			wait();
		}
		Message m = msg[debut];
		debut = (debut + 1) % taille();
		cpt--;
		Affichage.printRecMsg(arg0, m);
		//System.out.println(cpt);
		notifyAll();
		return m;
	}

	@Override
	public synchronized void put(_Producteur arg0, Message arg1) throws Exception,	InterruptedException {
		//System.out.println(isPlein());
		while(isPlein())//tant que le buffer est plein, on attend
		{
			wait();
		}
		msg[fin] = arg1;
		fin = (fin + 1) % taille();
		cpt++;
		Affichage.printDepMsg(arg1);
		//System.out.println(cpt);
		notifyAll();
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
