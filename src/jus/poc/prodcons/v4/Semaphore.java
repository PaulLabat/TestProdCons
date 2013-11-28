package jus.poc.prodcons.v4;

import java.util.ArrayList;

public class Semaphore {

	private int cpt;
	
	public Semaphore(int taille) {
		cpt = taille;
	}
	
	public synchronized void p() throws InterruptedException
	{
		if(--cpt < 0){
			wait();
		}
	}
	
	public synchronized void v()
	{
		if(++cpt <= 0){
			notifyAll();
		}
	}
	
	

}
