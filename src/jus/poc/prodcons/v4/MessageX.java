package jus.poc.prodcons.v4;

import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Message;

public class MessageX implements Message {
	
	private int idProd;
	private int numMsg;
	private int nbMsg;
	private int nbConso = 0;
	
	
	protected int getNbConso() {
		return nbConso;
	}

	public MessageX(int idProd, int numMsg, int nb) {
		this.idProd = idProd;
		this.numMsg = numMsg;
		this.nbMsg = nb;
	}
	
	public String toString()
	{
		return "(IDmsg : "+numMsg+", IDprod : "+idProd + ", NBCopiesRestantes : " + (nbMsg - nbConso) + ")";
		
		
	}

	protected int getNumMsg() {
		return numMsg;
	}

	protected int getNbMsg() {
		return nbMsg;
	}

	public void consommationCopie() throws ControlException {
		if(getNbMsg() > 0){
			nbConso++;
		}else{
			throw new ControlException(this.getClass(), "erreur à la consommation?");
		}
	}
}
