package jus.poc.prodcons.v2;

import jus.poc.prodcons.Message;

public class MessageX implements Message {
	
	private int idProd;
	private int numMsg;
	
	
	public MessageX(int idProd, int numMsg) {
		this.idProd = idProd;
		this.numMsg = numMsg;
	}
	
	public String toString()
	{
		return "Le message "+numMsg+"est produit par "+idProd;
		
		
	}

}
