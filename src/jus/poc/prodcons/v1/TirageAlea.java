package jus.poc.prodcons.v1;

import jus.poc.prodcons.Aleatoire;

public class TirageAlea extends Aleatoire {

	public TirageAlea(int moyenne, int deviation) {
		super(moyenne, deviation);
		// TODO Auto-generated constructor stub
	}

	public synchronized int next()
	{
		return super.next();

	}

}
