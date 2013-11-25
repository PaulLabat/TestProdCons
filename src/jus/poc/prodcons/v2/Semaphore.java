package jus.poc.prodcons.v2;

import java.util.ArrayList;

public class Semaphore {

	private int cpt;
	private ArrayList<Thread> liste;
	
	public Semaphore(int taille) {
		cpt = taille;
		liste = new ArrayList();
	}
	
	public synchronized void p()
	{
		
	}
	
	public synchronized void v()
	{
		
	}
	
	

}
