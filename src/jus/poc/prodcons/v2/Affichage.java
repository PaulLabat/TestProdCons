package jus.poc.prodcons.v2;

import jus.poc.prodcons.Message;
import jus.poc.prodcons._Acteur;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;

public class Affichage {

	public static void printCreaMsg(Message m){
		if (TestProdCons.affichage == 1){
			System.out.println("\t\tCreation : "+m);
		}
	}

	public static void printLecMsg(_Consommateur c, Message m){
		if(TestProdCons.affichage == 1){
			System.out.println("\t\tLecture IDCons "+c.identification() + " : "+m);
		}
	}
	
	public static void printDepMsg(Message m){
		if(TestProdCons.affichage == 1){
			System.out.println("\tDepot "+m);
		}
	}
	
	public static void printRecMsg(_Consommateur c, Message m){
		if(TestProdCons.affichage == 1){
			System.out.println("\tRecuperation IDCons "+c.identification()+" : "+m);
		}
	}
	
	public static void printStart(_Acteur a){
		if(TestProdCons.affichage == 1){
			if(a instanceof _Consommateur){
				System.out.println("Start : consommateur : " + a.identification());
			}else if(a instanceof _Producteur){
				System.out.println("Start : producteur : " + a.identification());
			}
			
		}
	}
	
	public static void printStop(_Acteur a){
		if(TestProdCons.affichage == 1){
			if(a instanceof _Consommateur){
				System.out.println("Stop : consommateur : " + a.identification() + " ayant lu " + a.nombreDeMessages() + " messages");
			}else if(a instanceof _Producteur){
				System.out.println("Stop : producteur : " + a.identification());
			}
			
		}
	}
	
	public static void printLastSurvivor(_Producteur p){
		if(TestProdCons.affichage == 1){
			System.out.println("Je suis le dernier prod, je tue tous le monde : id "+ p.identification());
		}
	}
	
	public static void countProd(){
		if(TestProdCons.affichage == 1){
			System.out.println("producteurAlive : "+TestProdCons.producteurAlive);
		}
	}
}
