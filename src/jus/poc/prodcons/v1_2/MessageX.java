package jus.poc.prodcons.v1_2;

import jus.poc.prodcons.Message;

public class MessageX implements Message {
	
	private int idProd;
	private int numMsg;
	private boolean poisonPill;
	
	public MessageX(int idProd, int numMsg, boolean poisonPill) {
		this.idProd = idProd;
		this.numMsg = numMsg;
		this.poisonPill = poisonPill;
	}
	
	public String toString()
	{
		return "(IDmsg : "+numMsg+", IDprod : "+idProd + " poisonPill "+ poisonPill +")";
		
		
	}
	

}
