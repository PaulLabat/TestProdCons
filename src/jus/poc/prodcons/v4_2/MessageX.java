package jus.poc.prodcons.v4_2;

import jus.poc.prodcons.Message;

public class MessageX implements Message {
	
	private int idProd;
	private int numMsg;
	private int nbConso;
	private int nbAConso;
	private boolean poisonPill;
	
	public MessageX(int idProd, int numMsg, int nbAConso, boolean poisonPill) {
		this.idProd = idProd;
		this.numMsg = numMsg;
		this.nbAConso = nbAConso;
		this.poisonPill = poisonPill;
		nbConso = 0;
	}
	
	protected int getNbConso() {
		return nbConso;
	}

	protected int getNbAConso() {
		return nbAConso;
	}

	public String toString()
	{
		return "(IDmsg : "+numMsg+", IDprod : "+idProd + " poisonPill "+ poisonPill +")";
	}
	public void consommation(){
		nbConso++;
	}

	public boolean destruction(){
		return ((nbAConso - nbConso) == 0);
	}
}